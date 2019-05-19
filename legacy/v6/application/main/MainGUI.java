/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.application.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.ActionCommands;
import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.CustomImagePanel;
import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.Log;
import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.Loggable;
import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.ThumbnailPanel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * @author Balduin Landolt
 *
 */
@SuppressWarnings("serial")
public class MainGUI extends JFrame implements ActionListener, WindowListener, ComponentListener, Loggable {
	
	//TODO add div. shortcuts, like shift+enter for pagebreak, alt+enter for columnbreak, etc.
	
	//TODO add option to load custom tag shortcuts
	//TODO add option to modify tag shortcuts in program
	
	private SpeedUP parent;
	private float imageScaling = 0.3f;
	private BufferedImage loadedImage;

	private NewWordAction newWordAction = new NewWordAction();
	private TabAction tabAction = new TabAction();
	private PasteAction pasteAction = new PasteAction();
	private CutAction cutAction = new CutAction();
	
	private JPanel contentPane;
	
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
	
	private JTabbedPane tabs;
	private String tab_transcriptionView = "Transcription";
	private String tab_imageView = "Image";
	private String tab_log = "Event Log (can be ignored)";
	private JSplitPane splitPane_general;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JSplitPane splitPane_xmlStuff;
	private JScrollPane scroller_xml;
	private JScrollPane scroller_stylesheet;
	
	private JTextPane xmlTextPane;
	private JTextPane rawTextPane;
	private JFXPanel fxPanel;
	private WebView browserComponent;
	
	private JLabel picture;
	private JPanel transcription;
	private JScrollPane scroller_picture;
	private JScrollPane scroller_raw;
	private JSplitPane splitPane_picVsTanscription;
	private JSplitPane splitPane_transVsRaw;
	private JTextField transcriptionInput;
	private JButton button_enter;
	private JToolBar imageToolBar;
	private JButton button_zoomIn;
	private JButton button_zoomOut;
	
	private CustomImagePanel imagePanel;
	private JButton cropSelectedImagePart;
	private JScrollPane scroller_thumbnail;
	private JPanel panelThumbnails;
	
	private JMenu menu_settings;
	
	private JTextArea logPane;
	
	/**
	 * Create the frame.
	 * @param parent 
	 */
	public MainGUI(SpeedUP parent) {
		this.parent = parent;
		
		initializeGeneral();
		makeMenu();
		makeBody();
		splitPane_xmlStuff.setDividerLocation(400);
		splitPane_transVsRaw.setDividerLocation(150);
		splitPane_picVsTanscription.setDividerLocation(180);
	}

	private void initializeGeneral() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		addComponentListener(this);
		setBounds(100, 100, 450, 300);
		setTitle("SpeedUP XML Transcription Aid.");
		setMinimumSize(new Dimension(800, 600));
//		HashSet<AWTKeyStroke> set = new HashSet<AWTKeyStroke>(getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
//		set.clear();
//		set.add(KeyStroke.getKeyStroke("control TAB"));
//		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, set);
//		setFocusTraversalKeysEnabled(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

	/**
	 * 
	 */
	private void makeBody() {
		tabs = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabs, BorderLayout.CENTER);
		
