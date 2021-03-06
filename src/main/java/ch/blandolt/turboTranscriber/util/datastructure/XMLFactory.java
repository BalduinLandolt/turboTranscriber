package ch.blandolt.turboTranscriber.util.datastructure;

import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation.*;
import org.jdom2.*;

import java.util.List;
import java.util.Map;

public class XMLFactory {
    private static Namespace ns_TEI = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

    // TODO: ensure that tags with multiple arguments work properly


    public static Document createTEIXML(List<AbstractTranscriptionObject> data) {
        Element root = new Element("TEI", ns_TEI);
        Document res = new Document(root);
        ProcessingInstruction relaxng = new ProcessingInstruction("xml-model",
                "href='../schema/tei-onmm.rng' " +
                        "type='application/xml' schematypens='http://relaxng.org/ns/structure/1.0'");
        res.addContent(0, relaxng);
        ProcessingInstruction restrict = new ProcessingInstruction("xml-model",
                "href='../schema/restrict.xsd' " +
                        "type='application/xml' schematypens='http://www.w3.org/2001/XMLSchema'");
        res.addContent(1, restrict);
        ProcessingInstruction stylesheet = new ProcessingInstruction("xml-stylesheet",
                "href='style.css' type='text/css'");
        res.addContent(2, stylesheet);

        Element header = makeTEIHeader();
        root.addContent(header);

        Element text = makeText(data);
        root.addContent(text);

        Log.log("Generated TEI XML");

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

        return text;
    }

    private static Content generateXMLFromTranscriptionObject(AbstractTranscriptionObject tr) {
        if (tr instanceof TTAbbreviation){
            TTAbbreviation expan = (TTAbbreviation)tr;
            return generateXMLforAbbreviation(expan);
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
            for (AbstractTranscriptionObject o: ex.getContent()){
                e.addContent(XMLFactory.generateXMLFromTranscriptionObject(o));
            }
            return e;
        } else if (tr instanceof TTGlyph){
            TTGlyph g = (TTGlyph)tr;
            Element e = new Element("g", ns_TEI);
            e.setAttribute("ref", "#"+g.toString());
            return e;
        } else if (tr instanceof TTNonGlyphPunctuation){
            TTNonGlyphPunctuation p = (TTNonGlyphPunctuation)tr;
            Text t = new Text(p.toString());
            return t;
        } else if (tr instanceof TTPunctuationCharacter){
            TTPunctuationCharacter p = (TTPunctuationCharacter)tr;
            Element e = new Element("pc", ns_TEI);
            e.addContent(XMLFactory.generateXMLFromTranscriptionObject(p.getContent().getFirst()));
            return e;
        } else if (tr instanceof TTTag){
            TTTag t = (TTTag)tr;
            return getXMLforTag(t);
        } else if (tr instanceof TTTextSegment){
            TTTextSegment p = (TTTextSegment)tr;
            Text t = new Text(p.toString().replace('_', ' '));
            return t;
        } else if (tr instanceof TTWord){
            TTWord w = (TTWord)tr;
            Element e = new Element("w", ns_TEI);
            for (AbstractTranscriptionObject t: w.getContent()){
                Content content = XMLFactory.generateXMLFromTranscriptionObject(t);
                if (null != content)
                    e.addContent(content);
            }
            return e;
        }
        return null; // should never happen
    }

    private static Content getXMLforTag(TTTag t) {
        // TODO: find solution for tags that should not be anchors (e.g. [p], [div], ...) [?]

        if (t == null || t.getTagName().isEmpty())
            return null;
        Element e = null;
        try {
            e = new Element(t.getTagName(), ns_TEI);
            for (Map.Entry<String, String> attribute: t.getAttributes().entrySet()){
                e.setAttribute(attribute.getKey(),attribute.getValue());
            }
            for (AbstractTranscriptionObject o: t.getContent()){
                Content c = XMLFactory.generateXMLFromTranscriptionObject(o);
                if (c != null)
                    e.addContent(c);
            }
        } catch (Exception ex) {
            return null;
        }
        return e;
    }

    private static Content generateXMLforAbbreviation(TTAbbreviation expan) {
        Element choice = new Element("choice", ns_TEI);
        Element e = new Element("expan", ns_TEI);
        Element a = new Element("abbr", ns_TEI);
        choice.addContent(a);
        choice.addContent(e);

        if (expan.hasInfix())
            a.addContent(XMLFactory.generateXMLFromTranscriptionObject(expan.getInfix()));
        a.addContent(XMLFactory.generateXMLFromTranscriptionObject(expan.getAbbreviationMark()));

        e.addContent(XMLFactory.generateXMLFromTranscriptionObject(expan.getExpansion()));
        if (expan.hasInfix())
            e.addContent(XMLFactory.generateXMLFromTranscriptionObject(expan.getInfix()));

        return choice;
    }

    private static Element makeTEIHeader() {
        Element teiHeader = new Element("teiHeader", ns_TEI);

        Element fileDesc = new Element("fileDesc", ns_TEI);
        teiHeader.addContent(fileDesc);
        Element titleStmt = new Element("titleStmt", ns_TEI);
        fileDesc.addContent(titleStmt);
        Element title = new Element("title", ns_TEI);
        titleStmt.addContent(title);
        Text txt = new Text("[Title]");
        title.addContent(txt);
        // TODO: add Title information
        Element publicationStmt = new Element("publicationStmt", ns_TEI);
        fileDesc.addContent(publicationStmt);
        Element p = new Element("p", ns_TEI);
        publicationStmt.addContent(p);
        txt = new Text("[Publication Statement]");
        p.addContent(txt);
        // TODO: add publ. information
        Element sourceDesc = new Element("sourceDesc", ns_TEI);
        fileDesc.addContent(sourceDesc);
        p = new Element("p", ns_TEI);
        sourceDesc.addContent(p);
        txt = new Text("[Publication Statement]");
        p.addContent(txt);
        // TODO: add publ. information


        Element encodingDesc = new Element("encodingDesc", ns_TEI);
        teiHeader.addContent(encodingDesc);
        p = new Element("p", ns_TEI);
        encodingDesc.addContent(p);
        txt = new Text("[source description]");
        p.addContent(txt);
        // TODO: add encoding information

        // TODO: how can metadata be implemented in raw?

        return teiHeader;
    }
}
