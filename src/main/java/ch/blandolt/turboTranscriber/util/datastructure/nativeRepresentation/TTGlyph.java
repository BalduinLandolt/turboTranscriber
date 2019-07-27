package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeGlyph;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypePunctuationCharacter;

public class TTGlyph extends AbstractAtomarTranscriptionSegment {
    public TTGlyph(TTPlainTextSegment content) {
        super(content);
        // TODO: resolve what they signify
    }

    public static TTGlyph convertTokenClassSpecific(TokenTypeGlyph t) {
        return new TTGlyph(new TTPlainTextSegment(t.getText()));
    }

    public static boolean isGlyph(TokenTypePunctuationCharacter t) {
        return t.getText().matches("\\{.*\\}");
    }
}
