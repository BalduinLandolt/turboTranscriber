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
        // TODO: how are anchors handled?

        if (t instanceof TokenTypePunctuationCharacter)
            return TTPunctuationCharacter.convertTokenClassSpecific((TokenTypePunctuationCharacter) t);

        if (t instanceof TokenTypeTextFragment)
            return TTTextSegment.convertTokenClassSpecific((TokenTypeTextFragment) t);

        if (t instanceof TokenTypeLegitWord)
            return TTWord.convertTokenClassSpecific((TokenTypeLegitWord) t);

        return null;
    }
}
