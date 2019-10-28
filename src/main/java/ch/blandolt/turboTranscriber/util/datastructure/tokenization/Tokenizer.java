package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

import ch.blandolt.turboTranscriber.util.Log;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
Documentation:
==============

Tokenizing works in the following order:

1. Multi Line Comments are extracted first.
Everything in comment is treated as simple comment text.

2. Then, single line comments are extracted.
Everything starting from '#' up to the next line break is treated as simple comment text.

3. Opening Tags and Closing Tags are identified as such and extracted.
Tag hierarchy is ignored.

4. Abbreviations are extracted.
Glyphs in abbreviations are treAted as "text" in the abbreviation for now.
They get extracted later on.

5. Extract Glyphs

6. Extract Punctuation

7. Extract Linebreaks

// TODO: update

// TODO: Add XML to format.
    Maybe something like "$XML_<some weird construct>...</>$XML_"

*/

public class Tokenizer {

    public static synchronized List<TranscriptionToken> tokenize(String text) {


        List<Tokenizable> tokens = Tokenizer.extractMultilineComments(text);
        tokens = Tokenizer.extractSingleLineComments(tokens);
        tokens = Tokenizer.extractWordborders(tokens);
        // TODO: implement special word-border-cases (e.g. `aa¬lande` for `á landi`)
        // TODO: should Linebreaks (in raw, not the anchor [lb]) work as wordborders?
        tokens = Tokenizer.extractOpeningAndClosingTags(tokens);
        tokens = Tokenizer.extractAbbreviations(tokens);
        tokens = Tokenizer.extractGlyphs(tokens);
        tokens = Tokenizer.extractPunctuationCharacters(tokens);
        tokens = Tokenizer.extractLinebreaks(tokens);
        tokens = Tokenizer.extractTextFragments(tokens);

        tokens.add(new TokenTypeWordborder(""));
        tokens.add(0, new TokenTypeWordborder(""));
        tokens = Tokenizer.removeDoubleWordBorders(tokens);

        // FIXME: when line ends on abbreviation (potentially others too), lb ends up in the previous word

        tokens = Tokenizer.segmentByWordborders(tokens);
        tokens = Tokenizer.getLegitWords(tokens);

        return castToTranscriptionTokens(tokens);
    }

    static ArrayList<TranscriptionToken> castToTranscriptionTokens(List<Tokenizable> tokens) {
        return (ArrayList<TranscriptionToken>) tokens
                .stream()
                .filter(t -> t instanceof TranscriptionToken)
                .map(t -> (TranscriptionToken) t)
                .collect(Collectors.toList());
    }

    private static List<Tokenizable> getLegitWords(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<>();

        for (Tokenizable t: tokens){
            if (t instanceof TokenTypePotentialWord){
                TokenTypePotentialWord potentialWord = (TokenTypePotentialWord) t;
                if (potentialWord.isLegit()){
                    res.add(potentialWord.getLegitWordRepresentation());
                } else {
                    res.addAll(potentialWord.getContents());
                }
            } else {
                res.add(t);  // should never happen
            }
        }

        return res;

        // TODO: find out, which tags can be in a word and which not
    }

    private static List<Tokenizable> segmentByWordborders(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<>();

        TokenTypePotentialWord potentialWord = new TokenTypePotentialWord();

        for (Tokenizable t: tokens){
            if (t instanceof TokenTypeWordborder){
                res.add(potentialWord);
                potentialWord = new TokenTypePotentialWord();
            } else {
                potentialWord.add(t);
            }
        }

        return res;
    }

    private static List<Tokenizable> removeDoubleWordBorders(List<Tokenizable> tokens) {
        LinkedList<Tokenizable> res = new LinkedList<>();

        for (Tokenizable current: tokens){
            if (current instanceof TokenTypeWordborder && !res.isEmpty()){
                Tokenizable prev = res.getLast();
                if (prev instanceof TokenTypeWordborder){
                    continue;
                } else {
                    res.add(current);
                }
            } else {
                res.add(current);
            }
        }

        return res;
    }

