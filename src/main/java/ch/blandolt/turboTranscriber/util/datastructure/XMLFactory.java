package ch.blandolt.turboTranscriber.util.datastructure;

import ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation.AbstractTranscriptionObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.ProcessingInstruction;

import java.util.List;

public class XMLFactory {

    public static Document createXML(List<AbstractTranscriptionObject> data) {
        Element root = new Element("TEI", "http://www.tei-c.org/ns/1.0");
        Document res = new Document(root);
        ProcessingInstruction relaxng = new ProcessingInstruction("xml-model",
                "href='http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_all.rng' " +
                        "type='application/xml' schematypens='http://relaxng.org/ns/structure/1.0'");
        res.addContent(0, relaxng);
        ProcessingInstruction schematron = new ProcessingInstruction("xml-model",
                "href='http://www.tei-c.org/release/xml/tei/custom/schema/relaxng/tei_all.rng' " +
                        "type='application/xml' schematypens='http://purl.oclc.org/dsdl/schematron'");
        res.addContent(1, schematron);
        ProcessingInstruction stylesheet = new ProcessingInstruction("xml-stylesheet",
                "href='style.css' type='text/css'");
        res.addContent(2, stylesheet);

        // TODO implement adding data

        return res;
    }
}
