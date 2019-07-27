package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

public class TTControlElement extends AbstractAtomarTranscriptionSegment {
    public enum Type { // TODO: will there be more control elements?
        LINEBREAK
    }

    private Type myType;

    public TTControlElement(Type type) {
        super(new TTPlainTextSegment(""));
        myType = type;
    }
}
