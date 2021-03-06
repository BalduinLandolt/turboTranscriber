package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

public class TokenTypeClosingTag extends TranscriptionToken {
    private TokenTypeOpeningTag opener;
    TokenTypeClosingTag(String txt) {
        super(txt);
    }

    void addOpeningTag(TokenTypeOpeningTag opener) {
        this.opener = opener;
    }

    public TokenTypeOpeningTag getOpeningTag() {
        return opener;
    }
}
