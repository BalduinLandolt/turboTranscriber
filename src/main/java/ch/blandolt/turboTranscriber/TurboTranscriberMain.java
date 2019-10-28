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
		// TODO: add javadoc comments
		//
		// TODO: go through all legacy code
		//
		// TODO: look at error logging
		//
		// TODO: Image-Related:
		// 		- Rework CustomImagePanel and ThumbnailPanel
		// 		- no cropping, when selection is outside of raster or the entire image
		// 		- Add option to remove images
		// 		- Add file names to thumbnails and image
		//
		// TODO: improve Popped out Image inspection view

		// TODO: show image inspection view upon double clicking image panel in transcription view
		//
		// TODO: implement settings
		//
		// TODO: have a look at AbortException
		//
		// TODO: Settle for file-based or project-based working
		//
		// TODO: add things like "open recent"

		// TODO: implement menota
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
