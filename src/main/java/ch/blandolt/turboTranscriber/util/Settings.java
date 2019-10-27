/**
 * 
 */
package ch.blandolt.turboTranscriber.util;


//TODO Import from older version. Check!

import java.io.File;

/**
 * @author Balduin Landolt
 *
 */
public class Settings {
	private static boolean openLogOnClose = false;
	private static boolean askBeforeShutDown = false;
	private static boolean autoOverwrite = true;
	private static boolean autoOpenRawFile = false;
	private static boolean autoOpenXMLFile = false;
	private static File current_raw_file = null;
	
	public static boolean openLogOnClose() {
		return openLogOnClose;
	}
	
	public static void getSettings() {
		//TODO load Settings from preferences
	}

	/**
	 * @return
	 */
	public static boolean askBeforeShutDown() {
		return askBeforeShutDown;
	}

	/**
	 * @return
	 */
	public static boolean autoOverwrite() {
		return autoOverwrite;
	}

	public static File getCurrent_raw_file() {
		return current_raw_file;
	}

	public static void setCurrent_raw_file(File current_raw_file) {
		Settings.current_raw_file = current_raw_file;
	}

	public static boolean isAutoOpenRawFile() {
		return autoOpenRawFile;
	}

	public static void setAutoOpenRawFile(boolean autoOpenRawFile) {
		Settings.autoOpenRawFile = autoOpenRawFile;
	}

	public static boolean isAutoOpenXMLFile() {
		return autoOpenXMLFile;
	}

	public static void setAutoOpenXMLFile(boolean autoOpenXMLFile) {
		Settings.autoOpenXMLFile = autoOpenXMLFile;
	}

	public static boolean hasCurrentRawFile() {
		return null != getCurrent_raw_file();
	}
}
