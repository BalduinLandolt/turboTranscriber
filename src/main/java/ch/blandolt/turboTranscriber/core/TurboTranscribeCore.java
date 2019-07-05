/**
 * 
 */
package ch.blandolt.turboTranscriber.core;

import ch.blandolt.turboTranscriber.gui.MainGUI;
import ch.blandolt.turboTranscriber.gui.ThumbnailPanel;
import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.Settings;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.Tokenizer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    private Data data = new Data();

    public void thumbnailRequestsActivation(ThumbnailPanel thumbnail) {
        data.activatedImage = thumbnail.getInitialImage();
        refreshGUI();
    }

    public void a_inspect_selected_image() {
        popoutImage();
    }

    public BufferedImage getSelectedImage() {
        return data.activatedImage;
    }

    public void a_transcription_state_changed() {
        Log.log("Transcription has changed.");

        Tokenizer.tokenize(gui.getTranscriptionString());

        // TODO: tokenize transcription
        // TODO: refresh gui with normalized representation of transcription
        // TODO: build up internal representation from tokens
        // TODO: generate XML
        // TODO: transform XML to HTML

        refreshGUI();
    }

    // TODO: Rethink Data organisation!
    private class Data {
        LinkedList<BufferedImage> loadedImages = new LinkedList<>();
        BufferedImage activatedImage = null;
    }

    /**
     * Initialized the core of TurboTranscribe.
     * <p>
     *     This includes initializing the Log, loading settings (in progress), etc.
     * </p>
     */
    public TurboTranscribeCore() {

        Log.initialize();
        Log.log("Log initialized.\n");

        loadSettings();

        // TODO: more?
    }

    private void loadSettings() {
        // TODO: implement
    }

    /**
     * Runs the core of TurboTranscribe. This launches the GUI.
     */
    public void run() {
        // TODO: more to do in core.run()?

        // Launch GUI
        SwingUtilities.invokeLater(() -> gui = new MainGUI(this));
        SwingUtilities.invokeLater(() -> gui.showMainGUI());
    }

    /**
     * Requests the program to shut down.
     */
    public void prepareShutDown() {
        if (hasUnsavedChanges()) {
            // TODO: Dialog, asking if changes should be saved before shutdown
        }


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

    /**
     * Checks, if there are any unsaved changes.
     *
     * @return True, if there are unsaved changes; false otherwise.
     */
    public boolean hasUnsavedChanges() {
        // TODO: implement checking, if there are unsaved changes
        return false;
    }

    /**
     * Action (Menu): New Transcription
     */
    public void am_new_transcription() {
        Log.log("Action: New Transcription");
        // TODO: Implement
    }

    /**
     * Action (Menu): Import XML
     */
    public void am_import_xml() {
        Log.log("Action: Import XML");
        // TODO: Implement
    }

    /**
     * Action (Menu): Import Raw
     */
    public void am_import_raw() {
        Log.log("Action: Import Raw");

        // TODO: should that discard unsaved changes?

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./sample_data"));
        fc.setFileFilter(new FileNameExtensionFilter("Raw", "txt", "raw"));
        fc.setMultiSelectionEnabled(false);
        int returnVal = fc.showOpenDialog(gui);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            List<String> lines_raw = loadRaw(f);
            gui.setRaw(lines_raw);
            a_transcription_state_changed();
            //refreshGUI();
            // TODO: copy to project?
        } else {
            Log.log("Aborted.");
        }
    }

    private List<String> loadRaw(File f) {
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            Log.log("Error: Failed to read raw file.");
            return null;
        }
    }

    /**
     * Action (Menu): Save
     */
    public void am_save() {
        Log.log("Action: Save");
        // TODO: Implement
    }

    /**
     * Action (Menu): Save As
     */
    public void am_save_as() {
        Log.log("Action: Save As");
        // TODO: Implement
    }

    /**
     * Action (Menu): Close
     */
    public void am_close() {
        Log.log("Action: Close");
        // TODO: Implement
    }

    /**
     * Action (Menu): load images
     */
    public void am_load_images() {
        Log.log("Action: Load Images");

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./sample_data"));
        fc.setFileFilter(new FileNameExtensionFilter("images", "jpg", "jpeg"));
        fc.setMultiSelectionEnabled(true);
        int returnVal = fc.showOpenDialog(gui);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] ff = fc.getSelectedFiles();
            if (ff.length < 1) {
                return;
            } else {
                loadImages(ff);
            }
        } else {
            Log.log("Aborted.");
        }
    }

    private void loadImages(File[] files) {
        LinkedList<BufferedImage> images = new LinkedList<BufferedImage>();
        for (File f: files) {
            BufferedImage im = loadImage(f);
            if (im != null) {
                images.add(im);
            }
        }
        Log.log("Loaded Images: "+images.size());
        for (BufferedImage img: images) {
            data.loadedImages.add(img);
        }
        data.activatedImage = data.loadedImages.getFirst();
        //addDataStage();
        refreshGUI();
        gui.switchToImageView();
    }

    private void refreshGUI() {
        if (gui == null)
            return;

        Log.log("Refreshing GUI content.");

        gui.displayImage(data.activatedImage);
        gui.createThumbnails();
        gui.refreshEnabledComponents();

        // TODO: more?
    }

    private BufferedImage loadImage(File f) {
        if (!f.exists())
            return null;

        try {
            BufferedImage im = ImageIO.read(f);
            return im;
        } catch (IOException e) {
            Log.log("Failed to load image.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Action (Button): crop selected area of image
     * @param croppOfSelection
     */
    public void a_crop_selected(BufferedImage croppOfSelection) {
        Log.log("Action: Crop Selected");
        data.loadedImages.add(croppOfSelection);
        data.activatedImage = croppOfSelection;
        refreshGUI();
    }

    public void a_xmlArea_state_changed() {
        gui.refreshEnabledComponents();
        // TODO: Do more things when it changes:
        //       - update xml and styled view with some delay
    }

    public ArrayList<BufferedImage> getLoadedImages() {
        ArrayList<BufferedImage> res = new ArrayList<BufferedImage>(data.loadedImages);
        return res;
    }

    public void popoutImage() {
        JFrame f = new JFrame();
        SwingUtilities.invokeLater(() -> {
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setMinimumSize(new Dimension(400,400));
            f.pack();
            f.setExtendedState(JFrame.MAXIMIZED_BOTH);
            JPanel p = new JPanel(new BorderLayout());
            f.setContentPane(p);
            f.setVisible(true);
            JLabel il = new JLabel(new ImageIcon(data.activatedImage));
            JPanel inner = new JPanel();
            inner.add(il);
            JScrollPane scroller = new JScrollPane(inner);
            scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            p.add(scroller, BorderLayout.CENTER);
        });
    }
}
