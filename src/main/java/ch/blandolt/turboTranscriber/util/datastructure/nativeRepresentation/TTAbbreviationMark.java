package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import java.util.LinkedList;

public class TTAbbreviationMark extends AbstractTranscriptionContainer {
    public TTAbbreviationMark(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }
    // TODO: can be glyphe or text segment. Add constructors accordingly.
}
