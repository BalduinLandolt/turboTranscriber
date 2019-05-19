/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

/**
 * @author Balduin Landolt
 *
 */
public class TagCloseElement extends AbstractTranscriptionObject {

	/**
	 * @param rawRepresentation
	 */
	public TagCloseElement(String rawRepresentation) {
		super(rawRepresentation);
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#generateTextualRepresentation(java.lang.String)
	 */
	@Override
	public String generateTextualRepresentation(String rawRepresentation) {
		return rawRepresentation;
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#clone()
	 */
	@Override
	public AbstractTranscriptionObject clone() {
		return new TagCloseElement(getRawRepresentation());
	}
	
}
