/**
 * 
 */
package ch.unibas.landolt.balduin.XMLSpeedupV6.src.application.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Parent;

import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.Log;
import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.ThumbnailPanel;
import ch.unibas.landolt.balduin.XMLSpeedupV6.src.util.datastructure.AbstractTranscriptionObject;

/**
 * Class that collects all Data of the running program at a given time. <br/>
 * Upon every (relevant) change of data, instead of changing the data,
 * a clone should be created and altered. <br/>
 * Thus, the former set of data can be stored, and recreated at any point. <br/>
 * Likewise, the program itself should not store any data, but constantly rely on
 * an instance of MainDataSet.
 * 
 * @author Balduin Landolt
 *
 */
public class MainDataSet implements Cloneable {
	private SpeedUP parent;
	private BufferedImage image;
	private ArrayList<BufferedImage> loadedImages;
	private File currentLocation;
	private File xmlFile;
	private File rawFile;
	private Document jdomDocument;
	private ArrayList<String> rawData;
	private ArrayList<AbstractTranscriptionObject> internalData;
	private SimpleEntry<Element, Integer> insertionIndex;

	/**
	 * Constructor that creates an instance of {@link MainDataSet};
	 * this constructor should only be called by the MainDataSet.clone() method,
	 * or when creating a new line of Data, when creating a new project. <br/>
	 * In all other cases, the previous instance of {@link MainDataSet} should be cloned.
	 * 
	 * @param speedUP the instance of {@link SpeedUP} that calls this method,
	 * should hand over itself to the constructor.
	 */
	public MainDataSet(SpeedUP speedUP) {
		parent = speedUP;
		loadedImages = new ArrayList<BufferedImage>();
		rawData = new ArrayList<String>();
		internalData = new ArrayList<AbstractTranscriptionObject>();
		currentLocation = null;
		xmlFile = null;
		rawFile = null;
		jdomDocument = null;
		insertionIndex = new SimpleEntry<Element, Integer>(null, 0);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected MainDataSet clone() {
		MainDataSet clone = new MainDataSet(parent);
		for (BufferedImage im: loadedImages) {clone.addLoadedImage(im);}
		for (String s: rawData) {clone.addLineRaw(s);}
		for (AbstractTranscriptionObject o: internalData) {clone.addTranscriptionElement(o.clone());}
		clone.image = image;
		if (currentLocation != null) clone.currentLocation = new File(currentLocation.getAbsolutePath());
		if (xmlFile != null) clone.xmlFile = new File(xmlFile.getAbsolutePath());
		if (rawFile != null) clone.rawFile = new File(rawFile.getAbsolutePath());
		if (jdomDocument != null) clone.jdomDocument = new Document(jdomDocument.getRootElement().clone());
		if (insertionIndex.getKey() != null) 
			clone.insertionIndex = new SimpleEntry<Element, Integer>(getEquivalentParent(clone.jdomDocument), Integer.valueOf(insertionIndex.getValue().intValue()));

		return clone;
	}


	/**
	 * In a jdom Document that is a clone of this.jdomDocument, this method finds and returns the jdom Element,
	 * that is equally positioned, as the Element stored in this.insertionIndex.
	 * 
	 * @param cloneDOM must be a clone of this.jdomDocument.
	 * @return <i>Null</i>, if something is problematic. <br/>
	 * 		   The Element in the clone that is equivalent to the Element stored in this.insertionIndex.
	 */
	private Element getEquivalentParent(Document cloneDOM) {
		if (cloneDOM==null || jdomDocument==null )
			return null;

		Iterator<Content> it = jdomDocument.getDescendants();
		Iterator<Content> itC = cloneDOM.getDescendants();
		while (it.hasNext() && itC.hasNext()) {
			Content c = (Content) it.next();
			Content cc = (Content) itC.next();
			if (!c.getValue().equals(cc.getValue())) {
				Log.log("Uhm... Clone doesn't resemble clone... Fuck.");
				return null;
			}
			if (c == insertionIndex.getKey()){
				if (cc instanceof Element)
					return (Element)cc;
				else
					return null;
			}
		}
		
		return null;
		
		
	}



	/**
	 * Adds an {@link AbstractTranscriptionObject} at the end of the List of stored {@link AbstractTranscriptionObject}.
	 * @param o the Object that is added to the List
	 */
	public void addTranscriptionElement(AbstractTranscriptionObject o) {
		internalData.add(o);
	}
	
	/**
	 * Gets the List of {@link AbstractTranscriptionObject} stored in this Data Set.
	 * @return the List of {@link AbstractTranscriptionObject}
	 */
	public ArrayList<AbstractTranscriptionObject> getInternalDataList(){
		return internalData;
	}

	/**
	 * Adds a line of raw transcription to the Data.
	 * @param s a String representation of the line of raw transcription.
	 */
	public void addLineRaw(String s) {
		rawData.add(s);
	}

	/**
	 * @param im
	 */
	public void setActivatedImage(BufferedImage im) {
		image = im;
	}

	/**
	 * @return
	 */
	public BufferedImage getActivatedImage() {
		return image;
	}

	/**
	 * @param img
	 */
	public void addLoadedImage(BufferedImage img) {
		loadedImages.add(img);
	}

	/**
	 * @return
	 */
	public ArrayList<BufferedImage> getLoadedImages() {
		return loadedImages;
	}

	/**
	 * @return
	 */
	public File getCurrentLocation() {
		return currentLocation;
	}

	/**
	 * @param parentFile
	 */
	public void setCurrentLocation(File f) {
		currentLocation = f;
	}

	/**
	 * @param rawFile
	 */
	public void setRawFile(File f) {
		rawFile = f;
	}

	/**
	 * @param xmlFile
	 */
	public void setXMLFile(File f) {
		xmlFile = f;
	}

	/**
	 * 
	 */
	public File getRawFile() {
		return rawFile;
	}

	/**
	 * 
	 */
	public File getXMLFile() {
		return xmlFile;
	}

	/**
	 * @param d
	 */
	public void setJDOMDocument(Document d) {
		jdomDocument = d;
	}
	
	public Document getJDOMDocument() {
		return jdomDocument;
	}

	/**
	 * @return an {@link ArrayList} of Strings, each of which represents a line of raw transcription.
	 */
	public ArrayList<String> getRawList() {
		return rawData;
	}

	/**
	 * inserts a List of jdom Content at the current insertion location in the xml document.
	 * @param l the list to add
	 */
	public void insertXMLContent(LinkedList<Content> l) {
		if (l == null || l.isEmpty())
			return;
		
		for (Content c: l) {
			insertXMLContent(c);
		}
	}
	
	
	/**
	 * inserts a single instance of jdom Content at the current insertion location in the xml document.
	 * @param c the Content to add
	 */
	private void insertXMLContent(Content c) {
		if (c == null)
			return;
		
		if (c instanceof Element) {
			setRootNamespace((Element)c);
			//TODO will that be a problem once I have multiple namespaces?
		}
		
		Element parent = insertionIndex.getKey();
		int index = insertionIndex.getValue();
		
		parent.addContent(index, c);
		
		insertionIndex.setValue(Integer.valueOf(index+1));
	}
	
	/**
	 * Sets the Namespace of an Element and all its content, to the namespace of the document root.
	 * @param e the Element, which namespace is to be changed.
	 */
	private void setRootNamespace(Element e) {
		e.setNamespace(getRootNamespace());


		Iterator<Content> it = e.getDescendants();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof Element) {
				Element e2 = (Element) o;
				e2.setNamespace(getRootNamespace());
			}
		}
		
	}

	public void setInsertionIndex(Element parent, int index) {
		insertionIndex = new SimpleEntry<Element, Integer>(parent, Integer.valueOf(index));
	}

	/**
	 * @return
	 */
	public Namespace getRootNamespace() {
		return jdomDocument.getRootElement().getNamespace();
	}

	/**
	 * 
	 */
	public void closeTag() {//TODO test, if this works
		
		Element current = insertionIndex.getKey();
		
		Element newElement = current.getParentElement();
		int newIndex = current.getParent().indexOf(current)+1;
		//TODO is that correct, or do I have to remove "+1"?
		
		setInsertionIndex(newElement, newIndex);
		Log.log("set Index out of current element.");
	}

	/**
	 * @param content
	 */
	public void setIndexIntoElement(Content content) {
		if (!(content instanceof Element)) {
			Log.log("couldn't jump insertion index into specified element.");
			return;
		} else {
			Log.log("set Index into element.");
			Element e = (Element)content;
			setInsertionIndex(e, 0);
		}
	}
}
