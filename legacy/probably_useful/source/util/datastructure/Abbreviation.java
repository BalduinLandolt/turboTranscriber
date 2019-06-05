/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

import java.util.ArrayList;

import org.jdom2.Element;

/**
 * @author Balduin Landolt
 *
 */
public class Abbreviation extends AbstractWordContent {
	private WordPart expansion;
	private WordPart abbeviationMark;
	private Element ex;
	private Element am;
	
	//TODO make abbreviations dynamically customizable over a list
	
	/**
	 * @param rawRepresentation
	 */
	public Abbreviation(String s) {
		super(s);
		addContents(getRawRepresentation());
		Element e = new Element("choice");
		e.addContent(ex);
		e.addContent(am);
		setXMLRepresentation(e);
	}
	

	private void addContents(String s) {
		
		if (!s.contains(";"))
			s = normalizeSpecialCase(s);
		
		String[] ss = s.split(";");
		
		//TODO check special case list somewhere here
		
		generateExpansion(ss[0]);
		generateAbbreviationMark(ss[1]);
	}
	
	/**
	 * @param string
	 */
	private void generateAbbreviationMark(String s) {
		am = new Element("am");
		WordPart content = new WordPart(s);
		am.addContent(content.getXMLRepresentation());
	}


	/**
	 * @param string
	 */
	private void generateExpansion(String s) {
		ex = new Element("ex");
		WordPart content = new WordPart(s);
		ex.addContent(content.getXMLRepresentation());
	}


	/**
	 * @param s
	 * @return
	 */
	private String normalizeSpecialCase(String s) {
		return s+";"+s;
	}


	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractWordContent#generateTextualRepresentation(java.lang.String)
	 */
	@Override
	public String generateTextualRepresentation(String rawRepresentation) {
		return rawRepresentation;
	}
	
	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractWordContent#clone()
	 */
	@Override
	public AbstractTranscriptionObject clone() {
		Abbreviation clone = new Abbreviation(getRawRepresentation());
		clone.setXMLRepresentation(cloneXMLRepresentation());
		clone.abbeviationMark = (WordPart) abbeviationMark.clone();
		clone.expansion = (WordPart) expansion.clone();
		return clone;
	}


	/**
	 * @param p
	 * @return
	 */
	public static boolean isAbbreviation(String p) {
		p = p.trim();
		return (p.startsWith("(") && p.endsWith(""));
	}
	
}
