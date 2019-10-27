package ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation;

import ch.blandolt.turboTranscriber.util.datastructure.XMLLookup;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeClosingTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TokenTypeOpeningTag;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
        /*
        Rules for Tag:
        ==============

        Possible cases:
        ---------------

        regulars:

         - name only
         [p]...[/p]

         - name plus one attribute set
         [lb:n=1]

         - name plus multiple attribute sets
         [name:type=person:rend=hi]


         special cases that need look-up:

         - name plus one value
         [lb=1]
         // TODO: should logically be [lb:1] -> make synonymous?

         - only values
         [miracle=1]

         - implicit keys for values
         [name:person:hi]


         */

        //special case (type [lb=1])
        if (rawRepresentation.contains("=") && !rawRepresentation.contains(":")) // contains = but no :
            rawRepresentation = rawRepresentation.replace("=", ":");

        List<String> parts = new LinkedList<>(Arrays.asList(rawRepresentation.split(":")));
        tagName = parts.remove(0);

        if (XMLLookup.isSpecialCase__valueForName(tagName)){
            parts.add(0, tagName);
            tagName = XMLLookup.getSpecialCase__nameByValue(tagName);
        }

        for (String part: parts){
            if (part.contains("=")){
                String[] pp = part.split("=");
                attributes.put(pp[0], pp[1]);
            } else {
                attributes.put(part, part);
                // TODO: lookup here already?
            }
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
