package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeClosingTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeOpeningTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.HashMap;
import java.util.LinkedList;

public class TTTag extends AbstractTranscriptionContainer{
    private String rawRepresentation;
    private String tagName;
    private HashMap<String, String> attributes = new HashMap();

    public TTTag(LinkedList<AbstractTranscriptionObject> content, String rawRepresentation) {
        super(content);
        this.rawRepresentation = rawRepresentation;
        setUpData(rawRepresentation);
    }

    private void setUpData(String rawRepresentation) {
        String[] ss = rawRepresentation.split("=");
        if (ss.length == 1){ // no attributes
            tagName = ss[0];
        } else if (ss.length == 2) { // exactly one attribute
            tagName = ss[0];
            attributes.put("XXXXX", ss[1]);
            // TODO: look up what the key for value is
        } else {
            // TODO handle multiple
        }
    }

    public static TTTag convertTokenClassSpecific(TokenTypeOpeningTag t) {
        LinkedList<AbstractTranscriptionObject> c = new LinkedList<>();

        for (TranscriptionToken inner: t.getContent()){
            if (!(inner instanceof TokenTypeClosingTag))
                c.add(AbstractTranscriptionObject.convertToken(inner));
        }

        return new TTTag(c, t.getRawRepresentation());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": '" + rawRepresentation + "'" + getContent().toString();
    }

    public String getRawRepresentation() {
        return rawRepresentation;
    }

    public String getTagName() {
        return tagName;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }
}
