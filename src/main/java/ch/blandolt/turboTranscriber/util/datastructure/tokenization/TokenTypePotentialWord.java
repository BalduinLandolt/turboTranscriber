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
        // TODO: check, if this is an actual word.
        return true;
    }
}
