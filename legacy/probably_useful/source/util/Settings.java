/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.util;

/**
 * @author Balduin Landolt
 *
 */
public class Settings {
	private static boolean openLogOnClose = false;
	private static boolean askBeforeShutDown = false;
	private static boolean autoOverwrite = true;
	
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
}