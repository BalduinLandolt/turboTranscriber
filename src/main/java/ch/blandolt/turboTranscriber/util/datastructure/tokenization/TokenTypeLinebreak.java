package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

public class TokenTypeLinebreak extends TranscriptionToken {
    private boolean isProtected = false;
    public TokenTypeLinebreak(String txt, boolean isProtected) {
        super(txt);
        this.isProtected = isProtected;
    }
}
