package ch.blandolt.turboTranscriber.gui;

import ch.blandolt.turboTranscriber.core.TurboTranscribeCore;
import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.Loggable;
import ch.blandolt.turboTranscriber.util.Settings;
import ch.blandolt.turboTranscriber.util.rsyntax.RawTokenMaker;
import ch.blandolt.turboTranscriber.util.rsyntax.TTCompletionProvider;
import ch.blandolt.turboTranscriber.util.rsyntax.WeightedCompletion;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Main GUI class of TurboTranscriber
 *
 * @author Balduin Landolt
 */

public class MainGUI extends JFrame  implements Loggable, WindowListener, DocumentListener {
    private JTabbedPane mainTabbedPane;
    private JPanel mainPanel;
    private JPanel logPane;
    private JTextArea logTextArea;
    private JScrollPane logScroller;
    private JPanel imageContainer;
    private JPanel controls;
    private JButton cropSelected;
    private JPanel pThumbnails;
    private JPanel transcriptionContainer;
    private JSplitPane splitpaneGeneral;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JSplitPane splitterXMLStuff;
    private JSplitPane splitterRSTAvsImage;
    private JScrollPane styledScroller;

    private JLabel picture;
    private JScrollPane pictureScroller;
    private JToolBar imageToolBar;
    private JButton bt_zoomOut;
    private RSyntaxTextArea transcriptionSyntaxTextArea;
    private RTextScrollPane syntaxScroller;
    private RTextScrollPane xmlScroller;
    private RSyntaxTextArea xmlArea;
    private JButton bt_zoomIn;
    private CustomImagePanel imagePanel;
    private JButton inspectImage;

    // Menubar
    private JMenuBar menuBar;
    private JMenu menu_file;
    private JMenuItem menuItem_file_newTranscription;
    private JMenuItem menuItem_file_importXML;
    private JMenuItem menuItem_file_importRaw;
    private JMenuItem menuItem_file_save;
    private JMenuItem menuItem_file_saveAs;
    private JMenuItem menuItem_exportXML;
    private JMenuItem menuItem_file_close;
    private JMenuItem menuItem_file_openFolder;
    private JMenu menu_edit;
    private JMenuItem menuItem_edit_undo;
    private JMenuItem menuItem_edit_redo;
    private JMenuItem menuItem_edit_loadImages;
    private JMenuItem menuItem_edit_inspectSelectedImage;
    private JMenu menu_settings;

    private TurboTranscribeCore owner;

    private BufferedImage loadedImage;
    private float imageScaling = 0.4f;

    private DefaultCompletionProvider provider = null;

    /**
     * Constructor of the GUI.
     * <p>
     * This sets up the entire GUI, creating the JFrame and all of its contents, including the menu bar.
     * </p>
     * <p>
     * Once this is done, the GUI can be shown by calling {@link MainGUI#showMainGUI()}.
     * </p>
     * @param caller the {@link TurboTranscribeCore} that calls this constructor. Is stored as the owner.
     */
    public MainGUI(TurboTranscribeCore caller){

        // TODOs
        // -----
        //
        // TODO: test if images display correctly
        // TODO: finish transcription view
        //     - TODO: Add Image Panel to transcription view.
        // TODO: handle action listening
        // TODO: handle window resizing

        super("Turbo Transcriber");

        owner = caller;

        Log.log("Setting up GUI...");

        setUpMainWindow();

        make_menubar();
        handle_listeners();

        Log.addLoggable(this);

        Log.log("GUI set up.");
    }

