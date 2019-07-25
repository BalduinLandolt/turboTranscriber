package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

public class TTTextSegment extends AbstractAtomarTranscriptionSegment {
    public TTTextSegment(TTPlainTextSegment content) {
        super(content);
    }

    public TTTextSegment(String content) {
        super(new TTPlainTextSegment(content));
    }
}
