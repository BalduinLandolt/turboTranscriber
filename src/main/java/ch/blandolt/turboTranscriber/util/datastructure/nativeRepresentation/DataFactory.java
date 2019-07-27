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

        prepareTags(tokens);

        // TODO: return something
        return null;
    }

    private static void prepareTags(List<TranscriptionToken> tokens) {
        for (TranscriptionToken t: tokens){
            if (t instanceof TokenTypeClosingTag) {
                TokenTypeOpeningTag opener = findOpeningTag(tokens, t);
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