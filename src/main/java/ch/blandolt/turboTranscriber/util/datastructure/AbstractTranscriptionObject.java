/**
 * 
 */
package ch.blandolt.turboTranscriber.util.datastructure;

import java.util.LinkedList;

import org.jdom2.Content;
import org.jdom2.Namespace;

/**
 * @author Balduin Landolt
 *
 */
public abstract class AbstractTranscriptionObject implements Cloneable {
	private LinkedList<Content> xmlRepresentation;
	private String rawRepresentation;
	private String textualRepresentation;
	
	
	public abstract String generateTextualRepresentation(String rawRepresentation);
	
	
	public abstract AbstractTranscriptionObject clone();


	/**
	 * @return the textualRepresentation
	 */
	public String getTextualRepresentation() {
		return textualRepresentation;
	}

	/**
	 * @param textualRepresentation the textualRepresentation to set
	 */
	public void setTextualRepresentation(String textualRepresentation) {
		this.textualRepresentation = textualRepresentation;
	}

	/**
	 * @param rawRepresentation
	 */
	public AbstractTranscriptionObject(String rawRepresentation) {
		super();
		this.rawRepresentation = rawRepresentation;
		xmlRepresentation = new LinkedList<Content>();
	}

	/**
	 * @return the rawRepresentation
	 */
	public String getRawRepresentation() {
		return rawRepresentation;
	}

	/**
	 * @param raw
	 */
	public void setRawRepresentation(String raw) {
		this.rawRepresentation = raw;
	}

	/**
	 * @param e
	 * @return
	 */

	// TODO: implement this
	public static AbstractTranscriptionObject getObject(String e) {
//		if (PunctuationCharacter.isPunctuation(e)) {
//			return new PunctuationCharacter(e);
//		}
//
//		if (CommentObject.isComment(e)) {
//			return new CommentObject(e);
//		}
//
//		if (Tag.isTag(e)) {
//			if (e.startsWith("[/"))
//				return new TagCloseElement(e);
//			return new Tag(e);
//		}
//
//		if (Word.isWord(e)) {
//			return new Word(e);
//		}
//
//		return new CommentObject("/*!!! Couldn't handle: "+e+" !!!*/");

		return null;
	}

	/**
	 * @return
	 */
	public LinkedList<Content> getXMLRepresentation() {
		return xmlRepresentation;
	}
	
	public void setXMLRepresentation(Content c) {
		xmlRepresentation.add(c);
	}
	
	public void setXMLRepresentation(LinkedList<Content> l) {
		xmlRepresentation.addAll(l);
	}
	
	protected LinkedList<Content> cloneXMLRepresentation() {
		LinkedList<Content> res = new LinkedList<Content>();
		for (Content c: xmlRepresentation) {
			res.add(c.clone());
		}
		return res;
	}
}
