package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

public class TokenTypeOpeningTag extends TranscriptionToken {
    private String tagName;
    private boolean isAnchor = false;

    public TokenTypeClosingTag getClosingTag() {
        return closingTag;
    }

    private TokenTypeClosingTag closingTag;

    public TokenTypeOpeningTag(String txt) {
        super(txt);
        String[] ss = txt.split("=");
        tagName = ss[0];
    }

    public String getTagName(){
        return tagName;
    }

    public void addClosingTag(TokenTypeClosingTag t) {
        closingTag = t;
    }

    public void setIsAnchor(boolean b) {
        isAnchor = b;
    }

    public boolean isAnchor() {
        return isAnchor;
    }
}
