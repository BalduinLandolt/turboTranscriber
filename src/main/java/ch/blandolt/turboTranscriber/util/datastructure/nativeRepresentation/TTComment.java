package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeMultilineComment;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeSingleLineComment;

public class TTComment extends AbstractAtomarTranscriptionSegment {
    private boolean isMultiline;

    public TTComment(TTPlainTextSegment content, boolean isMultiline) {
        super(content);
        this.isMultiline = isMultiline;
    }

    public static TTComment convertTokenClassSpecific(TokenTypeMultilineComment t) {
        return new TTComment(new TTPlainTextSegment(t.getText()), true);
    }

    public static TTComment convertTokenClassSpecific(TokenTypeSingleLineComment t) {
        return new TTComment(new TTPlainTextSegment(t.getText()), false);
    }
}
