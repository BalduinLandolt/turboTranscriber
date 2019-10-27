/**
 * 
 */
package ch.blandolt.turboTranscriber.core;

import ch.blandolt.turboTranscriber.gui.MainGUI;
import ch.blandolt.turboTranscriber.gui.ThumbnailPanel;
import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.Settings;
import ch.blandolt.turboTranscriber.util.datastructure.XMLFactory;
import ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation.AbstractTranscriptionObject;
import ch.blandolt.turboTranscriber.util.datastructure.nativeRepresentation.DataFactory;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.Tokenizer;
import ch.blandolt.turboTranscriber.util.datastructure.tokenization.TranscriptionToken;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private boolean IS_LOCKED = false;
    private boolean REQUESTED_REFRESH = false;
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

        if (isLocked()){
            Log.log("Tokenizing still locked.");
            requestRefreshWhenUnlocked();
            return;
        }
        IS_LOCKED = true;
        REQUESTED_REFRESH = false;
        long seconds = 5;
        startTimer(seconds);

        long start = System.currentTimeMillis();
        List<TranscriptionToken> tokens = Tokenizer.tokenize(gui.getTranscriptionString());
        long duration = System.currentTimeMillis() - start;
        Log.log("Tokenizing took: " +  duration + "ms");

        start = System.currentTimeMillis();
        List<AbstractTranscriptionObject> data = DataFactory.buildDatastructure(tokens);
        duration = System.currentTimeMillis() - start;
        Log.log("Building Datastructure took: " +  duration + "ms");

        //Log.log(data);

        start = System.currentTimeMillis();
        Document document = XMLFactory.createTEIXML(data);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        String s = beautify(outputter.outputString(document));
        gui.setXML(s);
        duration = System.currentTimeMillis() - start;
        Log.log("Building XML took: " +  duration + "ms");

        // TODO: transform XML to HTML

        // TODO: some form of "normalisation" of input?

        // TODO: add javadoc comments
        // TODO: tidy up code

        refreshGUI();
        // TODO: make duration dynamic
    }

    private String beautify(String xml) { // TODO: do something with LB and PB
        String res = xml;
        Pattern p = Pattern.compile("(?s)\\<w\\>.*?\\<\\/w\\>");
        Matcher m = p.matcher(xml);

        while (m.find()) {
            String hit = m.group();
            String replacement = hit.replaceAll("\n", "");
            replacement = replacement.replaceAll("\\s+", " ");
            replacement = replacement.replace("> ", ">");
            replacement = replacement.replace(" <", "<");
            res = res.replace(hit, replacement);
        }

        res = res.replaceAll("[^\\S\\n]*\\<pb ", "\n\n <pb ");
        res = res.replaceAll("[^\\S\\n]*\\<cb ", "\n  <cb ");
        res = res.replaceAll("[^\\S\\n]*\\<lb ", "   <lb ");
        res = res.replaceAll("(\\S)   +\\<lb ", "$1<lb ");

        return res;
    }

    private void startTimer(long seconds) {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable unlock  = () -> this.unlock();
        ses.schedule(unlock , seconds, TimeUnit.SECONDS);
    }

    public void unlock() {
        IS_LOCKED = false;
        Log.log("Unlocked Tokenizer");
        if (REQUESTED_REFRESH){
            REQUESTED_REFRESH = false;
            Log.log("Refresh has been requested. Reparsing.");
            a_transcription_state_changed();
        }
    }

    public boolean isLocked() {
        return IS_LOCKED;
    }

    public void requestRefreshWhenUnlocked() {
        REQUESTED_REFRESH = true;
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

        gui = new MainGUI(this);
        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            gui.showMainGUI();
        });
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
        return true;
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

    public void am_export_xml() {
        Log.log("Action: Export XML");
        String s = gui.getXMLString();

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./sample_data"));
        fc.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        fc.setMultiSelectionEnabled(false);
        int returnVal = fc.showSaveDialog(gui);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().endsWith(".xml"))
                f = new File(f.getPath() + ".xml");
            try {
                List<CharSequence> lines = s.lines()
                        .map(x -> new StringBuffer(x))
                        .collect(Collectors.toList());
                Files.write(Paths.get(f.toURI()), lines);
                if (Desktop.isDesktopSupported() && Settings.isAutoOpenXMLFile()) {
                    Desktop d = Desktop.getDesktop();
                    d.open(f);
                }
                refreshGUI();
                Log.log("Exported XML to File: "+f.getPath());
            } catch (IOException e) {
                e.printStackTrace();
                Log.log(e.getStackTrace());
            }

            // TODO: handle overwrite etc.
        } else {
            Log.log("Aborted.");
        }
    }

    /**
     * Action (Menu): Import Raw
     */
    public void am_import_raw() {
        Log.log("Action: Import Raw");

        // TODO: should that discard unsaved changes?

        // TODO: should add a new data stage to text area

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./sample_data"));
        fc.setFileFilter(new FileNameExtensionFilter("Raw", "txt", "raw"));
        fc.setMultiSelectionEnabled(false);
        int returnVal = fc.showOpenDialog(gui);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            List<String> lines_raw = loadRaw(f);
            gui.setRaw(lines_raw);
            //a_transcription_state_changed();
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
        saveRawToFile(Settings.getCurrent_raw_file());
        refreshGUI();
    }

    /**
     * Action (Menu): Save As
     */
    public void am_save_as() {
        Log.log("Action: Save As");

        // TODO: Add concept of "current file I'm working on".
        //      that would change, when doing save_as or import; and it would define,
        //      where save saves the data to.

        // TODO: Add concept of saved/unsaved changes

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./sample_data"));
        fc.setFileFilter(new FileNameExtensionFilter("Raw", "txt", "raw"));
        fc.setMultiSelectionEnabled(false);
        int returnVal = fc.showSaveDialog(gui);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            saveRawToFile(f);
        } else {
            Log.log("Aborted.");
        }
    }

    private void saveRawToFile(File f) {
        // TODO: handle overwrite etc.
        if (!f.getName().endsWith(".txt"))
            f = new File(f.getPath() + ".txt");
        try {
            List<CharSequence> lines = gui.getTranscriptionString().lines()
                    .map(x -> new StringBuffer(x))
                    .collect(Collectors.toList());
            Files.write(Paths.get(f.toURI()), lines);
            Settings.setCurrent_raw_file(f);
            if (Desktop.isDesktopSupported() && Settings.isAutoOpenRawFile()) {
                Desktop d = Desktop.getDesktop();
                d.open(f);
            }
            refreshGUI();
            Log.log("Exported Raw to File: "+f.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.log(e.getStackTrace());
        }
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