		makeTranscriptionView();
		makeImageView();
		makeLogView();
	}

	/**
	 * 
	 */
	private void makeLogView() {
		logPane = new JTextArea();
		JScrollPane scroller = new JScrollPane(logPane);
		tabs.addTab(tab_log, scroller);
	}

	/**
	 * 
	 */
	private void makeImageView() {
		JPanel p = new JPanel(new BorderLayout());
		tabs.addTab(tab_imageView, p);
		
		JPanel p_forImage = new JPanel(new BorderLayout());
		p.add(p_forImage, BorderLayout.CENTER);
		imagePanel = new CustomImagePanel(parent);
		p_forImage.add(imagePanel, BorderLayout.CENTER);
		
		JPanel pControls = new JPanel();
		pControls.setLayout(new BoxLayout(pControls, BoxLayout.Y_AXIS));
		p.add(pControls, BorderLayout.EAST);
		
		cropSelectedImagePart = new JButton("Crop Selection");
		cropSelectedImagePart.addActionListener(this);
		cropSelectedImagePart.setActionCommand(ActionCommands.cropSeletedArea);
		pControls.add(cropSelectedImagePart);
		
		panelThumbnails = new JPanel();
		scroller_thumbnail = new JScrollPane(panelThumbnails);
		scroller_thumbnail.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroller_thumbnail.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		p.add(scroller_thumbnail, BorderLayout.SOUTH);
		createThumbnails();
	}

	private void makeTranscriptionView() {
		leftPanel = new JPanel(new BorderLayout());
		rightPanel = new JPanel(new BorderLayout());
		splitPane_general = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
		splitPane_general.setDividerLocation(1000);
		tabs.addTab(tab_transcriptionView, splitPane_general);

		scroller_xml = new JScrollPane();
		scroller_stylesheet = new JScrollPane();
		splitPane_xmlStuff = new  JSplitPane(JSplitPane.VERTICAL_SPLIT, scroller_xml, scroller_stylesheet);
		rightPanel.add(splitPane_xmlStuff, BorderLayout.CENTER);
		
		transcriptionInput = new JTextField();
		transcriptionInput.setActionCommand(ActionCommands.lineEntered);
		transcriptionInput.addActionListener(this);
		transcriptionInput.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "newWord");
		transcriptionInput.getActionMap().put("newWord", newWordAction);
		transcriptionInput.getInputMap().put(KeyStroke.getKeyStroke("control TAB"), "tab");//TODO can I make this tab???
		transcriptionInput.getActionMap().put("tab", tabAction);
		transcriptionInput.getInputMap().put(KeyStroke.getKeyStroke("control V"), "paste");
		transcriptionInput.getActionMap().put("paste", pasteAction);
		transcriptionInput.getInputMap().put(KeyStroke.getKeyStroke("control X"), "cut");
		transcriptionInput.getActionMap().put("cut", cutAction);
		transcriptionInput.setFont(transcriptionInput.getFont().deriveFont(20f));
		transcription = new JPanel(new BorderLayout());
		JPanel p = new JPanel(new BorderLayout());
		p.add(transcriptionInput, BorderLayout.CENTER);
		button_enter = new JButton("go");
		button_enter.setActionCommand(ActionCommands.lineEntered);
		button_enter.addActionListener(this);
		p.add(button_enter, BorderLayout.EAST);
		transcription.add(p, BorderLayout.SOUTH);
		scroller_raw = new JScrollPane();
		splitPane_transVsRaw = new  JSplitPane(JSplitPane.VERTICAL_SPLIT, transcription, scroller_raw);
		p = new JPanel(new BorderLayout());
		picture = new JLabel();
		scroller_picture = new JScrollPane(picture);
		imageToolBar = new JToolBar(JToolBar.VERTICAL);
		imageToolBar.setLayout(new GridLayout(0, 1));
		imageToolBar.setEnabled(false);
		button_zoomIn = new JButton("+");
		button_zoomIn.setPreferredSize(new Dimension(30, 30));
		button_zoomIn.addActionListener(this);
		button_zoomIn.setActionCommand(ActionCommands.zoomIn);
		button_zoomOut = new JButton("-");
		button_zoomOut.setPreferredSize(new Dimension(30, 30));
		button_zoomOut.addActionListener(this);
		button_zoomOut.setActionCommand(ActionCommands.zoomOut);
		imageToolBar.add(button_zoomIn);
		imageToolBar.add(button_zoomOut);
		p.add(scroller_picture, BorderLayout.CENTER);
		p.add(imageToolBar, BorderLayout.EAST);
		splitPane_picVsTanscription = new  JSplitPane(JSplitPane.VERTICAL_SPLIT, p, splitPane_transVsRaw);
		leftPanel.add(splitPane_picVsTanscription, BorderLayout.CENTER);
		
		xmlTextPane = new JTextPane();
		TabStop[] tt = new TabStop[10];
		for (int i=0; i<tt.length; i++) {tt[i] = new TabStop(30*i);}
		TabSet tabset = new TabSet(tt);
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet attr = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.TabSet, tabset);
		xmlTextPane.setParagraphAttributes(attr, false);
		xmlTextPane.setEditable(false);
		scroller_xml.setViewportView(xmlTextPane);
		
		rawTextPane = new JTextPane();
		rawTextPane.setEditable(false);
		scroller_raw.setViewportView(rawTextPane);
		
		fxPanel = new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initFX();
            }

			private void initFX() {
		        Stage stage = new Stage();  
		        Group root = new Group();  
		        Scene scene = new Scene(root,80,20);  
		        stage.setScene(scene); 
		        browserComponent = new WebView();
		        WebEngine webEngine = browserComponent.getEngine();
		        webEngine.loadContent("<b>Sylesheet View here</b>");
		        
		        ObservableList<Node> children = root.getChildren();
		        children.add(browserComponent);                     
		         
		        fxPanel.setScene(scene); 
			}
        });
		scroller_stylesheet.setViewportView(fxPanel);
	}


	private void makeMenu() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// Menu: File
		menu_file = new JMenu("File");
		menuBar.add(menu_file);

		menuItem_file_newTranscription = new JMenuItem("Start New Transcription");
		menuItem_file_newTranscription.addActionListener(this);
		menuItem_file_newTranscription.setActionCommand(ActionCommands.newTranscription);
		menuItem_file_newTranscription.setEnabled(false);
		menu_file.add(menuItem_file_newTranscription);
		
		menuItem_file_importXML = new JMenuItem("Import XML File");
		menuItem_file_importXML.addActionListener(this);
		menuItem_file_importXML.setActionCommand(ActionCommands.importXML);
		menuItem_file_importXML.setEnabled(false);
		menu_file.add(menuItem_file_importXML);
		
		menuItem_file_importRaw = new JMenuItem("Import Raw Transcription File");
		menuItem_file_importRaw.addActionListener(this);
		menuItem_file_importRaw.setActionCommand(ActionCommands.importRaw);
		menuItem_file_importRaw.setEnabled(false);
		menu_file.add(menuItem_file_importRaw);
		
		menu_file.addSeparator();
		
		menuItem_file_save = new JMenuItem("Save");
		menuItem_file_save.addActionListener(this);
		menuItem_file_save.setActionCommand(ActionCommands.save);
		menuItem_file_save.setEnabled(false);
		menuItem_file_save.setAccelerator(KeyStroke.getKeyStroke("control S"));
		menu_file.add(menuItem_file_save);
		
		menuItem_file_saveAs = new JMenuItem("Save As ...");
		menuItem_file_saveAs.addActionListener(this);
		menuItem_file_saveAs.setActionCommand(ActionCommands.saveAs);
		menuItem_file_saveAs.setEnabled(false);
		menuItem_file_saveAs.setAccelerator(KeyStroke.getKeyStroke("control shift S"));
		menu_file.add(menuItem_file_saveAs);
		
		menu_file.addSeparator();
		
		menuItem_file_close = new JMenuItem("Close Transcription");
		menuItem_file_close.addActionListener(this);
		menuItem_file_close.setActionCommand(ActionCommands.close);
		menuItem_file_close.setEnabled(false);
		menu_file.add(menuItem_file_close);
		
		menu_file.addSeparator();
		
		menuItem_file_openFolder = new JMenuItem("Open Folder");
		menuItem_file_openFolder.addActionListener(this);
		menuItem_file_openFolder.setActionCommand(ActionCommands.openFolder);
		menuItem_file_openFolder.setEnabled(false);
		menu_file.add(menuItem_file_openFolder);
		
		
		// Menu: Edit
		menu_edit = new JMenu("Edit");
		menuBar.add(menu_edit);

		menuItem_edit_undo = new JMenuItem("Undo");
		menuItem_edit_undo.addActionListener(this);
		menuItem_edit_undo.setActionCommand(ActionCommands.undo);
		menuItem_edit_undo.setEnabled(false);
		menuItem_edit_undo.setAccelerator(KeyStroke.getKeyStroke("control Z"));
		menu_edit.add(menuItem_edit_undo);

		menuItem_edit_redo = new JMenuItem("Redo");
		menuItem_edit_redo.addActionListener(this);
		menuItem_edit_redo.setActionCommand(ActionCommands.redo);
		menuItem_edit_redo.setEnabled(false);
		menuItem_edit_redo.setAccelerator(KeyStroke.getKeyStroke("control Y"));
		menu_edit.add(menuItem_edit_redo);
		
		menu_edit.addSeparator();

		menuItem_edit_loadImages = new JMenuItem("Load Images");
		menuItem_edit_loadImages.addActionListener(this);
		menuItem_edit_loadImages.setActionCommand(ActionCommands.loadImages);
		menu_edit.add(menuItem_edit_loadImages);
		
		
		//Menu: Settings
		menu_settings = new JMenu("Settings");
		menuBar.add(menu_settings);
		//TODO actually implement settings
	}

	/**
	 * 
	 */
	public void showGUI() {
		Log.log("Showing GUI.");
		this.setVisible(true);
		adjustSplitters();
		//setFocusTraversalKeysEnabled(false);
	}

	/**
	 * 
	 */
	private void adjustSplitters() {
		if (splitPane_general == null)
			return;
		
		splitPane_general.setDividerLocation(0.6);
		splitPane_xmlStuff.setDividerLocation(0.5);
		splitPane_picVsTanscription.setDividerLocation(0.2);
		splitPane_transVsRaw.setDividerLocation(0.4);
	}

	/**
	 * 
	 */
	public void maximize() {
		Log.log("Maximizing GUI.");
		this.setExtendedState(MAXIMIZED_BOTH);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Log.log("Got Action: "+e.getActionCommand());
		
		switch (e.getActionCommand()) {
			case ActionCommands.close:
				parent.closeProject();
				break;

			case ActionCommands.importRaw:
				parent.importRaw();
				break;

			case ActionCommands.importXML:
				parent.importXML();
				break;

			case ActionCommands.newTranscription:
				parent.newTranscription();
				break;

			case ActionCommands.openFolder:
				parent.openFolder();
				break;

			case ActionCommands.redo:
				parent.redo();
				break;

			case ActionCommands.undo:
				parent.undo();
				break;

			case ActionCommands.save:
				parent.save();
				break;

			case ActionCommands.saveAs:
				parent.saveAs();
				break;

			case ActionCommands.loadImages:
				loadImages();
				break;

			case ActionCommands.lineEntered:
				lineEntered();
				break;

			case ActionCommands.previewContentChanged:
				correctSpaces();
				parent.updateLinePreview(transcriptionInput.getText());
				break;

			case ActionCommands.CtrlTabPressed:
				correctSpaces();
				parent.ctrlTabPressed(transcriptionInput.getText());
				break;

			case ActionCommands.zoomIn:
				zoomIn();
				break;

			case ActionCommands.zoomOut:
				zoomOut();
				break;

			case ActionCommands.cropSeletedArea:
				parent.addCroppedImage(imagePanel.getCroppOfSelection());
				break;
			
			default:
				break;
		}
	}
	
	/**
	 * 
	 */
	private void correctSpaces() {
		transcriptionInput.setText(parent.correctSpaces(transcriptionInput.getText()));
	}

	private ImageIcon getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return new ImageIcon(resizedImg);
	}
	
	private void setImage() {
		
		Rectangle viewRectOld = scroller_picture.getViewport().getViewRect();
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
		
		scroller_picture.getViewport().setViewPosition(newPos);
	}

	/**
	 * 
	 */
	private void zoomOut() {
		imageScaling *= 0.9;
		setImage();
	}

	/**
	 * 
	 */
	private void zoomIn() {
		imageScaling *= 1.1;
		setImage();
	}

	/**
	 * 
	 */
	private void loadImages() {
		parent.loadImages();
	}

	/**
	 * 
	 */
	private void lineEntered() {
		String line = transcriptionInput.getText();
		transcriptionInput.setText("");
		parent.addLine(line);
		//TODO scroll image a little
		
		Point p = scroller_picture.getViewport().getViewPosition();
		p.y += 50;
		scroller_picture.getViewport().setViewPosition(p);
	}

	/**
	 * @param projectState 
	 * 
	 */
	public void activateProjectSpecific() {
		menuItem_file_newTranscription.setEnabled(true);
		menuItem_file_importXML.setEnabled(true);
		menuItem_file_importRaw.setEnabled(true);
		menuItem_edit_loadImages.setEnabled(true);
		picture.setEnabled(true);
		
		button_enter.setEnabled(parent.hasProject());
		menuItem_file_close.setEnabled(parent.hasProject());
		transcriptionInput.setEnabled(parent.hasProject());
		menuItem_file_save.setEnabled(parent.hasUnsavedChanges());
		menuItem_file_saveAs.setEnabled(parent.hasProject());
		menuItem_file_openFolder.setEnabled(parent.getCurrentLocation()!=null);
		tabs.setEnabledAt(tabs.indexOfTab(tab_imageView), loadedImage!=null);
		if(!tabs.isEnabledAt(tabs.getSelectedIndex()))
			tabs.setSelectedIndex(0);
		menuItem_edit_undo.setEnabled(parent.canUndo());
		menuItem_edit_redo.setEnabled(parent.canRedo());
		activateImageToolbarButtons();
	}

	private void activateImageToolbarButtons() {
		boolean flag = (loadedImage!=null);
		button_zoomIn.setEnabled(flag);
		button_zoomOut.setEnabled(flag);
	}

	private class NewWordAction extends AbstractAction {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ActionEvent e = new ActionEvent(transcriptionInput, ActionEvent.ACTION_PERFORMED, ActionCommands.previewContentChanged);
			Log.log("Action was called: Space");
			MainGUI.this.actionPerformed(e);
		}
		
	}

	private class TabAction extends AbstractAction {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ActionEvent e = new ActionEvent(transcriptionInput, ActionEvent.ACTION_PERFORMED, ActionCommands.CtrlTabPressed);
			Log.log("Action was called: Tab");
			MainGUI.this.actionPerformed(e);
		}
		
	}

	private class PasteAction extends AbstractAction {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent arg) {
			Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
			if (t == null )
				return;
			if (!t.isDataFlavorSupported(DataFlavor.stringFlavor))
				return;
			
			try {
				String s = (String) t.getTransferData(DataFlavor.stringFlavor);
				Log.log("Paste: "+s);

				StringBuffer b = new StringBuffer(transcriptionInput.getText());
				int pos = transcriptionInput.getCaretPosition();
				String selection = transcriptionInput.getSelectedText();
				if (selection != null && !selection.isEmpty()) {
					b.replace(transcriptionInput.getSelectionStart(), transcriptionInput.getSelectionEnd(), s);
				} else {
					b.insert(pos, s);
				}
				transcriptionInput.setText(b.toString());
				transcriptionInput.setCaretPosition(pos+s.length());

				ActionEvent e = new ActionEvent(transcriptionInput, ActionEvent.ACTION_PERFORMED, ActionCommands.previewContentChanged);
				MainGUI.this.actionPerformed(e);
				
			} catch (UnsupportedFlavorException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	private class CutAction extends AbstractAction {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent arg) {
			if (transcriptionInput.getText().isEmpty())
				return;
			
			String selection = transcriptionInput.getSelectedText();
			int selectionIndex = transcriptionInput.getSelectionStart();
			
			if (selection == null || selection.isEmpty())
				return;
			Log.log("Cut: "+selection);
			
			StringBuffer b = new StringBuffer(transcriptionInput.getText());
			b.delete(selectionIndex, selectionIndex+selection.length());
			transcriptionInput.setText(b.toString());
			transcriptionInput.setCaretPosition(selectionIndex);
			
			StringSelection stringSel = new StringSelection(selection);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSel, stringSel);
			
		}
		
	}

	/**
	 * @param im
	 */
	public void sendImage(BufferedImage im) {
		if (im == null){
			picture.setIcon(null);
			imagePanel.setImage(null);
			loadedImage = null;
		} else {
			loadedImage = im;
			setImage();
			imagePanel.setImage(loadedImage);
		}
		activateImageToolbarButtons();
	}

	/**
	 * 
	 */
	void switchToImageView() {
		tabs.setSelectedIndex(tabs.indexOfTab(tab_imageView));
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent arg0) {	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent arg0) {	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		Log.log("WindowClosing requested");
		parent.prepareShutDown();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent arg0) { }

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		adjustSplitters();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent arg0) { }

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent arg0) { }

	/**
	 * 
	 */
	public void close() {
		this.dispose();
	}

	/**
	 * 
	 */
	public void createThumbnails() {
		ArrayList<BufferedImage> images = parent.getLoadedImages();
		
		if (images.isEmpty()) {
			panelThumbnails.add(new ThumbnailPanel(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB), this));
			return;
		}
		
		panelThumbnails.removeAll();
		
		for (BufferedImage im: images) {
			ThumbnailPanel tp = new ThumbnailPanel(im, this);
			tp.setActivated(tp.hasSamePicture(loadedImage));
			panelThumbnails.add(tp);
		}
		repaint();
	}

	/**
	 * @param thumbnailPanel
	 */
	public void thumbnailRequestsActivation(ThumbnailPanel tp) {
		parent.thumbnailRequestsActivation(tp);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentMoved(ComponentEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent arg0) {
		adjustSplitters();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentShown(ComponentEvent arg0) {}

	/**
	 * @param xmlAsString
	 */
	public void setXMLText(String xmlString) {
		xmlTextPane.setText(xmlString);
	}

	/**
	 * @param rawAsString
	 */
	public void setRawText(String rawString) {
		rawTextPane.setText(rawString);
	}

	/**
	 * 
	 */
	public void updateStylesheetView() {
		if (browserComponent == null)
			return;
		
		String html = parent.getHTMLRepresentation();
		WebEngine engine = browserComponent.getEngine();
		//engine.setUserStyleSheetLocation(applicableStylesheetLocation());
		Platform.runLater(() -> engine.loadContent(html));
	}

	/**
	 * @return
	 */
	private String applicableStylesheetLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ch.unibas.landolt.balduin.XMLSpeedupV5.src.util.Logable#log(java.lang.String)
	 */
	@Override
	public void log(String s) {
		logPane.append(Log.lineSep);
		logPane.append(s);
		logPane.setCaretPosition(logPane.getText().length());
	}


	
}
