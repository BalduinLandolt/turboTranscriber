/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

/**
 * @author Balduin Landolt
 *
 */
public abstract class AbstractWordPartContent extends AbstractWordContent {
	

	/**
	 * @param rawRepresentation
	 */
	public AbstractWordPartContent(String rawRepresentation) {
		super(rawRepresentation);
	}

	/**
	 * @param p
	 * @return
	 */
	public static AbstractWordPartContent getWordPartContent(String p) {
		if (Glyphe.isGlyphe(p))
			return new Glyphe(p);
		
		return new TextContent(p);
	}
	

	
}
