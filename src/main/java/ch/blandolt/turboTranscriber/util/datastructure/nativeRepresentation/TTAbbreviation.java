package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeAbbreviation;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.Tokenizer;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TTAbbreviation extends AbstractTranscriptionContainer {
    public TTAbbreviation(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public TTAbbreviation(TranscriptionToken ex, TranscriptionToken infix, TranscriptionToken am) {
        // TODO: fix. logic of what is atomar and what not, seems messed up
        this(new LinkedList<AbstractTranscriptionObject>(Arrays.asList(
                new TTExpansion(TTExpansion.convertTokenClassSpecific(ex)),
                new TTTextSegment(new TTPlainTextSegment(infix.getText())),
                new TTAbbreviationMark(TTAbbreviationMark.convertTokenClassSpecific(am)))));
    }

    public TTAbbreviation(TranscriptionToken ex, TranscriptionToken am) {
        this(ex, new TranscriptionToken(""), am);
    }

    public static TTAbbreviation convertTokenClassSpecific(TokenTypeAbbreviation t) {
        List<TranscriptionToken> contents = Tokenizer.tokenizeAbbreviation(t);
        
        if (contents.isEmpty() || contents.size() > 3){
            Log.log("Error! Unexpected abbreviation size!");
            return null;
        }
        
        TTAbbreviation abbreviation = null;
        
        if (contents.size() == 1){
            // TODO: check special cases!
            //      - currently assuming one-argument-abbreviations to be same-same-abbreviations.
            abbreviation = new TTAbbreviation(contents.get(0), contents.get(0));
        } else if (contents.size() == 2){
            abbreviation = new TTAbbreviation(contents.get(0), contents.get(1));
        } else if (contents.size() == 3){
            abbreviation = new TTAbbreviation(contents.get(0), contents.get(1), contents.get(2));
        }
        
        return abbreviation;
    }

    public TTExpansion getExpansion(){
        return (TTExpansion)getContent().get(0);
    }

    public TTTextSegment getInfix(){
        return (TTTextSegment)getContent().get(1);
    }

    public TTAbbreviationMark getAbbreviationMark(){
        return (TTAbbreviationMark)getContent().get(2);
    }

    public boolean hasInfix() {
        TTTextSegment t = getInfix();
        return t.toString().isBlank();
    }
    // TODO: am, potential middle, ex
    //      - overload constructor
}
