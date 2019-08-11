package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

public class TokenizableText implements Tokenizable {
    private String text;

    public TokenizableText(String txt){
        text = txt;
    }

    @Override
    public String getText() {
        return text;
    }
}
