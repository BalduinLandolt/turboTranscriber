package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeTextFragment;

public class TTTextSegment extends AbstractAtomarTranscriptionSegment {
    public TTTextSegment(TTPlainTextSegment content) {
        super(content);
    }

    public TTTextSegment(String content) {
        super(new TTPlainTextSegment(content));
    }

    public static TTTextSegment convertTokenClassSpecific(TokenTypeTextFragment t) {
        return new TTTextSegment(t.getText());
    }
}