    private void setUpMainWindow() {
        // Create Components
        mainTabbedPane = new JTabbedPane();
        mainPanel = new JPanel();
        logPane = new JPanel();
        logTextArea = new JTextArea();
        logScroller = new JScrollPane();
        imageContainer = new CustomImagePanel();
        controls = new CustomImagePanel();
        cropSelected = new JButton("Crop Selection");
        pThumbnails = new CustomImagePanel();
        transcriptionContainer = new JPanel();
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        styledScroller = new JScrollPane();

        picture = new JLabel();
        imageToolBar = new JToolBar();
        bt_zoomOut = new JButton(" - ");
        transcriptionSyntaxTextArea = new RSyntaxTextArea();
        xmlScroller = new RTextScrollPane();
        xmlArea = new RSyntaxTextArea();
        bt_zoomIn = new JButton(" + ");
        imagePanel = new CustomImagePanel();
        inspectImage = new JButton("Inspect Image");

        // Set Up Window Structure
        setContentPane(mainPanel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        setMinimumSize(new Dimension(600, 400));

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(mainTabbedPane, BorderLayout.CENTER);

        mainTabbedPane.addTab("Transcription", transcriptionContainer);
        mainTabbedPane.addTab("Images", imageContainer);
        mainTabbedPane.addTab("Log", logPane);

        // Transcription Tab
        transcriptionContainer.setLayout(new BorderLayout());
        splitpaneGeneral = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        transcriptionContainer.add(splitpaneGeneral, BorderLayout.CENTER);
        leftPanel.setLayout(new BorderLayout());
        rightPanel.setLayout(new BorderLayout());

        JPanel pTmp = new JPanel();
        pTmp.setLayout(new BorderLayout());
        syntaxScroller = new RTextScrollPane(transcriptionSyntaxTextArea);
        splitterRSTAvsImage = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pTmp, syntaxScroller);
        leftPanel.add(splitterRSTAvsImage);

        pictureScroller = new JScrollPane(picture);
        pTmp.add(pictureScroller, BorderLayout.CENTER);
        pTmp.add(imageToolBar, BorderLayout.EAST);
        imageToolBar.setOrientation(JToolBar.VERTICAL);
        imageToolBar.add(bt_zoomIn);
        imageToolBar.add(bt_zoomOut);
        bt_zoomIn.setMinimumSize(new Dimension(50, 50));
        bt_zoomOut.setMinimumSize(new Dimension(50, 50));
        // TODO make buttons look good

        transcriptionSyntaxTextArea.setCodeFoldingEnabled(true);
        syntaxScroller.setLineNumbersEnabled(true);
        syntaxScroller.setFoldIndicatorEnabled(true);
        syntaxScroller.setIconRowHeaderEnabled(true);
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/raw", RawTokenMaker.class.getName());
        transcriptionSyntaxTextArea.setSyntaxEditingStyle("text/raw");

        try {
            // TODO: make this a setting
            // TODO: make all of the UI according, not just the text area
            // TODO: ensure loading in jars (might not work, not sure)

            String path = "theme_light.xml";
            // TODO: make dark theme more contrastive
            //String path = "theme_dark.xml";
            Log.log(getClass());
            InputStream in = getClass().getClassLoader().getResourceAsStream(path);
            Theme theme = Theme.load(in);
            theme.apply(transcriptionSyntaxTextArea);
        } catch (IOException ioe) { // Never happens
            ioe.printStackTrace();
        }
        Font prev = transcriptionSyntaxTextArea.getFont();
        transcriptionSyntaxTextArea.setBracketMatchingEnabled(true);
        transcriptionSyntaxTextArea.setPaintMatchedBracketPair(true);
        transcriptionSyntaxTextArea.setShowMatchedBracketPopup(true);
        transcriptionSyntaxTextArea.setAnimateBracketMatching(true);
        // TODO: get this to work!
        transcriptionSyntaxTextArea.setFont(new Font(prev.getName(), prev.getStyle(), prev.getSize()+4));
        // TODO make font size a setting

        setUpCodeCompletion();

        xmlScroller = new RTextScrollPane(xmlArea);
        styledScroller = new JScrollPane(new JLabel("Imagine nice HTML here."));
        splitterXMLStuff = new JSplitPane(JSplitPane.VERTICAL_SPLIT, xmlScroller, styledScroller);
        rightPanel.add(splitterXMLStuff);

        xmlArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        xmlArea.setEditable(false);
        xmlArea.setCodeFoldingEnabled(true);
        xmlScroller.setFoldIndicatorEnabled(true);
        xmlScroller.setLineNumbersEnabled(true);
        xmlScroller.setIconRowHeaderEnabled(true);

        // Build Image Tab
        imageContainer.setLayout(new BorderLayout());
        imageContainer.add(imagePanel, BorderLayout.CENTER);
        imageContainer.add(controls, BorderLayout.EAST);
        controls.add(cropSelected);
        controls.add(inspectImage);
        JScrollPane scrlTmp = new JScrollPane(pThumbnails);
        imageContainer.add(scrlTmp, BorderLayout.SOUTH);
        // TODO: beautify at some point

        // Build Log Tab
        logScroller = new JScrollPane(logTextArea);
        logPane.setLayout(new BorderLayout());
        logPane.add(logScroller, BorderLayout.CENTER);
    }

