package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeClosingTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeOpeningTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataFactory {
    public static List<AbstractTranscriptionObject> buildDatastructure(List<TranscriptionToken> tokens) {
        List<AbstractTranscriptionObject> res = new LinkedList<>();

        List<Object> tmp = new LinkedList<>();

        for (TranscriptionToken t: tokens){
            if (t instanceof TokenTypeClosingTag) {
                Log.log(t.getText() + " @ index: " + tokens.indexOf(t));
                TokenTypeOpeningTag opener = findOpeningTag(tokens, t);
                Log.log("Opened at: " + tokens.indexOf(opener) + ": " + opener.getText());
            }
        }

        // TODO: return something
        return null;
    }

    private static TokenTypeOpeningTag findOpeningTag(List<TranscriptionToken> tokens, TranscriptionToken t) {
        String name = t.getText().replace("/", "");
        List<TranscriptionToken> sublist = tokens.subList(0, tokens.indexOf(t));
        sublist = new LinkedList<>(sublist);
        Collections.reverse(sublist); // FIXME: seems to manipulate the original list.
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
