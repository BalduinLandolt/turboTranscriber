package ch.blandolt.turboTranscriber.util.datastructure;

import ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation.AbstractTranscriptionObject;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.List;

public class XMLFactory {

    public static Document createXML(List<AbstractTranscriptionObject> data) {
        Element root = new Element("TEI", "http://www.tei-c.org/ns/1.0");
        Document res = new Document(root);

        // TODO implement adding data

        return res;
    }
}
