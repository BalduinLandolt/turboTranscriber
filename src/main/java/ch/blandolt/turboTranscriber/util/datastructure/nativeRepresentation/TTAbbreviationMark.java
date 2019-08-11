package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.Tokenizer;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.LinkedList;
import java.util.List;

public class TTAbbreviationMark extends AbstractTranscriptionContainer {
    public TTAbbreviationMark(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public static LinkedList<AbstractTranscriptionObject> convertTokenClassSpecific(TranscriptionToken am) {
        List<TranscriptionToken> tokens = Tokenizer.tokenizeAbbreviationMark(am);
        LinkedList<AbstractTranscriptionObject> res = new LinkedList<>();
        for (TranscriptionToken t: tokens){
            res.add(AbstractTranscriptionObject.convertToken(t));
        }
        return res;
    }

    public LinkedList<AbstractTranscriptionObject> getContent(){
        return super.getContent();
    }
}
