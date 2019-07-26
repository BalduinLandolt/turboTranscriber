package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

public class TokenTypeOpeningTag extends TranscriptionToken {
    private String tagName;

    public TokenTypeOpeningTag(String txt) {
        super(txt);
        String[] ss = txt.split("=");
        tagName = ss[0];
    }

    public String getTagName(){
        return tagName;
    }
}
