/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

import java.util.LinkedList;

import org.jdom2.Content;

/**
 * @author Balduin Landolt
 *
 */
public class WordPart extends AbstractWordContent {
	LinkedList<AbstractWordPartContent> contents;
	
	/**
	 * @param rawRepresentation
	 */
	public WordPart(String rawRepresentation) {
		super(rawRepresentation);
		contents = new LinkedList<AbstractWordPartContent>();
		generateContents(rawRepresentation);
		generateXMLRepresentation();
	}
	
	/**
	 * 
	 */
	private void generateXMLRepresentation() {
		LinkedList<Content> res = new LinkedList<Content>();
		for (AbstractWordPartContent c: contents) {
			res.addAll(c.getXMLRepresentation());
		}
		setXMLRepresentation(res);
	}

	/**
	 * @param s
	 */
	private void generateContents(String s) {
		//TODO special treatment for cases like hyphens etc. that are actually suprasegmental
		
		//separates glyphes from textcontent
		s = s.replace("{", "¢{");
		s = s.replace("}", "}¢");
		
		while (s.contains("¢¢")) {
			s = s.replace("¢¢", "¢");
		}
		
		String[] ss = s.split("¢");
		
		for (String p: ss) {
			AbstractWordPartContent c = AbstractWordPartContent.getWordPartContent(p);
			if (c != null)
				contents.add(c);
		}
	}



	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractWordContent#generateTextualRepresentation(java.lang.String)
	 */
	@Override
	public String generateTextualRepresentation(String rawRepresentation) {
		// TODO Auto-generated method stub
		return rawRepresentation;
	}
	
	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractWordContent#clone()
	 */
	@Override
	public AbstractTranscriptionObject clone() {
		WordPart clone = new WordPart(getRawRepresentation());
		clone.setXMLRepresentation(cloneXMLRepresentation());
		clone.cloneContents();
		return clone;
	}

	/**
	 * 
	 */
	private void cloneContents() {
		LinkedList<AbstractWordPartContent> l = new LinkedList<AbstractWordPartContent>();
		for (AbstractWordPartContent w: contents) {
			l.add((AbstractWordPartContent) w.clone());
		}
	}
	
}
