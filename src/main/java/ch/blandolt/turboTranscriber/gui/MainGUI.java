package ch.blandolt.turboTranscriber.gui;

import ch.blandolt.turboTranscriber.util.Log;
import ch.blandolt.turboTranscriber.util.Loggable;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame  implements Loggable {
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
    private JScrollPane xmlScroller;
    private JScrollPane styledScroller;

    private JLabel picture;
    private JScrollPane pictureScroller;
    private JToolBar imageToolBar;
    private JButton button1;
    private JButton button2;
    private RSyntaxTextArea transcriptionSyntaxTextArea;
    private RTextScrollPane syntaxScroller;

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

    public MainGUI(){

        // TODOs
        // -----
        //
        // TODO: test if images display correctly
        // TODO: finish transcription view
        //     - TODO: Add Image Panel to transcription view.
        // TODO: handle action listening

        super("Turbo Transcriber");

        Log.log("Setting up GUI...");

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));

        // TODO: Remove. Test only
        transcriptionSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);

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
//        menuItem_file_newTranscription.addActionListener(this);
//        menuItem_file_newTranscription.setActionCommand(ActionCommands.newTranscription);
        menuItem_file_newTranscription.setEnabled(false);
        menu_file.add(menuItem_file_newTranscription);

        menuItem_file_importXML = new JMenuItem("Import XML File");
//        menuItem_file_importXML.addActionListener(this);
//        menuItem_file_importXML.setActionCommand(ActionCommands.importXML);
        menuItem_file_importXML.setEnabled(false);
        menu_file.add(menuItem_file_importXML);

        menuItem_file_importRaw = new JMenuItem("Import Raw Transcription File");
//        menuItem_file_importRaw.addActionListener(this);
//        menuItem_file_importRaw.setActionCommand(ActionCommands.importRaw);
        menuItem_file_importRaw.setEnabled(false);
        menu_file.add(menuItem_file_importRaw);

        menu_file.addSeparator();

        menuItem_file_save = new JMenuItem("Save");
//        menuItem_file_save.addActionListener(this);
//        menuItem_file_save.setActionCommand(ActionCommands.save);
        menuItem_file_save.setEnabled(false);
        menuItem_file_save.setAccelerator(KeyStroke.getKeyStroke("control S"));
        menu_file.add(menuItem_file_save);

        menuItem_file_saveAs = new JMenuItem("Save As ...");
//        menuItem_file_saveAs.addActionListener(this);
//        menuItem_file_saveAs.setActionCommand(ActionCommands.saveAs);
        menuItem_file_saveAs.setEnabled(false);
        menuItem_file_saveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
        menu_file.add(menuItem_file_saveAs);

        menu_file.addSeparator();

        menuItem_file_close = new JMenuItem("Close Transcription");
//        menuItem_file_close.addActionListener(this);
//        menuItem_file_close.setActionCommand(ActionCommands.close);
        menuItem_file_close.setEnabled(false);
        menu_file.add(menuItem_file_close);

        menu_file.addSeparator();

        menuItem_file_openFolder = new JMenuItem("Open Folder");
//        menuItem_file_openFolder.addActionListener(this);
//        menuItem_file_openFolder.setActionCommand(ActionCommands.openFolder);
        menuItem_file_openFolder.setEnabled(false);
        menu_file.add(menuItem_file_openFolder);


        // Menu: Edit
        menu_edit = new JMenu("Edit");
        menuBar.add(menu_edit);

        menuItem_edit_undo = new JMenuItem("Undo");
//        menuItem_edit_undo.addActionListener(this);
//        menuItem_edit_undo.setActionCommand(ActionCommands.undo);
        menuItem_edit_undo.setEnabled(false);
        menuItem_edit_undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
        menu_edit.add(menuItem_edit_undo);

        menuItem_edit_redo = new JMenuItem("Redo");
//        menuItem_edit_redo.addActionListener(this);
//        menuItem_edit_redo.setActionCommand(ActionCommands.redo);
        menuItem_edit_redo.setEnabled(false);
        menuItem_edit_redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
        menu_edit.add(menuItem_edit_redo);

        menu_edit.addSeparator();

        menuItem_edit_loadImages = new JMenuItem("Load Images");
//        menuItem_edit_loadImages.addActionListener(this);
//        menuItem_edit_loadImages.setActionCommand(ActionCommands.loadImages);
        menu_edit.add(menuItem_edit_loadImages);


        //Menu: Settings
        menu_settings = new JMenu("Settings");
        menuBar.add(menu_settings);
        //TODO actually implement settings
    }

    public void showMainGUI(){
        // TODO: create Thumbnails

        pack();
        setExtendedState(MAXIMIZED_BOTH);

        setVisible(true);

        adjustSplitters();

        Log.log("Showing GUI.");
    }

    private void adjustSplitters() {
/*        if (splitPane_general == null)
            return;

        splitPane_general.setDividerLocation(0.6);
        splitPane_xmlStuff.setDividerLocation(0.5);
        splitPane_picVsTanscription.setDividerLocation(0.2);
        splitPane_transVsRaw.setDividerLocation(0.4);*/
    }

    @Override
    public void log(String s) {
        logTextArea.append(Log.lineSep);
        logTextArea.append(s);
        logTextArea.setCaretPosition(logTextArea.getText().length());
    }
}
