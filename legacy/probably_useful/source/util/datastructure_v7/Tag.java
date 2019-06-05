/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.jdom2.Attribute;
import org.jdom2.Element;

import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.Log;
import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.TagShortcuts;

/**
 * @author Balduin Landolt
 *
 */
public class Tag extends AbstractTranscriptionObject {
	private String tagName;
	private HashMap<String, String> attributes;
	private boolean isEmpty = false;
	

	/**
	 * @param e
	 */
	public Tag(String s) {
		super(s);
		attributes = new HashMap<String, String>();
		setTextualRepresentation(generateTextualRepresentation(s));
		generateTagNameAndAttributes();
		Element e = new Element(tagName);
		addAttributes(e);
		setXMLRepresentation(e);
	}



	/**
	 * @param e
	 */
	private void addAttributes(Element e) {
	    Iterator<Entry<String, String>> it = attributes.entrySet().iterator();
	    while (it.hasNext()) {
	        Entry<String, String> pair = it.next();
	        Attribute a = new Attribute(pair.getKey(), pair.getValue());
	        e.setAttribute(a);
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}



	/**
	 * 
	 */
	private void generateTagNameAndAttributes() {
		String s = getTextualRepresentation();
//		
//		if (!s.contains(":")) {
//			s = TagShortcuts.getNormalizedForm(s);
//		}
//		
//		String[] ss = s.split(":");
//		tagName = ss[0];
//		String attributes = ss[1];
//		
//		if (!(attributes.endsWith("/")) && TagShortcuts.tagIsAlwaysEmpty(tagName))
//			attributes += "/";
//			
//		
//		if (ss.length > 1)
//			processAttributes(attributes);
		
		if (!s.contains("=")) {
			tagName = s;
		} else {
			String[] ss = s.split("=");
			tagName = ss[0];
			String attributes = ss[1];
			
			if (tagName.equals("lb") || tagName.equals("pb")) {
				attributes += "/";
			}
			
			processAttributes(attributes);
		}
	}




	/**
	 * @param string
	 */
	private void processAttributes(String string) {
		if (string.endsWith("/")) {
			isEmpty = true;
			string = string.replace("/", "");
		}
		String[] ss = string.split(",");
		
		for (String s: ss) {
			processAttribute(s);
		}
	}



	/**
	 * @param s
	 */
	private void processAttribute(String s) {
//		String[] ss = s.split("=");
//		
//		if (ss.length == 2) {
//			attributes.put(ss[0], ss[1]);
//		} else {
//			Log.log("Got a weird tag: "+getTextualRepresentation());
//		}

		if (tagName.equals("lb")) {
			attributes.put("n", s);
		}
		if (tagName.equals("pb")) {
			attributes.put("n", s);
		}
		if (tagName.equals("l")) {
			attributes.put("n", s);
		}
		if (tagName.equals("lg")) {
			attributes.put("type", "stanza");
			attributes.put("n", s);
		}
	}



	/**
	 * @param e
	 * @return
	 */
	public static boolean isTag(String e) {
		e = e.trim();
		return (e.startsWith("[") && e.endsWith("]"));
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#generateTextualRepresentation(java.lang.String)
	 */
	@Override
	public String generateTextualRepresentation(String rawRepresentation) {
		String s = rawRepresentation.replace("[", "");
		s = s.replace("]", "");
		return s;
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.datastructure.AbstractTranscriptionObject#clone()
	 */
	@Override
	public AbstractTranscriptionObject clone() {
		Tag clone = new Tag(getRawRepresentation());
		clone.setXMLRepresentation(cloneXMLRepresentation());
		return clone;
	}



	/**
	 * @return
	 */
	public boolean isEmpty() {
		return isEmpty;
	}



	/**
	 * @return
	 */
	public String getTagName() {
		return tagName;
	}
	
}
