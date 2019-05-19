/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.TagShortcuts.shortcutType;


/**
 * @author Balduin Landolt
 *
 */
public class TagShortcuts {
	private static LinkedList<TagSet> sets = new LinkedList<TagSet>();
	private static LinkedList<String> always_empty_tags = new LinkedList<String>();
	
	public static enum shortcutType {TAG_SHORTCUT, TAG_ALLWAYS_EMPTY, ABBREVIATION_SHORTCUT};
	
	/**
	 * @param s
	 * @return
	 */
	public static String getNormalizedForm(String s) {
		
		String[] ss = s.split("\\=");
		TagSet set = getApplicableSet(ss[0]);
		if (set == null)
			return s;
		
		if (ss.length>1) {
			set.addValues(ss[1]);
		}
		String norm = set.getNormalizedForm();
		Log.log("Normalized '"+s+"' to '"+norm+"'");
		return norm;
	}

	/**
	 * @param string
	 * @return
	 */
	private static TagSet getApplicableSet(String s) {
		
		for (TagSet t: sets) {
			if (t.callName.equals(s))
				return t;
		}
		
		return null;
	}


	/**
	 * @param in
	 */
	public static void load(InputStream in, shortcutType type) {
		Log.log("Loading from stream...");
		if (in==null) {
			Log.log("Stream doesn't exist");
			return;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		try {
			String first = reader.readLine();
			first = removeBOM(first);
			processLine(first, type);
			while (reader.ready()) {
				String line = reader.readLine();
				processLine(line, type);
			}
		} catch (IOException e) {
			Log.log("IOException upon trying to load Tagsets from file.");
			e.printStackTrace();
		}
	}

	/**
	 * @param first
	 * @return
	 */
	private static String removeBOM(String s) {
		if (s.startsWith("\uFEFF"))
			s = s.substring(1);
		return s;
	}

	/**
	 * @param type 
	 * @param line
	 */
	private static void processLine(String in, shortcutType type) {
		String line = in;
		if (line == null)
			return;
		line = line.trim();
		
		if (line.isEmpty())
			return;

		line = line.replace("*/", "_quote_start_");
		line = line.replace("/*", "_quote_end_");
		line = line.replaceAll("_quote_start_.*?_quote_end_", "");

		if (line.isEmpty())
			return;
		
		switch (type) {
		case TAG_SHORTCUT:
			processTagShortcut(line);
			break;
		case TAG_ALLWAYS_EMPTY:
			processAllwaysEmptyTag(line);
			break;

		default:
			break;
		}
		
	}

	private static void processAllwaysEmptyTag(String line) {
		always_empty_tags.add(line);
	}

	private static void processTagShortcut(String line) {
		TagSet set = new TagSet();
		line = line.replace("}", "");
		if (line.endsWith("/")) {
			set.isEmpty = true;
			line = line.replace("/", "");
		}
		String[] ss = line.split("\\{");
		set.callName = ss[0];
		ss = ss[1].split(":");
		set.type = ss[0];
		ss = ss[1].split(",");
		for (String s: ss) {
			Attribute a = new Attribute(s);
			set.attributes.add(a);
		}
		sets.add(set);
	}
	
	
	private static class TagSet {
		protected String callName="";
		protected String type="";
		protected LinkedList<Attribute> attributes = new LinkedList<Attribute>();
		protected boolean isEmpty = false;
		/**
		 * @param string
		 */
		protected void addValues(String s) {
			String[] ss = s.split(",");
			for (String v: ss) {
				addValue(v);
			}
		}
		/**
		 * @param v
		 */
		private void addValue(String v) {
			for (Attribute a: attributes) {
				if (a.value == null) {
					a.value = v;
					return;
				}
			}
		}
		/**
		 * @return
		 */
		public String getNormalizedForm() {
			StringBuffer res = new StringBuffer();
			res.append(type+":");
			for (Attribute a: attributes) {
				res.append(a.name + "=" + a.value + ",");
			}
			res.setLength(res.length()-1);
			
			if (isEmpty)
				res.append('/');
			
			return res.toString();
		}
	}
	
	private static class Attribute {
		/**
		 * @param s
		 */
		public Attribute(String s) {
			String[] ss = s.split("\\=");
			name=ss[0];
			if (ss.length>1)
				value = ss[1];
			else
				value = null;
		}
		protected String name="";
		protected String value="";
	}

	public static boolean tagIsAlwaysEmpty(String tagName) {
		for (String s: always_empty_tags) {
			if (s.equals(tagName))
				return true;
		}
		
		return false;
	}
	
}
