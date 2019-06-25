package ch.blandolt.turboTranscriber.gui;

import ch.blandolt.turboTranscriber.core.TurboTranscribeCore;
import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.Loggable;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Main GUI class of TurboTranscriber
 *
 * @author Balduin Landolt
 */
public class MainGUI extends JFrame  implements Loggable, WindowListener {
    private JTabbedPane mainTabbedPane;
    private JPanel mainPanel;
    private JPanel logPane;
    private JTextArea logTextArea;
    private JScrollPane logScroller;
    private JPanel imageContainer;
    private JPanel controls;
    private JButton cropSelected;
    private JPanel thumbnailPanel;
    private JPanel transcriptionContainer;
    private JSplitPane splitpaneGeneral;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JSplitPane splitterXMLStuff;
    private JScrollPane styledScroller;

    private JLabel picture;
    private JScrollPane pictureScroller;
    private JToolBar imageToolBar;
    private JButton button1;
    private JButton button2;
    private RSyntaxTextArea transcriptionSyntaxTextArea;
    private RTextScrollPane syntaxScroller;
    private RTextScrollPane xmlScroller;
    private RSyntaxTextArea xmlArea;

    // Menubar
    private JMenuBar menuBar;
    private JMenu menu_file;
    private JMenuItem menuItem_file_newTranscription;
    private JMenuItem menuItem_file_importXML;
    private JMenuItem menuItem_file_importRaw;
    private JMenuItem menuItem_file_save;
    private JMenuItem menuItem_file_saveAs;
    private JMenuItem menuItem_file_close;
    private JMenuItem menuItem_file_openFolder;
    private JMenu menu_edit;
    private JMenuItem menuItem_edit_undo;
    private JMenuItem menuItem_edit_redo;
    private JMenuItem menuItem_edit_loadImages;
    private JMenu menu_settings;

    private TurboTranscribeCore owner;

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

        setContentPane(mainPanel);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        setMinimumSize(new Dimension(600, 400));

        // TODO: Remove. Test only
        transcriptionSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        // FixMe: Line numbers don't show.

        xmlArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);

        make_menubar();
        handle_listeners();

        Log.addLoggable(this);
        // TODO: moves this to core application?

        Log.log("GUI set up.");
    }

    private void handle_listeners() {

        // TODO: Add all necessary listeners

        cropSelected.addActionListener(e -> {
            // TODO: implement
        });
    }

    private void make_menubar() {
        // TODO: Add Listeners to Menu

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
        menu_file.add(menuItem_file_importXML);

        menuItem_file_importRaw = new JMenuItem("Import Raw Transcription File");
        menuItem_file_importRaw.addActionListener(e -> owner.am_import_raw());
        menuItem_file_importRaw.setEnabled(false);
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
        menuItem_edit_undo.addActionListener(e -> {Log.log("Action: Undo"); xmlArea.undoLastAction();});
        menuItem_edit_undo.setEnabled(false);
        menuItem_edit_undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        menu_edit.add(menuItem_edit_undo);

        menuItem_edit_redo = new JMenuItem("Redo");
        menuItem_edit_redo.addActionListener(e -> {Log.log("Action: Redo"); xmlArea.redoLastAction();});
        menuItem_edit_redo.setEnabled(false);
        menuItem_edit_redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
        menu_edit.add(menuItem_edit_redo);

        menu_edit.addSeparator();

        menuItem_edit_loadImages = new JMenuItem("Load Images");
        menuItem_edit_loadImages.addActionListener(e -> owner.am_load_images());
        menu_edit.add(menuItem_edit_loadImages);


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

        // TODO: create Thumbnails

        SwingUtilities.invokeLater(() -> {
            pack();
            setVisible(true);
            setExtendedState(MAXIMIZED_BOTH);
            Log.log("Is window displayable: "+isDisplayable());
        });
        SwingUtilities.invokeLater(() -> {
            setPreferredSize(getSize());
            pack();
            mainPanel.setPreferredSize(getSize());
        });
        SwingUtilities.invokeLater(() -> adjustSplitters());

    }

    private void adjustSplitters() {
        if (splitpaneGeneral == null)
            return;
        Log.log("Adjusting Splitters.");

        Log.log(getWidth());
        Log.log(mainPanel.getWidth());
        Log.log(mainTabbedPane.getWidth());
        Log.log(transcriptionContainer.getWidth());
        Log.log(splitpaneGeneral.getWidth());
        splitpaneGeneral.setDividerLocation(0.6);
        splitterXMLStuff.setDividerLocation(0.5);
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
}