    private void setUpCodeCompletion() {
        provider = new TTCompletionProvider();
        provider.setAutoActivationRules(true, "({[;");
        AutoCompletion ac = new AutoCompletion(provider);
        ac.setAutoActivationEnabled(false);
        ac.install(transcriptionSyntaxTextArea);
    }

    public void refreshCodeCompletion() {
        List<String> tokens = getCompletionTokens();
        HashMap<String, Integer> types = new HashMap<>();
        for (String token: tokens){
            if (token.equals(""))
                continue;
            if (types.containsKey(token)){
                Integer v2 = types.get(token)+1;
                types.put(token, v2);
            } else {
                types.put(token, Integer.valueOf(1));
            }
        }
        provider.clear();
        for (Map.Entry<String, Integer> entry: types.entrySet()){
            // TODO: get weighted completion to work
            //provider.addCompletion(new WeightedCompletion(provider, entry.getKey(), entry.getValue()));
            provider.addCompletion(new BasicCompletion(provider, entry.getKey(), entry.getValue().toString()));
        }
    }

    private List<String> getCompletionTokens() {
        String s = transcriptionSyntaxTextArea.getText();
        s = s.replaceAll("\n", " ");
        return Arrays.asList(s.split(" "));
    }

    private void handle_listeners() {

        // TODO: Add all necessary listeners

        cropSelected.addActionListener(e -> owner.a_crop_selected(imagePanel.getCroppOfSelection()));
        inspectImage.addActionListener(e -> owner.a_inspect_selected_image());

        bt_zoomIn.addActionListener(e -> zoomIn());
        bt_zoomOut.addActionListener(e -> zoomOut());
        transcriptionSyntaxTextArea.getDocument().addDocumentListener(this);
        // TODO this is clearly double!
    }

