/**
 * 
 */
package ch.blandolt.turboTranscriber.core;

import ch.blandolt.turboTranscriber.gui.MainGUI;
import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.Settings;

import javax.swing.*;

/**
 * Core class of TurboTranscribe.
 * <p>
 * Organizes the core functionality of the application.
 * </p>
 *
 * @author Balduin Landolt
 *
 */
public class TurboTranscribeCore {
    private MainGUI gui;

    /**
     * Initialized the core of TurboTranscribe.
     * <p>
     *     This includes initializing the Log, loading settings (in progress), etc.
     * </p>
     */
    public TurboTranscribeCore() {

        Log.initialize();
        Log.log("Log initialized.\n");

        // TODO: implement
        //         - settings
        //         - ...?
    }

    /**
     * Runs the core of TurboTranscribe. This launches the GUI.
     */
    public void run() {
        // TODO: more?

        // Launch GUI
        SwingUtilities.invokeLater(() -> gui = new MainGUI(this));
        SwingUtilities.invokeLater(() -> gui.showMainGUI());
    }

    /**
     * Requests the program to shut down.
     */
    public void prepareShutDown() {
        // TODO: Check for unsaved changes.


        if (Settings.askBeforeShutDown()) {
            int result = JOptionPane.showConfirmDialog(gui, "Are you sure you want to shut down?", "Sure...",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION) {
                return;
            }
            if (result == JOptionPane.YES_OPTION) {
                shutDown();
            }
        } else {
            shutDown();
        }
    }

    private void shutDown() {
        gui.close();
        Log.terminate();
        System.out.println("Log is gone. Exiting successfully.");
        System.exit(0);
    }

    public void am_new_transcription() {
        Log.log("Action: New Transcription");
        // TODO: Implement
    }

    public void am_import_xml() {
        Log.log("Action: Import XML");
        // TODO: Implement
    }

    public void am_import_raw() {
        Log.log("Action: Import Raw");
        // TODO: Implement
    }

    public void am_save() {
        Log.log("Action: Save");
        // TODO: Implement
    }

    public void am_save_as() {
        Log.log("Action: Save As");
        // TODO: Implement
    }

    public void am_close() {
        Log.log("Action: Close");
        // TODO: Implement
    }

    public void am_load_images() {
        Log.log("Action: Load Images");
        // TODO: Implement
    }
}