    private static List<Tokenizable> extractWordborders(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<>();

        for (Tokenizable t: tokens) {
            if (t instanceof TokenizableText) {
                String s = t.getText();
                s = s.replace(" ", " ___wordborder___ ");
                while (s.contains(" ___wordborder___  ___wordborder___ ")){
                    s = s.replace(" ___wordborder___  ___wordborder___ ", " ___wordborder___ ");
                }
                String[] ss = s.split(" ");
                for (String substr: ss){
                    if (substr.equals("___wordborder___")){
                        res.add(new TokenTypeWordborder(""));
                    } else {
                        res.add(new TokenizableText(substr));
                    }
                }
            } else {
                res.add(t);
            }
        }

        return res;
    }

    private static List<Tokenizable> extractTextFragments(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<Tokenizable>();

        for (Tokenizable t: tokens) {
            if (t instanceof TokenizableText) {
                String s = t.getText();
                if (s.isBlank())
                    continue;
                s = s.strip();
                String[] ss = s.split(" ");
                for (String substr: ss){
                    res.add(new TokenTypeTextFragment(substr));
                }
            } else {
                res.add(t);
            }
        }

        return res;
    }

    private static List<Tokenizable> extractLinebreaks(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<Tokenizable>();

        for (Tokenizable t: tokens) {
            if (t instanceof TokenizableText) {
                String s = t.getText();
                s = s.replace("¬\n", "___protected-linebreak___");
                s = s.replace("\n", "\n___linebreak___\n");
                s = s.replace("___protected-linebreak___", "\n___protected-linebreak___\n");
                String[] ss = s.split("\n");
                for (String substr: ss){
                    if (substr.equals("___linebreak___")){
                        // TODO: test, if this works!
                        res.add(new TokenTypeWordborder(""));
                        res.add(new TokenTypeLinebreak("", false));
                        res.add(new TokenTypeWordborder(""));
                    } else if (substr.equals("___protected-linebreak___")){
                        res.add(new TokenTypeLinebreak("", true));
                    } else {
                        res.add(new TokenizableText(substr));
                    }
                }
            } else {
                res.add(t);
            }
        }

        return res;
    }

    private static List<Tokenizable> extractPunctuationCharacters(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<Tokenizable>();

        for (Tokenizable t: tokens) {
            if (t instanceof TokenizableText) {
                String s = t.getText();
                String splittable = s;
                String regex = "([\\.])";
                // TODO: more potential allowed Punctuation Characters
                // TODO: make allowed punctuation characters dynamic




                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(s);
                splittable = m.replaceAll("___match___$1___match___");

                String[] ss = splittable.split("___match___");

                for (String substr: ss){
                    if (substr.matches(regex)){
                        res.add(new TokenTypeWordborder("")); // TODO: make dynamically dependent (hyphens are not word borders!
                        res.add(new TokenTypePunctuationCharacter(substr));
                        res.add(new TokenTypeWordborder("")); // TODO: dito
                    }  else {
                        res.add(new TokenizableText(substr));
                    }
                }
            } else {
                res.add(t);
            }
        }
        return res;
    }

    private static List<Tokenizable> extractGlyphs(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<Tokenizable>();

        for (Tokenizable t: tokens) {
            if (t instanceof TokenizableText) {
                String s = t.getText();
                if (s.contains("{")) {
                    String[] ss = s.split("\\{", 2);
                    if (!ss[0].isBlank()){
                        res.add(new TokenizableText(ss[0]));
                    }
                    ss = ss[1].split("\\}", 2);
                    res.add(new TokenTypeGlyph(ss[0]));
                    if (ss.length == 2){
                        TokenizableText rest = new TokenizableText(ss[1]);
                        List<Tokenizable> l_rest = new LinkedList<>();
                        l_rest.add(rest);
                        res.addAll(extractGlyphs(l_rest));
                    }
                } else {
                    res.add(t);
                }
            } else {
                res.add(t);
            }
        }

        return res;
    }

