/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

import org.jdom2.Comment;

/**
 * @author Balduin Landolt
 *
 */
public class CommentObject extends AbstractTranscriptionObject {

	/**
	 * @param string
	 */
	public CommentObject(String e) {
		super(e);
		
		setTextualRepresentation(generateTextualRepresentation(e));
		
		Comment c = new Comment(getTextualRepresentation());
		setXMLRepresentation(c);
	}

	/**
	 * @param e
	 * @return
	 */
	public static boolean isComment(String e) {
		e = e.trim();
		if (e.startsWith("/*") && e.endsWith("*/"))
			return true;
		else
			return false;
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#generateTextualRepresentation(java.lang.String)
	 */
	@Override
	public String generateTextualRepresentation(String e) {
		String text = e.replace("_", " ");
		text = text.replace("/*", "");
		text = text.replace("*/", "");
		return text;
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#clone()
	 */
	@Override
	public AbstractTranscriptionObject clone() {
		CommentObject clone = new CommentObject(getRawRepresentation());
		clone.setXMLRepresentation(cloneXMLRepresentation());
		return clone;
	}
	
}
