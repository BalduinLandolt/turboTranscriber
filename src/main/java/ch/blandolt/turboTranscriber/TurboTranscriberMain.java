/**
 * 
 */
package ch.blandolt.turboTranscriber;

import ch.blandolt.turboTranscriber.gui.MainGUI;
import ch.blandolt.turboTranscriber.util.*;

/**
 * @author Balduin Landolt
 *
 */
public class TurboTranscriberMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log.initialize();
		Log.log("Log initialized.\n");
		
		//TODO Run Core here

		// TEST: gui
		MainGUI gui = new MainGUI();
		gui.showMainGUI();
		
//		Log.log("Requesting log shutdown...");
//		Log.terminate();
	}

	{
		// Collection of Tasks
		// -------------------
		//
		// TODO: Rework Log
		// TODO: Add comments to Log
		//
		// TODO: Rework CustomImagePanel and ThumbnailPanel
		// TODO: Add Comments to those
		//
		// TODO: Set up new GUI
		//
		// ...
	}

}
