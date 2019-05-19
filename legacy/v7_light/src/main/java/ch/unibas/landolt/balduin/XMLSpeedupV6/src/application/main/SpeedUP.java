/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.application.main;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.*;
import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.TagShortcuts.shortcutType;
import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure.*;

/**
 * Main Class of the XMLSpeedUP program. <br/>
 * By Creating and running an instance of this Class, the whole Program is run.
 * 
 * @author Balduin Landolt
 *
 */
public class SpeedUP {
	private MainGUI gui;
	
	private LinkedList<MainDataSet> dataStages;
	private int dataStageIndex;
	private MainDataSet currentData;

	private boolean unsavedChanges = false;
	

	/**
	 * 
	 */
	public SpeedUP() {
		super();
		initializeLog();
		Log.log("Program started. Log created.");
		dataStages = new LinkedList<MainDataSet>();
		dataStageIndex = 0;
		currentData = new MainDataSet(this);
		addDataStage();
		Log.log("Created Data-Stage Infrastructure.");
		Log.log("Loading program preferences...");
		loadPreferences();
		Log.log("Preferences loaded.");
		Log.log("Loading exception lists...");
		loadSpecialCases();
		Log.log("Lists loaded.");
		
		
		Log.log("Initializing GUI...");
		initializeGUI();
		Log.addLoggable(gui);
		Log.log("GUI inizitialized.");
		Log.log();
	}

	/**
	 * 
	 */
	private void loadSpecialCases() {
		// TODO More? - definitely abbreviations
		TagShortcuts.load(getClass().getClassLoader().getResourceAsStream("standard_tags.txt"), shortcutType.TAG_SHORTCUT);
		TagShortcuts.load(getClass().getClassLoader().getResourceAsStream("standard_tag_always_empty.txt"), shortcutType.TAG_ALLWAYS_EMPTY);
	}

	/**
	 * 
	 */
	private void loadPreferences() {
		// TODO Auto-generated method stub
		Log.log("!!!not yet implemented!!!");
	}

	/**
	 * 
	 */
	public void addDataStage() {
		try {
			if (dataStageIndex == 0) {
				dataStages.addFirst(currentData);
			} else {
				while (dataStageIndex > 0 && !dataStages.isEmpty()) {
					dataStageIndex--;
					dataStages.removeFirst();
				}
				dataStages.addFirst(currentData);
			}
			while (dataStages.size() > 10000) {
				dataStages.removeLast();
			}
			currentData = currentData.clone();
			refreshGUI();
		} catch (Exception e) {
			e.printStackTrace();
			Log.log("Error: Failed to create Data-Stage.");
			Log.err(e);
		}
	}

	/**
	 * 
	 */
	private void initializeGUI() {
		gui = new MainGUI(this);
	}

	/**
	 * 
	 */
	private void initializeLog() {
		Log.initialize();
	}

	/**
	 * 
	 */
	public void run() {
		Log.log("Running Application...");
		showGUI();
		Log.log("Application launched.");
		Log.log();
	}

	/**
	 * 
	 */
	private void showGUI() {
		gui.showGUI();
		gui.maximize();
		refreshGUI();
	}

	/**
	 * 
	 */
	private void refreshGUI() {
		if (gui == null)
			return;
		
		
		Log.log("Refreshing GUI content.");
		
		gui.sendImage(currentData.getActivatedImage());
		gui.createThumbnails();
		//light
		//gui.activateProjectSpecific();
		
		gui.setXMLText(getXMLAsString());
		gui.setRawText(getRawAsString());
		gui.updateStylesheetView(); //TODO can I update it that often, or is it too slow?
	}

	/**
	 * @return
	 */
	private String getRawAsString() {
		StringBuilder b = new StringBuilder();
		
		for (String s: currentData.getRawList()) {
			b.append(s);
			b.append(Log.lineSep);
		}
		
		return b.toString();
	}

	/**
	 * @return
	 */
	private String getXMLAsString() {
		Document d = currentData.getJDOMDocument();
		if (d == null)
			return "";
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		XMLOutputter out = new XMLOutputter(format);
		String res = out.outputString(d);
		res = makeWordsOneLine(res, format);
		return res;
	}

