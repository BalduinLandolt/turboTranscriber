package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.Tokenizer;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.LinkedList;
import java.util.List;

public class TTExpansion extends AbstractTranscriptionContainer {
    public TTExpansion(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public static LinkedList<AbstractTranscriptionObject> convertTokenClassSpecific(TranscriptionToken ex) {
        return new LinkedList<>(DataFactory.buildDatastructure(Tokenizer.tokenizeExpansion(ex.getText())));
    }
}
