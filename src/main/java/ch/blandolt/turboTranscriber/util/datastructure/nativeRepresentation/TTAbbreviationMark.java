package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.LinkedList;

public class TTAbbreviationMark extends AbstractTranscriptionContainer {
    public TTAbbreviationMark(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public static LinkedList<AbstractTranscriptionObject> convertTokenClassSpecific(TranscriptionToken am) {
    }
    // TODO: can be glyphe or text segment. Add constructors accordingly.
}
