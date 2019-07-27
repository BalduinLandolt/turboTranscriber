package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.*;

public class AbstractTranscriptionObject {
    public static AbstractTranscriptionObject convertToken(TranscriptionToken t) {
        if (t instanceof TokenTypeAbbreviation)
            return TTAbbreviation.convertToken((TokenTypeAbbreviation) t);

        if (t instanceof TokenTypeGlyph)
            return TTGlyph.convertToken((TokenTypeGlyph) t);

        if (t instanceof TokenTypeLinebreak)
            //return TokenTypeLinebreak.convertToken((TokenTypeLinebreak) t);
            return null; // TODO: handle line breaks

        if (t instanceof TokenTypeMultilineComment)
            return TTComment.convertToken((TokenTypeMultilineComment) t);

        if (t instanceof TokenTypeSingleLineComment)
            return TTComment.convertToken((TokenTypeSingleLineComment) t);

        if (t instanceof TokenTypeOpeningTag)
            return TTTag.convertToken((TokenTypeOpeningTag) t);

        if (t instanceof TokenTypePunctuationCharacter)
            return TTPunctuationCharacter.convertToken((TokenTypePunctuationCharacter) t);

        if (t instanceof TokenTypeTextFragment)
            return TTTextSegment.convertToken((TokenTypeTextFragment) t);

        if (t instanceof TokenTypeWordborder)
            //return TokenTypeAbbreviation.convertToken((TokenTypeAbbreviation) t);
            return null; // TODO: handle word borders!

        return null;
    }
}