	/**
	 * @param res
	 * @return
	 */
	private String makeWordsOneLine(String in, Format f) {
		//TODO make this only when necessary (see special cases where this should be suppressed, or other formats like menota
		Log.log("Getting Words in Line.");
		
		String lineSep = f.getLineSeparator();
		String indent = f.getIndent();
		String res = in;
		String r = "\\<w\\>.*?\\</w\\>";

		Pattern regex = Pattern.compile(r, Pattern.DOTALL);
		Matcher matcher = regex.matcher(in);
		

		while (matcher.find()){
			String hit = matcher.group();
			String hitCorr = hit;
			hitCorr = hitCorr.replace(lineSep, "");
			hitCorr = hitCorr.replace(indent, "");
			res = res.replace(hit, hitCorr);
			Log.log("Replaced '"+hit+"' with '"+hitCorr+"'");
		}
		
		return res;
	}

	/**
	 * 
	 */
	public void loadImages() {
		Log.log("Loading Images.");
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
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

	/**
	 * @param ff
	 */
	private void loadImages(File[] ff) {
		LinkedList<BufferedImage> images = new LinkedList<BufferedImage>();
		for (File f: ff) {
			BufferedImage im = loadImage(f);
			if (im != null) {
				images.add(im);
			}
		}
		Log.log("Loaded Images: "+images.size());
		for (BufferedImage img: images) {
			currentData.addLoadedImage(img);
		}
		currentData.setActivatedImage(images.getFirst());
		addDataStage();
		gui.switchToImageView();
	}

	/**
	 * @param f
	 */
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
	 * @return
	 */
	public boolean hasProject() {
		boolean res = (currentData.getXMLFile()!=null || currentData.getRawFile()!=null);//is that all?
		return res;
	}

	/**
	 * @param croppOfSelection
	 */
	public void addCroppedImage(BufferedImage croppOfSelection) {
		currentData.addLoadedImage(croppOfSelection);
		currentData.setActivatedImage(croppOfSelection);
		addDataStage();
	}

	/**
	 * 
	 */
	public void importRaw() {
		Log.log("Importing Raw Transcription...");
		
		try {
			askToSave();
			closeProject();

			File f = getFileRaw();
			//askToOverwrite(f);
			
			currentData.setRawFile(f);
			saveAsForXML();
			
			createRawStructure();
			createXMLStructure();
			createInternalStructure();
			
			
			
			List<String> l = Files.readAllLines(f.toPath());
			LinkedList<String> lines = new LinkedList<>();
			int lineNumberOnPage=1;
			int lineInStanza=1;
			int stanzaNo=1;
			
			for (String s: l) {
				if (s.isEmpty())
					continue;
				Log.log("Read Line in File: "+s);
				String corr = correctSpaces(s);

				if (corr.startsWith("[head]")) {
					corr = corr+"[/head]";
				} else if (corr.startsWith("[lg")) {
					if (stanzaNo>1)
						lines.add("[/lg]");
					stanzaNo++;
					lineInStanza=1;
				} else if (corr.startsWith("[pb")) {
					lineNumberOnPage=1;
				} else if (corr.equals("[lb]")) {
					//
				} else {//TODO potentially add more possible line starts to be handled differently
					corr = "[l="+lineInStanza+"] "+corr+"[/l]";
					lineInStanza++;
				}
				
				if (corr.contains("[lb]")) {
					corr = corr.replace("[lb]", "[lb="+lineNumberOnPage+"]");
					lineNumberOnPage++;
				}

				corr = corr.replace(")", ";)");
				
				corr = corr.replace("&", "{amp}");
				corr = corr.replace("{s}", "{slong}");
				corr = corr.replace("{f}", "{fins}");
				
				corr = correctSpaces(corr);
				lines.add(corr);
				Log.log("Corrected Line to: "+corr);
			}
			
			for (String s: lines) {
				processLine(s);
				Log.log("actually XML-ed line: "+s);
			}
			
			saveForXML();
			Log.log("Saved XML to disk");
			
			//addDataStage();
			
		} catch (AbortException e) {
			Log.log("Aborted: " + e.getMessage());
		} catch (IOException e) {
			Log.log("Failed to Import Raw: "+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void importXML() {
		Log.log("Importing XML Transcription...");
		
		try {
			askToSave();
			closeProject();

			File f = getFileRaw();
			//askToOverwrite(f);

			//TODO import from File, create Raw
			Log.log("!!!!! NOT YET IMPLEMENTED !!!!!");
			
		} catch (AbortException e) {
			Log.log("Aborted: " + e.getMessage());
		}
	}

	/**
	 * @param corr
	 */
	private void processLine(String line) {
		if (line == null || line.isEmpty())
			return;
		
		line = line.trim();
		
		currentData.addLineRaw(line);
		
		Log.log("Processing Line: "+line);
		String[] elements = line.split(" ");
		
		for (String e: elements) {
			processElement(e);
		}
	}

	/**
	 * @param e
	 */
	private void processElement(String e) {
		try {
			e = e.trim();
			if (e.isEmpty())
				return;
			
			Log.log("Processing Element: "+e);
			
			AbstractTranscriptionObject o = AbstractTranscriptionObject.getObject(e);
			if (o ==null) {
				Log.log("Produced 'null'-Element.");
				return;
			}
			currentData.addTranscriptionElement(o);
			if (o instanceof TagCloseElement)
				currentData.closeTag();
			else
				currentData.insertXMLContent(o.getXMLRepresentation());
			
			if (o instanceof Tag) {
				Tag t = (Tag) o;
				if (t.isEmpty()) {
					TagCloseElement close = new TagCloseElement("/"+t.getTagName());//TODO is that even necessary?
					currentData.addTranscriptionElement(close);
					//currentData.closeTag();
				} else {
					currentData.setIndexIntoElement(t.getXMLRepresentation().get(0));
				}
			}
			
			//TODO implement here, in which element we are with the following - should be done, I think
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.err(ex);
		}
	}

	/**
	 * 
	 */
	public void newTranscription() {
		Log.log("New transcription...");
		
		try {
			askToSave();
			File f = getFileRaw();

			String fileName = f.getName();
			if (fileName.contains(".")) {
				fileName = fileName.split("\\.")[0];
			}
			File rawFile = new File(f.getParentFile(), fileName+".txt");
			File xmlFile = new File(f.getParentFile(), fileName+".xml");

			askToOverwrite(rawFile);
			askToOverwrite(xmlFile);
			
			closeProject();
			
			Log.log("Raw: "+rawFile);
			Log.log("XML: "+xmlFile);
			
			currentData.setCurrentLocation(f.getParentFile());
			currentData.setRawFile(rawFile);
			currentData.setXMLFile(xmlFile);
			
			createXMLStructure();
			createRawStructure();
			
			createInternalStructure();
			unsavedChanges = true;

			Log.log("Created new Transcription.");
			Log.log();
			addDataStage();
			
		} catch (AbortException e1) {
			Log.log("Aborted: " + e1.getMessage());
		} catch (IOException e) {
			Log.log("Failed to create Files.");
		}
	}

	/**
	 * 
	 */
	private void createInternalStructure() {
		//TODO anything?
	}

	/**
	 * 
	 */
	private void createRawStructure() {
		currentData.addLineRaw("");
		// TODO anything?
	}

	/**
	 * @param rawFile
	 * @throws AbortException 
	 * @throws IOException 
	 */
	private void askToOverwrite(File f) throws AbortException, IOException {
		if (!f.exists())
			return;
		if (Settings.autoOverwrite()) {
			f.delete();
			f.createNewFile();
			return;
		}
		int res = JOptionPane.showConfirmDialog(gui, "This File already exists. Do you want to overwrite it?", 
				"Overwrite?", JOptionPane.YES_NO_OPTION);
		if (res == JOptionPane.NO_OPTION) {
			throw new AbortException("When asked to overwrite: Declined.");
		} else {
			f.delete();
			f.createNewFile();
		}
	}

	private void askToSave() throws AbortException {
		if (unsavedChanges){
			int result = JOptionPane.showConfirmDialog(gui,
					"Unsaved Changes in your Project. Would you like to save them?",
					"Unsaved Changes...", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.CANCEL_OPTION) {
				throw new AbortException("Saving Aborted.");
			}
			if (result == JOptionPane.YES_OPTION) {
				save();
			}
		}
	}

	/**
	 * 
	 */
	private void createXMLStructure() {
		Log.log("Creating XML Structure...");
		
		Namespace ns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");
		Element root = new Element("TEI", ns);
		createTEIStructure(root); //TODO other formats than TEI... MENOTA, etc.
		Document d = new Document(root);
		
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try {
			Log.log("Document:");
			out.output(d, Log.getCurrentLogStream());
			Log.log();
		} catch (IOException e) {
			Log.log(e.getMessage());
			e.printStackTrace();
		}
		currentData.setJDOMDocument(d);
	}

	/**
	 * @param root
	 */
	private void createTEIStructure(Element root) {
		//TODO get Meta Information
		
		//TEI Header
		Element header = new Element("teiHeader", root.getNamespace());
		root.addContent(header);
		Element fDesc = new Element("fileDesc", root.getNamespace());
		header.addContent(fDesc);
		Element tStmt = new Element("titleStmt", root.getNamespace());
		fDesc.addContent(tStmt);
		Element title = new Element("title", root.getNamespace());
		tStmt.addContent(title);
		//TODO use actual title here
		title.addContent("[Title]");
		Element author = new Element("author", root.getNamespace());
		tStmt.addContent(author);
		//TODO use actual author here
		author.addContent("[Author]");
		
		Element publicationStmt = new Element("publicationStmt", root.getNamespace());
		fDesc.addContent(publicationStmt);
		//TODO have actual publication statement
		Element p = new Element("p", root.getNamespace());
		p.addContent("[Publication Statement]");
		publicationStmt.addContent(p);		
		
		Element sourceDesc = new Element("sourceDesc", root.getNamespace());
		fDesc.addContent(sourceDesc);
		//TODO have actual source description
		p = new Element("p", root.getNamespace());
		p.addContent("[Source Description]");
		publicationStmt.addContent(p);
		
		//TEI Body
		Element text = new Element("text", root.getNamespace());
		root.addContent(text);
		Element body = new Element("body", root.getNamespace());
		text.addContent(body);
		Element div = new Element("div", root.getNamespace());
		body.addContent(div);
		div.addContent(new Comment("Here starts the Transcription."));
		currentData.setInsertionIndex(div, 1);
		div.addContent(new Comment("Here ends the Transcription."));
		
	}

	/**
	 * 
	 */
	public void openFolder() {
		Log.log("Opening Project Folder...");

		if (Desktop.isDesktopSupported()){
			Desktop d = Desktop.getDesktop();
			try {
				d.open(currentData.getCurrentLocation());
				Log.log("Opened Project Folder.");
			} catch (Exception e) {
				Log.log("Failed to open Project Folder.");
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * 
	 */
	public void save() {
		Log.log("Saving...");
		
		boolean r = saveForRaw();
		boolean x = saveForXML();
		
		if (r && x) {
			unsavedChanges = false;
			Log.log("Saved Sucessfully.");
		} else {
			Log.log("A Problem occured during saving.");
		}
		refreshGUI();
	}

	/**
	 * 
	 */
	private boolean saveForXML() {
		File f = currentData.getXMLFile();
		if (f == null) {
			return saveAsForXML();
		}
		f.delete();
		try {
			f.createNewFile();
		} catch (IOException e1) {
			Log.log("Failed to Save XML.");
			return false;
		}
		
		String xmlString = getXMLAsString();
		
		if (xmlString==null || xmlString.isEmpty()) {
			Log.log("No XML Data to save.");
			return false;
		}
		try (FileOutputStream fos = new FileOutputStream(f);
				//PrintWriter w = new PrintWriter(fos)){
				OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
				PrintWriter w = new PrintWriter(osw)){
			w.println(xmlString);
		} catch (FileNotFoundException e) {
			Log.log("Failed to Save XML.");
			return false;
		} catch (IOException e) {
			Log.log("Failed to Save XML.");
			return false;
		}
		
		
//		Document d = currentData.getJDOMDocument();
//		if (d==null) {
//			Log.log("No XML Data to save.");
//			return false;
//		}
//		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
//		//TODO make words one-lined
//		
//		try {
//			FileOutputStream fos = new FileOutputStream(f);
//			out.output(d, fos);
//		} catch (FileNotFoundException e) {
//			Log.log("Failed to Save XML.");
//			return false;
//		} catch (IOException e) {
//			Log.log("Failed to Save XML.");
//			return false;
//		}
		
		
		Log.log("Sucessfully saved XML File.");
		return true;
	}

	/**
	 * 
	 */
	private boolean saveForRaw() {
		File f = currentData.getRawFile();
		if (f == null) {
			return saveAsForRaw();
		}
		f.delete();
		try {
			f.createNewFile();
		} catch (IOException e1) {
			Log.log("Failed to Save XML.");
			return false;
		}
		
		ArrayList<String> raw = currentData.getRawList();
		if (raw==null || raw.isEmpty()) {
			Log.log("No Raw Data to save.");
			return false;
		}
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			PrintStream p = new PrintStream(fos);
			for (String s: raw) {
				p.println(s);
			}
			p.close();
		} catch (FileNotFoundException e) {
			Log.log("Failed to Save Raw.");
			return false;
		}

		Log.log("Sucessfully saved Raw File.");
		return true;
	}

	/**
	 * 
	 */
	public void saveAs() {
		Log.log("Saving As...");

		boolean r = saveAsForRaw();
		boolean x = saveAsForXML();

		if (r && x) {
			unsavedChanges = false;
			Log.log("Saved Sucessfully.");
		} else {
			Log.log("A Problem occured during saving.");
		}
		refreshGUI();
	}

	/**
	 * @return 
	 * 
	 */
	private boolean saveAsForXML() {
		File res;
		try {
			res = getFileXML();
			
			currentData.setXMLFile(res);
			
			return saveForXML();
		} catch (AbortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}


	private File getFileRaw() throws AbortException {
		File f = null;
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File("."));
		fc.setFileFilter(new FileNameExtensionFilter("Raw Text", "txt"));
		//fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showOpenDialog(gui);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = fc.getSelectedFile();
		} else {
			throw new AbortException("Opening Raw: Canceled.");
		}
		if (f.isDirectory()) {
			throw new AbortException("Opening Raw: Chosen File was Directory.");
		}

		String name = f.getName();
		if (name.contains("."))
			name = name.split("\\.")[0];
		name += ".txt";
		File res = new File(f.getParent(), name);
		
		
		return res;
	}

	private File getFileXML()  throws AbortException {
		File f = null;
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(".")); //TODO use current working location on these?
		fc.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
		//fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showSaveDialog(gui);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = fc.getSelectedFile();
		} else {
			throw new AbortException("Opening XML: Canceled.");
		}
		if (f.isDirectory()) {
			throw new AbortException("Opening XML: Chosen File was Directory.");
		}
		String name = f.getName();
		if (name.contains("."))
			name = name.split("\\.")[0];
		name += ".xml";
		File res = new File(f.getParent(), name);
		return res;
	}

	/**
	 * @return 
	 * 
	 */
	private boolean saveAsForRaw() {
		File f = null;
//		JFileChooser fc = new JFileChooser();
//		fc.setCurrentDirectory(new File(".")); //TODO use current working location on these?
//		fc.setFileFilter(new FileNameExtensionFilter("Raw File", "txt"));
//		//fc.setMultiSelectionEnabled(false);
//		int returnVal = fc.showSaveDialog(gui);
//		if (returnVal == JFileChooser.APPROVE_OPTION) {
//			f = fc.getSelectedFile();
//		} else {
//			return false;
//		}
//		if (f.isDirectory()) {
//			Log.log("Can't work with directories. Aborted.");
//			return false;
//		}
		try {
			f = getFileRaw();
		} catch (AbortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		String name = f.getName();
//		if (name.contains("."))
//			name = name.split("\\.")[0];
//		name += ".txt";
//		File res = new File(f.getParent(), name);
//		
		
//		currentData.setRawFile(res);
		currentData.setRawFile(f);
		
		return saveForRaw();
	}

	/**
	 * 
	 */
	public void closeProject() {
		Log.log("Closing Project...");
		
		try {
			askToSave();

			dataStages = new LinkedList<MainDataSet>();
			dataStageIndex = 0;
			currentData = new MainDataSet(this);
			addDataStage();
			unsavedChanges = false;
			refreshGUI();
			
		} catch (AbortException e) {
			Log.log("Aborted: " + e.getMessage());
		}
	}

	/**
	 * @param line
	 */
	public void addLine(String line) {
		Log.log("Adding Line: "+line);
		line = correctSpaces(line);
		processLine(line);
		addDataStage();
	}

	/**
	 * @param text
	 */
	public void updateLinePreview(String lineForPreview) {
		Log.log("Updating Preview with Line: "+lineForPreview);
		// TODO a word has been typed in the editor; update preview of current line
		Log.log("!!!!! NOT YET IMPLEMENTED !!!!!");
		
	}

	/**
	 * @param currentLine 
	 * 
	 */
	public void ctrlTabPressed(String currentLine) {
		Log.log("Handling 'control TAB'...");
		// TODO implement "closing the last open tag"
		Log.log("!!!!! NOT YET IMPLEMENTED !!!!!");
	}

	/**
	 * 
	 */
	public void prepareShutDown() {
		if (unsavedChanges){
			int result = JOptionPane.showConfirmDialog(gui,
					"Unsaved Changes in your Project. Would you like to save them?",
					"Unsaved Changes...", JOptionPane.YES_NO_CANCEL_OPTION);
			if (result == JOptionPane.CANCEL_OPTION) {
				return;
			}
			if (result == JOptionPane.YES_OPTION) {
				save();
				shutDown();
			}
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
	 * @return
	 */
	public boolean canUndo() {
		boolean res = false;
		res = dataStages.size() > dataStageIndex+1;
		return res;
	}

	/**
	 * @return
	 */
	public boolean canRedo() {
		return dataStageIndex > 0;
	}

	/**
	 * 
	 */
	public void redo() {
		dataStageIndex--;
		currentData = dataStages.get(dataStageIndex);
		refreshGUI();
	}

	/**
	 * 
	 */
	public void undo() {
		dataStageIndex++;
		currentData = dataStages.get(dataStageIndex);
		refreshGUI();
	}

	/**
	 * 
	 */
	public ArrayList<BufferedImage> getLoadedImages() {
		return currentData.getLoadedImages();
	}

	/**
	 * @param tp
	 */
	public void thumbnailRequestsActivation(ThumbnailPanel tp) {
		currentData.setActivatedImage(tp.getInitialImage());
		refreshGUI();
	}

	/**
	 * @param text
	 * @return
	 */
	public String correctSpaces(String text) {
		text = text.replace("[", " [");
		text = text.replace("]", "] ");
		text = text.replace(".", " . ");
		text = text.replace("/*", " /*");
		text = text.replace("*/", "*/ ");
		//TODO more rules?
		
		text = setUnderscoresInComments(text);
		
		while (text.contains("  ")) {
			text = text.replace("  ", " ");
		}
		
		return text.trim();
	}

	/**
	 * @param text
	 * @return
	 */
	private String setUnderscoresInComments(String text) {
		
		String m = "/\\*.*?\\*/";

		Pattern regex = Pattern.compile(m);
		Matcher matcher = regex.matcher(text);
			
		String res = text;
		
		while (matcher.find()){
			String hit = matcher.group();
			System.out.println(hit);
			String hitCorr = hit.replace(" ", "_");
			res = res.replace(hit, hitCorr);
		}
		
		return res;
	}

	/**
	 * @return
	 */
	public File getCurrentLocation() {
		return currentData.getCurrentLocation();
	}

	/**
	 * @return
	 */
	public boolean hasUnsavedChanges() {
		return unsavedChanges;
	}

	/**
	 * @return
	 */
	public String getHTMLRepresentation() {
		// make a html representation of the xml content
		return "<b>Sylesheet View here</b>";
	}

	
	
}
