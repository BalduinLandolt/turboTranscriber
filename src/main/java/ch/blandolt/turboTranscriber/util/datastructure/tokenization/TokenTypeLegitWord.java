package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

import ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation.AbstractTranscriptionObject;

import java.util.LinkedList;
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
}
