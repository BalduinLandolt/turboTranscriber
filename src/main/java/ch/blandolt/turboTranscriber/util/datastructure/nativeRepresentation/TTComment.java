package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

public class TTComment extends AbstractAtomarTranscriptionSegment {
    private boolean isMultiline;

    public TTComment(TTPlainTextSegment content, boolean isMultiline) {
        super(content);
        this.isMultiline = isMultiline;
    }
}