    private void make_menubar() {

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menu: File
        menu_file = new JMenu("File");
        menuBar.add(menu_file);

        menuItem_file_newTranscription = new JMenuItem("Start New Transcription");
        menuItem_file_newTranscription.addActionListener(e -> owner.am_new_transcription());
        menuItem_file_newTranscription.setEnabled(false);
        menu_file.add(menuItem_file_newTranscription);

        menuItem_file_importXML = new JMenuItem("Import XML File");
        menuItem_file_importXML.addActionListener(e -> owner.am_import_xml());
        menuItem_file_importXML.setEnabled(false);
        menuItem_file_importXML.setAccelerator(KeyStroke.getKeyStroke("control shift I"));
        menu_file.add(menuItem_file_importXML);

        menuItem_file_importRaw = new JMenuItem("Import Raw Transcription File");
        menuItem_file_importRaw.addActionListener(e -> owner.am_import_raw());
        menuItem_file_importRaw.setEnabled(false);
        menuItem_file_importRaw.setAccelerator(KeyStroke.getKeyStroke("control I"));
        menu_file.add(menuItem_file_importRaw);

        menu_file.addSeparator();

        menuItem_file_save = new JMenuItem("Save");
        menuItem_file_save.addActionListener(e -> owner.am_save());
        menuItem_file_save.setEnabled(false);
        menuItem_file_save.setAccelerator(KeyStroke.getKeyStroke("control S"));
        menu_file.add(menuItem_file_save);

        menuItem_file_saveAs = new JMenuItem("Save As ...");
        menuItem_file_saveAs.addActionListener(e -> owner.am_save_as());
        menuItem_file_saveAs.setEnabled(false);
        menuItem_file_saveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
        menu_file.add(menuItem_file_saveAs);

        menuItem_exportXML = new JMenuItem("Export XML to File");
        menuItem_exportXML.addActionListener(e -> owner.am_export_xml());
        menuItem_exportXML.setEnabled(false);
        menuItem_exportXML.setAccelerator(KeyStroke.getKeyStroke("control E"));
        menu_file.add(menuItem_exportXML);

        menu_file.addSeparator();

        menuItem_file_close = new JMenuItem("Close Transcription");
        menuItem_file_close.addActionListener(e -> owner.am_close());
        menuItem_file_close.setEnabled(false);
        menu_file.add(menuItem_file_close);

        menu_file.addSeparator();

        menuItem_file_openFolder = new JMenuItem("Open Folder");
//        menuItem_file_openFolder.addActionListener(e -> owner.am_open_folder()); // TODO: still necessary?
        menuItem_file_openFolder.setEnabled(false);
        menu_file.add(menuItem_file_openFolder);


        // Menu: Edit
        menu_edit = new JMenu("Edit");
        menuBar.add(menu_edit);

        menuItem_edit_undo = new JMenuItem("Undo");
        menuItem_edit_undo.addActionListener(e -> {Log.log("Action: Undo");
            transcriptionSyntaxTextArea.undoLastAction();});
        menuItem_edit_undo.setEnabled(false);
        menuItem_edit_undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        menu_edit.add(menuItem_edit_undo);

        menuItem_edit_redo = new JMenuItem("Redo");
        menuItem_edit_redo.addActionListener(e -> {Log.log("Action: Redo");
            transcriptionSyntaxTextArea.redoLastAction();});
        menuItem_edit_redo.setEnabled(false);
        menuItem_edit_redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
        menu_edit.add(menuItem_edit_redo);

        menu_edit.addSeparator();

        menuItem_edit_loadImages = new JMenuItem("Load Images");
        menuItem_edit_loadImages.addActionListener(e -> owner.am_load_images());
        menu_edit.add(menuItem_edit_loadImages);

        menuItem_edit_inspectSelectedImage = new JMenuItem("Inspect Selected Image");
        menuItem_edit_inspectSelectedImage.addActionListener(e -> owner.a_inspect_selected_image());
        menu_edit.add(menuItem_edit_inspectSelectedImage);


        //Menu: Settings
        menu_settings = new JMenu("Settings");
        menuBar.add(menu_settings);
        //TODO actually implement settings
    }

    /**
     * Shows the GUI.
     * <p>
     *     Can be called after the constructor is done. This function will make the JFrame visible,
     *     maximize it and adjust the splitters to a reasonable position.
     * </p>
     */
    public void showMainGUI(){
        Log.log("Showing GUI.");

        pack();
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        Log.log("Is window displayable: "+isDisplayable());
        setPreferredSize(getSize());
        pack();
        mainPanel.setPreferredSize(getSize());
        refreshEnabledComponents();
        createThumbnails();
        SwingUtilities.invokeLater(() -> adjustSplitters());
    }

    public void createThumbnails() {
        if (pThumbnails == null) {
            return;
        }

        ArrayList<BufferedImage> images = owner.getLoadedImages();

        if (images.isEmpty()) {
            ThumbnailPanel thp = new ThumbnailPanel(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB), this);
            pThumbnails.add(thp);
            return;
        }

        pThumbnails.removeAll();

