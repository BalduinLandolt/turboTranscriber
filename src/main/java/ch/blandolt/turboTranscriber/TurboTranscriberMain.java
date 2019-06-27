/**
 *
 */
package ch.blandolt.turboTranscriber;

import ch.blandolt.turboTranscriber.core.TurboTranscribeCore;

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
		TurboTranscribeCore core = new TurboTranscribeCore();
		core.run();

		
//		Log.log("Requesting log shutdown...");
//		Log.terminate();
	}

	static {
		// Collection of Tasks
		// -------------------
		//
		// TODO: go through all legacy code
		//
		// TODO: look at error logging
		//
		// TODO: Rework CustomImagePanel and ThumbnailPanel
		// TODO: Add option to remove images
		//
		// TODO: integrate or remove MainDataSet class
		//
		// TODO: implement settings
		//
		// TODO: have a look at AbortException
		//
		// TODO: no cropping, when selection is outide of raster or the entire image
		//
		// TODO: improve Popped out Image inspection view
		// TODO: show image inspection view upon double klicking image panel in transcription view
		//
		// TODO: add things like "open recent"
		//
		// TODO: auto TEI core(?)/minimal(?) schema check, to see, what elements are anchors/containers/...
		//
		// TODO: Comments
		//       - Settings.java
		//       - datastructure/*
		//       - ThumbnailPanel
		//       - CustomImagePanel
		//		 - ... newly added functions
		//
		// ...
	}

}
