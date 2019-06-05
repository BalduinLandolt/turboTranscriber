/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * @author Balduin Landolt
 *
 */
public class PunctuationCharacter extends AbstractTranscriptionObject {

	/**
	 * @param e
	 */
	public PunctuationCharacter(String rawRepresentation) {
		super(rawRepresentation);
		setTextualRepresentation(generateTextualRepresentation(rawRepresentation));
		Element e = new Element("pc");
		e.setText(getTextualRepresentation());
		setXMLRepresentation(e);
	}

	/**
	 * @param e
	 * @return
	 */
	public static boolean isPunctuation(String e) {
		if (e.equals("."))
			return true;
		if (e.equals("-"))
			return true;
		
		//TODO more possible punctuation characters?
		return false;
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#clone()
	 */
	@Override
	public AbstractTranscriptionObject clone() {
		PunctuationCharacter clone = new PunctuationCharacter(getRawRepresentation());
		clone.setXMLRepresentation(cloneXMLRepresentation());
		return clone;
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#getTextualRepresentation(java.lang.String)
	 */
	@Override
	public String generateTextualRepresentation(String rawRepresentation) {
		String res = rawRepresentation;
		//TODO catch and modify, if rawRepresentation is a Glyphe
		
		return res;
	}
	
}
