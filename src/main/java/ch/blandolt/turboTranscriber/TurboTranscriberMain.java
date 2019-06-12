/**
 *
 */
package ch.blandolt.turboTranscriber;

import ch.blandolt.turboTranscriber.gui.MainGUI;
import ch.blandolt.turboTranscriber.util.*;

import javax.swing.*;

/**
 * Launcher Class for the application `TurboTranscriber`.
 * <p>
 * Contains only one method: `public static void main`, that does nothing but to launch the app.
 * </p>
 *
 * @author Balduin Landolt
 *
 */
public class TurboTranscriberMain {

	/**
	 * Main method of the class {@link TurboTranscriberMain}.
	 * <P>
	 * This Method launches the application.</p>
	 *
	 * @param args ignored.
	 */
	public static void main(String[] args) {
		Log.initialize();
		Log.log("Log initialized.\n");
		
		//TODO Run Core here

		// TEST: gui
		SwingUtilities.invokeLater(() -> {
			MainGUI gui = new MainGUI();
			gui.showMainGUI();
		});
		
//		Log.log("Requesting log shutdown...");
//		Log.terminate();
	}

	{
		// Collection of Tasks
		// -------------------
		//
		// TODO: look at error logging
		//
		// TODO: Rework CustomImagePanel and ThumbnailPanel
		//
		// TODO: Set up new GUI
		//
		// TODO: Implement core class
		//
		// TODO: integrate or remove MainDataSet class
		//
		// TODO: implement settings
		//
		// TODO: have a look at AbortException
		//
		// TODO: Comments
		//       - Settings.java
		//       - datastructure/*
		//       - ThumbnailPanel
		//       - CustomImagePanel
		//
		// ...
	}

}
