/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

/**
 * @author Balduin Landolt
 *
 */
public abstract class AbstractWordContent extends AbstractTranscriptionObject {
	
	/**
	 * @param rawRepresentation
	 */
	public AbstractWordContent(String rawRepresentation) {
		super(rawRepresentation);
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#generateTextualRepresentation(java.lang.String)
	 */
	@Override
	public abstract String generateTextualRepresentation(String rawRepresentation);
	
	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#clone()
	 */
	@Override
	public abstract AbstractTranscriptionObject clone();

	/**
	 * @param p
	 * @return
	 */
	public static AbstractWordContent getWordContent(String p) {
		if (Abbreviation.isAbbreviation(p)) {
			return new Abbreviation(p);
		}
		
		return new WordPart(p);
		//return AbstractWordPartContent.getWordPartContent(p);
		
//		
//		if (PunctuationCharacter.isPunctuation(e)) {
//			return new PunctuationCharacter(e);
//		}
//		
//		if (CommentObject.isComment(e)) {
//			return new CommentObject(e);
//		}
//		
//		if (Tag.isTag(e)) {
//			return new Tag(e);
//		}
//		
//		if (Word.isWord(e)) {
//			return new Word(e);
//		}
//		
//		return new CommentObject("/*!!! Couldn't handle: "+e+" !!!*/");
	}
	
}