    private static List<Tokenizable> extractAbbreviations(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<Tokenizable>();

        for (Tokenizable t: tokens) {
            if (t instanceof TokenizableText) {
                String s = t.getText();
                if (s.contains("(")) {
                    String[] ss = s.split("\\(", 2);
                    if (!ss[0].isBlank()){
                        res.add(new TokenizableText(ss[0]));
                    }
                    ss = ss[1].split("\\)", 2);
                    res.add(new TokenTypeAbbreviation(ss[0]));
                    if (ss.length == 2){
                        TokenizableText rest = new TokenizableText(ss[1]);
                        List<Tokenizable> l_rest = new LinkedList<>();
                        l_rest.add(rest);
                        res.addAll(extractAbbreviations(l_rest));
                    }
                } else {
                    res.add(t);
                }
            } else {
                res.add(t);
            }
        }

        return res;
    }

    private static List<Tokenizable> extractOpeningAndClosingTags(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<Tokenizable>();

        for (Tokenizable t: tokens) {
            if (t instanceof TokenizableText) {
                String s = t.getText();
                if (s.contains("[")) {
                    String[] ss = s.split("\\[", 2);
                    if (!ss[0].isBlank()){
                        res.add(new TokenizableText(ss[0]));
                    }
                    ss = ss[1].split("\\]", 2);
                    if (ss[0].startsWith("/")){
                        res.add(new TokenTypeClosingTag(ss[0]));
                    } else {
                        res.add(new TokenTypeOpeningTag(ss[0]));
                    }
                    if (ss.length == 2){
                        TokenizableText rest = new TokenizableText(ss[1]);
                        List<Tokenizable> l_rest = new LinkedList<>();
                        l_rest.add(rest);
                        res.addAll(extractOpeningAndClosingTags(l_rest));
                    }
                } else {
                    res.add(t);
                }
            } else {
                res.add(t);
            }
        }

        return res;
    }

    private static List<Tokenizable> extractSingleLineComments(List<Tokenizable> tokens) {
        List<Tokenizable> res = new LinkedList<Tokenizable>();

        for (Tokenizable t: tokens){
            if (t instanceof TokenizableText){
                String s = t.getText();
                if (s.contains("#")){
                    String[] ss = s.split("#", 2);
                    if (!ss[0].isBlank()){
                        res.add(new TokenizableText(ss[0]));
                    }
                    ss = ss[1].split("\n", 2); // TODO Check, if that works
                    res.add(new TokenTypeSingleLineComment(ss[0]));
                    if (ss.length == 2){
                        TokenizableText rest = new TokenizableText(ss[1]);
                        List<Tokenizable> l_rest = new LinkedList<>();
                        l_rest.add(rest);
                        res.addAll(extractSingleLineComments(l_rest));
                    }
                } else {
                    res.add(t);
                }
            } else {
                res.add(t);
            }
        }

        return res;
    }

    private static List<Tokenizable> extractMultilineComments(String text) {
        List<Tokenizable> res = new LinkedList<Tokenizable>();

        text = text.replaceAll("/\\*", "/*££start_comment££");
        text = text.replaceAll("\\*/", "/*");

        String[] ss = text.split("/\\*");

        for (String s: ss){
            if (s.startsWith("££start_comment££")){
                s = s.replace("££start_comment££", "");
                res.add(new TokenTypeMultilineComment(s));
            } else {
                res.add(new TokenizableText(s));
            }
        }

        return res;
    }

    public static List<TranscriptionToken> tokenizeAbbreviation(TokenTypeAbbreviation t) {
        String[] ss = t.getText().split(";");
        LinkedList<TranscriptionToken> res = new LinkedList<>();
        for (String s: ss){
            res.add(new TranscriptionToken(s));
        }
        return res;
    }

    public static List<TranscriptionToken> tokenizeExpansion(String text) {
        List<Tokenizable> tokens = new LinkedList<>();
        tokens.add(new TokenizableText(text));
        tokens = extractGlyphs(tokens);
        tokens = extractTextFragments(tokens);

        ArrayList<TranscriptionToken> tokens_finished = castToTranscriptionTokens(tokens);

        return tokens_finished;
    }
//
//    public static List<TranscriptionToken> tokenizeAbbreviationMark(TranscriptionToken am) {
//        List<Tokenizable> l = extractGlyphs(Arrays.asList(am));
//        l = extractTextFragments(l);
//
//        ArrayList<TranscriptionToken> tokens_finished = castToTranscriptionTokens(l);
//
//        return tokens_finished;
//    }
}
