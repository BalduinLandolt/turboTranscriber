package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

public class TokenTypeLegitWord extends TranscriptionToken {
    TokenTypePotentialWord word = null;
    public TokenTypeLegitWord(String txt) {
        super(txt);
    }
    public TokenTypeLegitWord(TokenTypePotentialWord w){
        this("");
        if (w.isLegit()){
            word = w;
        }
    }
}
