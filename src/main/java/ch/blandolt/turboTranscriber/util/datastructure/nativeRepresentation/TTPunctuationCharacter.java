package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import java.util.LinkedList;

public class TTPunctuationCharacter extends AbstractTranscriptionContainer{
    public TTPunctuationCharacter(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }
    // TODO: needs to be either a character of one string of one glyph
    //      - overload constructor


}
