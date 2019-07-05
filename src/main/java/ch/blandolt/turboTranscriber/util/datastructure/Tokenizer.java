package ch.blandolt.turboTranscriber.util.datastructure;

import ch.blandolt.turboTranscriber.util.Log;

import java.util.LinkedList;
import java.util.List;

public class Tokenizer {
    public static List<TranscriptionToken> tokenize(String text) {

        Log.log("Tokenizing the following:");
        Log.log(text);

        List<Tokenizable> tokens = Tokenizer.extractMultilineComments(text);



        return null;
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
