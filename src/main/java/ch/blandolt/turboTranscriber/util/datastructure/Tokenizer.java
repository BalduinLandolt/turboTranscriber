package ch.blandolt.turboTranscriber.util.datastructure;

import ch.blandolt.turboTranscriber.util.Log;

import java.util.LinkedList;
import java.util.List;

public class Tokenizer {
    public static List<TranscriptionToken> tokenize(String text) {

        Log.log("Tokenizing the following:");
        Log.log(text);

        List<Tokenizable> tokens = Tokenizer.extractMultilineComments(text);
        tokens = Tokenizer.extractSingleLineComments(tokens);


        Log.log(tokens);
        return null;
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
}
