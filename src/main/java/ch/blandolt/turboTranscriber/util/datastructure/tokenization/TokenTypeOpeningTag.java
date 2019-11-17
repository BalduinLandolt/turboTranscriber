package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

import java.util.LinkedList;

public class TokenTypeOpeningTag extends TranscriptionToken {
    private String tagName;
    private String raw;
    private boolean isAnchor = false;
    private LinkedList<TranscriptionToken> content = new LinkedList<>();
    private TokenTypeClosingTag closingTag;

    public TokenTypeOpeningTag(String txt) {
        super(txt);
        String[] ss = txt.split("=");
        tagName = ss[0];
    }

    public TokenTypeClosingTag getClosingTag() {
        return closingTag;
    }

    public String getTagName(){
        return tagName;
    }

    public void addClosingTag(TokenTypeClosingTag t) {
        closingTag = t;
        t.addOpeningTag(this);
    }

    public void setIsAnchor(boolean b) {
        isAnchor = b;
    }

    public boolean isAnchor() {
        return isAnchor;
    }

    public LinkedList<TranscriptionToken> getContent() {
        return content;
    }

    public void setContent(LinkedList<TranscriptionToken> content) {
        this.content = content;
    }

    public String getRawRepresentation() {
        return getText();
    }
}
