/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

import org.jdom2.Text;

import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.Log;

/**
 * @author Balduin Landolt
 *
 */
public class TextContent extends AbstractWordPartContent {
	
	/**
	 * @param rawRepresentation
	 */
	public TextContent(String rawRepresentation) {
		super(rawRepresentation);
		Text t = new Text(rawRepresentation);
		setXMLRepresentation(t);
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
		TextContent clone = new TextContent(getRawRepresentation());
		clone.setXMLRepresentation(cloneXMLRepresentation());
		return clone;
	}
	
}
