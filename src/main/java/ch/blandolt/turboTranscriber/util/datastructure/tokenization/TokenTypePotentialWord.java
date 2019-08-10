package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

import java.util.LinkedList;
import java.util.List;

public class TokenTypePotentialWord extends TranscriptionToken {

    private List<Tokenizable> contents = new LinkedList<>();

    public TokenTypePotentialWord(String txt) {
        super(txt);
    }

    public TokenTypePotentialWord(){
        this("");
    }

    public void add(Tokenizable t) {
        contents.add(t);
    }

    public List<Tokenizable> getContents() {
        return contents;
    }


    public boolean isLegit() {

        for (Tokenizable t: contents){
            if (t instanceof TokenTypeAbbreviation ||
                t instanceof TokenTypeGlyph ||
                t instanceof TokenTypeTextFragment)
                return true;
        }
        return false;
    }

    public TokenTypeLegitWord getLegitWordRepresentation() {
        if (!isLegit())
            return null;
        return new TokenTypeLegitWord(this);
    }
}
