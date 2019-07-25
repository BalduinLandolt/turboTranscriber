package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;


import java.util.Arrays;
import java.util.LinkedList;

public class TTWord extends AbstractTranscriptionContainer{
    public TTWord(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public TTWord(AbstractTranscriptionObject content){
        this(new LinkedList<AbstractTranscriptionObject>(Arrays.asList(content)));
    }
}
