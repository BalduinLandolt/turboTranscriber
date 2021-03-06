package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeClosingTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeMultilineComment;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeOpeningTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataFactory {

    public static List<AbstractTranscriptionObject> buildDatastructure(List<TranscriptionToken> tokens) {
        tokens = prepareTags(tokens);
        LinkedList<AbstractTranscriptionObject> data = convertTokens(tokens);

        return data;
    }

    private static LinkedList<AbstractTranscriptionObject> convertTokens(List<TranscriptionToken> tokens) {
        LinkedList<AbstractTranscriptionObject> res = new LinkedList<>();

        for (TranscriptionToken t: tokens){
            AbstractTranscriptionObject o = AbstractTranscriptionObject.convertToken(t);
            res.add(o);
        }

        return res;
    }

    private static List<TranscriptionToken> prepareTags(List<TranscriptionToken> tokens) {
        for (TranscriptionToken t: tokens){
            if (t instanceof TokenTypeClosingTag) {
                TokenTypeOpeningTag opener = findOpeningTag(tokens, t);
                if (opener == null) {
                    TranscriptionToken t_new =
                            new TokenTypeMultilineComment("!!! Fixme: Can't find opening tag for closing tag '[" +
                            t.getText() + "]' !!! (Auto-generated comment by TurboTranscriber.)");
                    tokens.set(tokens.indexOf(t), t_new);
                    continue;
                }
                opener.addClosingTag((TokenTypeClosingTag) t);
            }
        }

        // TODO: handle special cases from (future) list of special cases
        //      can it be that a tag is not an anchor, but is closed by the next same opening tag?
        for (TranscriptionToken t: tokens){
            if (t instanceof TokenTypeOpeningTag){
                TokenTypeOpeningTag opener = (TokenTypeOpeningTag) t;
                if (null == opener.getClosingTag()){
                    opener.setIsAnchor(true);
                }
            }
        }

        return makeTagTreeShape(tokens);
    }

    private static List<TranscriptionToken> makeTagTreeShape(List<TranscriptionToken> tokens) {
        while (hasClosingTags(tokens)){
            TokenTypeClosingTag closer = getFirstClosingTag(tokens);
            assert closer != null; // Should never happen while hasClosingTags()
            tokens = putContentInTags(closer, tokens);
        }
        return tokens;
    }

    private static TokenTypeClosingTag getFirstClosingTag(List<TranscriptionToken> tokens) {
        for (TranscriptionToken t: tokens){
            if (t instanceof TokenTypeClosingTag)
                return (TokenTypeClosingTag)t;
        }
        return null;
    }

    private static boolean hasClosingTags(List<TranscriptionToken> tokens) {
        for (TranscriptionToken t: tokens){
            if (t instanceof TokenTypeClosingTag)
                return true;
        }
        return false;
    }

    private static List<TranscriptionToken> putContentInTags(TokenTypeClosingTag closer, List<TranscriptionToken> tokens) {
        TokenTypeOpeningTag opener = closer.getOpeningTag();
        int i_opener = tokens.indexOf(opener);
        int i_closer = tokens.indexOf(closer);

        ArrayList<TranscriptionToken> res = new ArrayList<>();

        if (i_opener > 0){
            res.addAll(tokens.subList(0, i_opener));
        }

        res.add(opener);

        LinkedList<TranscriptionToken> contents = new LinkedList<>(tokens.subList(i_opener + 1, i_closer));
        opener.setContent(contents);

        if (i_closer < tokens.size()-1){
            res.addAll(tokens.subList(i_closer+1, tokens.size()));
        }

        return res;
    }

    private static TokenTypeOpeningTag findOpeningTag(List<TranscriptionToken> tokens, TranscriptionToken t) {
        String name = t.getText().replace("/", "");
        List<TranscriptionToken> sublist = tokens.subList(0, tokens.indexOf(t));
        sublist = new LinkedList<>(sublist);
        Collections.reverse(sublist); // FIXME: seems to manipulate the original list. (or does it?)
        for (TranscriptionToken sub: sublist){
            if (sub instanceof TokenTypeOpeningTag){
                TokenTypeOpeningTag tag = (TokenTypeOpeningTag) sub;
                if (tag.getTagName().equals(name)){
                    return tag;
                }
            }
        }

        return null;
    }
}
