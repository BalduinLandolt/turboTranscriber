package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

public class TTPlainTextSegment {
    public String getTextContent() {
        return textContent;
    }

    private String textContent;

    public TTPlainTextSegment(String content){
        textContent = content;
    }
}
