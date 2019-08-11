package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeClosingTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeOpeningTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.LinkedList;

public class TTTag extends AbstractTranscriptionContainer{
    public TTTag(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public static TTTag convertTokenClassSpecific(TokenTypeOpeningTag t) {
        LinkedList<AbstractTranscriptionObject> c = new LinkedList<>();

        // TODO: store other tag information (name, attributes, ...) too
        for (TranscriptionToken inner: t.getContent()){
            if (!(inner instanceof TokenTypeClosingTag))
                c.add(AbstractTranscriptionObject.convertToken(inner));
        }

        return new TTTag(c);
    }
}
