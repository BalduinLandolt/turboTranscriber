package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeOpeningTag;

import java.util.LinkedList;

public class TTTag extends AbstractTranscriptionContainer{
    public TTTag(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public static AbstractTranscriptionObject convertTokenClassSpecific(TokenTypeOpeningTag t) {
    }
}
