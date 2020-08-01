package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

import java.util.List;

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

    public List<TranscriptionToken> getContents() {
        return Tokenizer.castToTranscriptionTokens(word.getContents());
    }

    @Override
    public List<TranscriptionToken> getFlatList() {
        List<TranscriptionToken> res = super.getFlatList();
        res.addAll(getContents());
        return res;
    }
}
