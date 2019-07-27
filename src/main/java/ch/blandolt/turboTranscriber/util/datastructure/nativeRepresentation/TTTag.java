package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeOpeningTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.LinkedList;

public class TTTag extends AbstractTranscriptionContainer{
    public TTTag(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public static TTTag convertTokenClassSpecific(TokenTypeOpeningTag t) {
        LinkedList<AbstractTranscriptionObject> c = new LinkedList<>();

        for (TranscriptionToken inner: t.getContent()){
            c.add(AbstractTranscriptionObject.convertToken(inner));
        }

        return new TTTag(c);
    }
}
