package ch.blandolt.turboTranscriber.util.datastructure;

import ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation.*;
import org.jdom2.*;

import java.util.List;

public class XMLFactory {
    private static Namespace ns_TEI = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

    // TODO: implement menota

    public static Document createTEIXML(List<AbstractTranscriptionObject> data) {
        Element root = new Element("TEI", ns_TEI);
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

        Element header = makeTEIHeader();
        root.addContent(header);

        Element text = makeText(data);
        root.addContent(text);

        return res;
    }

    private static Element makeText(List<AbstractTranscriptionObject> data) {
        Element text = new Element("text", ns_TEI);

        Element body = new Element("body", ns_TEI);
        text.addContent(body);

        for (AbstractTranscriptionObject tr: data){
            Content content = XMLFactory.generateXMLFromTranscriptionObject(tr);
            if (null != content)
                body.addContent(content);
        }


        // TODO implement adding data

        return text;
    }

    private static Content generateXMLFromTranscriptionObject(AbstractTranscriptionObject tr) {
        if (tr instanceof TTAbbreviation){
            TTAbbreviation expan = (TTAbbreviation)tr;
            Element e = new Element("expan", ns_TEI);
            e.addContent(XMLFactory.generateXMLFromTranscriptionObject(expan.getExpansion()));
            if (expan.hasInfix())
                e.addContent(XMLFactory.generateXMLFromTranscriptionObject(expan.getInfix()));
            e.addContent(XMLFactory.generateXMLFromTranscriptionObject(expan.getAbbreviationMark()));
            return e;
        } else if (tr instanceof TTAbbreviationMark){
            TTAbbreviationMark am = (TTAbbreviationMark)tr;
            Element e = new Element("am", ns_TEI);
            for (AbstractTranscriptionObject t: am.getContent()){
                Content content = XMLFactory.generateXMLFromTranscriptionObject(t);
                if (null != content)
                    e.addContent(content);
            }
            return e;
        } else if (tr instanceof TTAnchor){ // TODO: existing?
            //
        } else if (tr instanceof TTComment){
            TTComment c = (TTComment) tr;
            Comment comment = new Comment(c.toString());
            return comment;
        } else if (tr instanceof TTControlElement){ // TODO: what? ignore? comment?
            //
        } else if (tr instanceof TTExpansion){
            TTExpansion ex = (TTExpansion)tr;
            Element e = new Element("ex", ns_TEI);
            e.addContent(new Text(ex.toString()));
            return e;
        } else if (tr instanceof TTGlyph){
            //
        } else if (tr instanceof TTNonGlyphPunctuation){
            //
        } else if (tr instanceof TTPunctuationCharacter){
            //
        } else if (tr instanceof TTTag){
            //
        } else if (tr instanceof TTTextSegment){
            //
        } else if (tr instanceof TTWord){
            //
        }
        return null; // should never happen
    }

    private static Element makeTEIHeader() {
        Element teiHeader = new Element("teiHeader", ns_TEI);

        Element fileDesc = new Element("fileDesc", ns_TEI);
        teiHeader.addContent(fileDesc);

        Element encodingDesc = new Element("encodingDesc", ns_TEI);
        teiHeader.addContent(encodingDesc);

        // TODO: add more elements, to get valid tei header

        // TODO: how can metadata be implemented in raw?

        return teiHeader;
    }
}
