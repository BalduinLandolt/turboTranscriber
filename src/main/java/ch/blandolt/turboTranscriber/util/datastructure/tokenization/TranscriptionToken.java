package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

import java.util.ArrayList;
import java.util.List;

public class TranscriptionToken implements Tokenizable {
    String text;
    public TranscriptionToken(String txt){
        text = txt;
    }

    public String getText() {
        return text;
    }

    public String toString(){
        //String s = super.toString();
        //return s + ": " + text;
        return this.getClass().getSimpleName() + ": " + text;
    }

	public List<TranscriptionToken> getFlatList() {
        ArrayList<TranscriptionToken> res = new ArrayList<>();
        res.add(this);
		return res;
	}
}
