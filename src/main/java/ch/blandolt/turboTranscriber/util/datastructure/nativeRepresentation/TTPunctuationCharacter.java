package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypePunctuationCharacter;

import java.util.LinkedList;

public class TTPunctuationCharacter extends AbstractTranscriptionContainer{
    public TTPunctuationCharacter(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public static TTPunctuationCharacter convertTokenClassSpecific(TokenTypePunctuationCharacter t) {
        LinkedList<AbstractTranscriptionObject> l = new LinkedList<>();
        if (TTGlyph.isGlyph(t)){
            l.add(new TTGlyph(new TTPlainTextSegment(t.getText())));
        } else {
            l.add(new TTNonGlyphPunctuation(new TTPlainTextSegment(t.getText())));
        }
        return new TTPunctuationCharacter(l);
    }

}
