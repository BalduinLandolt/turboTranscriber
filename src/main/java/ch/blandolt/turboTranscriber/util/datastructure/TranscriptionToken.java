package ch.blandolt.turboTranscriber.util.datastructure;

public class TranscriptionToken implements Tokenizable {
    private String text;
    public TranscriptionToken(String txt){
        text = txt;
    }

    public String getText() {
        return text;
    }
}
