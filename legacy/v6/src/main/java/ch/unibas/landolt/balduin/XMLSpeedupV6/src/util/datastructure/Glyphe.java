/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

import org.jdom2.Element;

/**
 * @author Balduin Landolt
 *
 */
public class Glyphe extends AbstractWordPartContent  {
	
	/**
	 * @param rawRepresentation
	 */
	public Glyphe(String rawRepresentation) {
		super(rawRepresentation);
		Element e = new Element("g");
		setTextualRepresentation(generateTextualRepresentation(rawRepresentation));
		addReference(e);
		setXMLRepresentation(e);
	}
	
	/**
	 * @param e
	 */
	private void addReference(Element e) {
		String val = "#" + getTextualRepresentation();
		e.setAttribute("ref", val);
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractWordContent#generateTextualRepresentation(java.lang.String)
	 */
	@Override
	public String generateTextualRepresentation(String s) {
		s = s.replace("{", "");
		s = s.replace("}", "");
		return s;
	}
	
	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractWordContent#clone()
	 */
	@Override
	public AbstractTranscriptionObject clone() {
		Glyphe clone = new Glyphe(getRawRepresentation());
		clone.setXMLRepresentation(cloneXMLRepresentation());
		return clone;
	}

	/**
	 * @param p
	 * @return
	 */
	public static boolean isGlyphe(String p) {
		return (p.trim().startsWith("{") && p.endsWith("}"));
	}
	
}
