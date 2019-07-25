package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import java.util.LinkedList;

public class AbstractTranscriptionContainer extends AbstractTranscriptionObject {
    private LinkedList<AbstractTranscriptionObject> content;

    public AbstractTranscriptionContainer(LinkedList<AbstractTranscriptionObject> content) {
        this.content = content;
    }
}
