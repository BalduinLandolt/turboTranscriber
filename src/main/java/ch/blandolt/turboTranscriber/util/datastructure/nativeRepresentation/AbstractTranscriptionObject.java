package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.*;

public abstract class AbstractTranscriptionObject {
    public static AbstractTranscriptionObject convertToken(TranscriptionToken t) {
        if (t instanceof TokenTypeAbbreviation)
            return TTAbbreviation.convertTokenClassSpecific((TokenTypeAbbreviation) t);

        if (t instanceof TokenTypeGlyph)
            return TTGlyph.convertTokenClassSpecific((TokenTypeGlyph) t);

        if (t instanceof TokenTypeLinebreak)
            return new TTControlElement(TTControlElement.Type.LINEBREAK);

        if (t instanceof TokenTypeMultilineComment)
            return TTComment.convertTokenClassSpecific((TokenTypeMultilineComment) t);

        if (t instanceof TokenTypeSingleLineComment)
            return TTComment.convertTokenClassSpecific((TokenTypeSingleLineComment) t);

        if (t instanceof TokenTypeOpeningTag)
            return TTTag.convertTokenClassSpecific((TokenTypeOpeningTag) t);

        if (t instanceof TokenTypePunctuationCharacter)
            return TTPunctuationCharacter.convertTokenClassSpecific((TokenTypePunctuationCharacter) t);

        if (t instanceof TokenTypeTextFragment)
            return TTTextSegment.convertTokenClassSpecific((TokenTypeTextFragment) t);

        if (t instanceof TokenTypeWordborder)
            //return TokenTypeAbbreviation.convertToken((TokenTypeAbbreviation) t);
            return null; // TODO: handle word borders!

        return null;
    }
}