        for (BufferedImage im: images) {
            ThumbnailPanel tp = new ThumbnailPanel(im, this);
            tp.setActivated(tp.hasSamePicture(loadedImage));
            pThumbnails.add(tp);
        }
        repaint();
    }

    private void adjustSplitters() {
        if (splitpaneGeneral == null)
            return;
        Log.log("Adjusting Splitters.");
        pack();

        splitpaneGeneral.setDividerLocation(0.6);
        splitterXMLStuff.setDividerLocation(0.5);
        Log.log("Window size: "+getSize());
        Log.log(splitpaneGeneral.getSize());
        Log.log(splitpaneGeneral.getDividerLocation());
    }

    /**
     * Enables and disables all menu items according to how they should be.
     */
    public void refreshEnabledComponents(){
        if (menuBar == null){
            return;
        }

        menuItem_file_newTranscription.setEnabled(true);
        menuItem_file_importXML.setEnabled(false);
        menuItem_file_importRaw.setEnabled(true);
        menuItem_file_save.setEnabled(owner.hasUnsavedChanges() && Settings.hasCurrentRawFile());
        menuItem_file_saveAs.setEnabled(true);
        menuItem_exportXML.setEnabled(!xmlArea.getText().isEmpty());
        menuItem_file_close.setEnabled(false); // TODO: ?
        menuItem_file_openFolder.setEnabled(false); // TODO: ?
        menuItem_edit_undo.setEnabled(transcriptionSyntaxTextArea.canUndo());
        menuItem_edit_redo.setEnabled(transcriptionSyntaxTextArea.canRedo());
        menuItem_edit_loadImages.setEnabled(true);
        menuItem_edit_inspectSelectedImage.setEnabled(owner.getSelectedImage() != null);

        cropSelected.setEnabled(owner.getSelectedImage() != null);
        inspectImage.setEnabled(owner.getSelectedImage() != null);
        bt_zoomIn.setEnabled(owner.getSelectedImage() != null);
        bt_zoomOut.setEnabled(owner.getSelectedImage() != null);

        // TODO: settings
        // TODO: keep up to date
    }

    public void switchToImageView() {
        mainTabbedPane.setSelectedComponent(imageContainer);
    }

    public void displayImage(BufferedImage activatedImage) {
        if (activatedImage == null){
            picture.setIcon(null);
            imagePanel.setImage(null);
            loadedImage = null;
        } else {
            loadedImage = activatedImage;
            setImage();
            imagePanel.setImage(loadedImage);
        }
        //activateImageToolbarButtons();
    }

    private void setImage() {

        Rectangle viewRectOld = pictureScroller.getViewport().getViewRect();
        Dimension wholeSizeOld = picture.getSize();
        float xPart = (float) viewRectOld.x / (float)wholeSizeOld.width;
        float yPart = (float) viewRectOld.y / (float)wholeSizeOld.height;

        int w = (int) ((float)loadedImage.getWidth() * imageScaling);
        int h = (int) ((float)loadedImage.getHeight() * imageScaling);

        ImageIcon i = getScaledImage(loadedImage, w, h);
        picture.setIcon(i);

        Dimension wholeSizeNew = new Dimension(i.getIconWidth(), i.getIconHeight());

        int x = (int)((float)wholeSizeNew.width * xPart);
        int y = (int)((float)wholeSizeNew.height * yPart);
        Point newPos = new Point(x, y);

        pictureScroller.getViewport().setViewPosition(newPos);
    }

    private ImageIcon getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        // TODO: optimize image quality

        return new ImageIcon(resizedImg);
    }

    /**
     * Forces the GUI to close itself. Practically, that means disposing the frame, but not shutting down the app.
     */
    public void close() {
        this.dispose();
    }

    @Override
    public void log(String s) {
        logTextArea.append(Log.lineSep);
        logTextArea.append(s);
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosing(WindowEvent e) {
        Log.log("WindowClosing requested");
        owner.prepareShutDown();
    }
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}

    public void thumbnailRequestsActivation(ThumbnailPanel thumbnail) {
        owner.thumbnailRequestsActivation(thumbnail);
    }

    private void zoomOut() {
        imageScaling *= 0.9;
        setImage();
    }

    private void zoomIn() {
        imageScaling *= 1.1;
        setImage();
    }

    public void setRaw(List<String> lines) {
        // TODO: Store carret position

        StringBuffer sb = new StringBuffer();
        for (String l: lines) {
            sb.append(l);
            sb.append("\n");
        }
        transcriptionSyntaxTextArea.setText(sb.toString());
        // TODO: ensure it's undoable -> is it?
    }

    public String getTranscriptionString() {
        return transcriptionSyntaxTextArea.getText();
    }

    public void setXML(String string){
        int cp = xmlArea.getCaretPosition();
        int l = xmlArea.getText().length();
        float pos = (float)Math.max(1, cp) / Math.max(1, l);
        int cp_new = (int)(string.length()*pos);
        // TODO: Improve: have a carret-pos token in raw, and pass its position here
        xmlArea.setText(string);
        xmlArea.setCaretPosition(cp_new);
    }

    public String getXMLString() {
        return xmlArea.getText();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
//        owner.a_transcription_state_changed();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        //owner.a_transcription_state_changed();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        owner.a_transcription_state_changed();
    }
}
