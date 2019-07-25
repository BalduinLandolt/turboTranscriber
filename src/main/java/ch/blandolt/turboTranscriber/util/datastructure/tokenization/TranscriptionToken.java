package ch.blandolt.turboTranscriber.util.datastructure.tokenization;

public class TranscriptionToken implements Tokenizable {
    private String text;
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
}
