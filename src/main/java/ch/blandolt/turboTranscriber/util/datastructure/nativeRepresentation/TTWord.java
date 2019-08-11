package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;


import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeLegitWord;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TTWord extends AbstractTranscriptionContainer{
    public TTWord(LinkedList<AbstractTranscriptionObject> content) {
        super(content);
    }

    public TTWord(List<AbstractTranscriptionObject> content) {
        this(new LinkedList<>(content));
    }

    public TTWord(AbstractTranscriptionObject content){
        this(new LinkedList<AbstractTranscriptionObject>(Arrays.asList(content)));
    }

    public static TTWord convertTokenClassSpecific(TokenTypeLegitWord t) {
        return new TTWord(DataFactory.buildDatastructure(t.getContents()));
    }
}
