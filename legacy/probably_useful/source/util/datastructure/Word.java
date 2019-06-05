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
public class Word extends AbstractTranscriptionObject {
	private ArrayList<AbstractTranscriptionObject> contents;

	/**
	 * @param e
	 */
	public Word(String s) {
		super(s);
		contents = new ArrayList<AbstractTranscriptionObject>();
		Element e = new Element("w");
		setXMLRepresentation(e);
		addContents(getRawRepresentation());
	}

	/**
	 * @param rawRepresentation
	 */
	private void addContents(String s) {
	//	s = s.replace("{", "¢{");
	//	s = s.replace("}", "}¢");
		
		//separates abbreviations from WordParts
		s = s.replace("(", "¢(");
		s = s.replace(")", ")¢");
		
		while (s.contains("¢¢")) {
			s = s.replace("¢¢", "¢");
		}
		
		String[] ss = s.split("¢");
		
		for (String p: ss) {
			AbstractWordContent c = AbstractWordContent.getWordContent(p);
			if (c != null)
				addContent(c);
		}
	}

	/**
	 * @param e
	 * @return
	 */
	public static boolean isWord(String e) {
		// TODO how is that defined???
		return true;
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#generateTextualRepresentation(java.lang.String)
	 */
	@Override
	public String generateTextualRepresentation(String nullString) {
		String s = "";
		for (AbstractTranscriptionObject o: contents) {
			if (o != null)
				s += o.getTextualRepresentation();
		}
		return s;
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#clone()
	 */
	@Override
	public AbstractTranscriptionObject clone() {
		Word clone = new Word(getRawRepresentation());
		clone.setXMLRepresentation(cloneXMLRepresentation());
		for (AbstractTranscriptionObject o: contents) {
			clone.addContent(o.clone());
		}
		return clone;
	}

	/**
	 * @param clone
	 */
	private void addContent(AbstractTranscriptionObject o) {
		contents.add(o);
		setTextualRepresentation(generateTextualRepresentation(null));
		//setXMLRepresentation(o.getXMLRepresentation());
		Element e = (Element) getXMLRepresentation().getFirst();
		e.addContent(o.getXMLRepresentation());
	}
	
}
