/**
 * 
 */
package ch.blandolt.turboTranscriber.gui;

import ch.blandolt.turboTranscriber.util.Log;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;


/**
 * @author Balduin Landolt
 *
 */
public class ThumbnailPanel extends JLabel implements MouseListener {
	private BufferedImage initialImage;
	private BufferedImage scaledImage;
	private Border activatedBorder;
	private Border nonActivatedBorder;
	private boolean isSelected;
	private MainGUIManuallyCreated owner;


//	 * @param im
//	 * @param caller
	public ThumbnailPanel(BufferedImage im, MainGUIManuallyCreated owner) {
		this.owner = owner;
		initialImage = im;
		scaledImage = scale(im, 100);
		activatedBorder = BorderFactory.createLineBorder(Color.GREEN, 5);
		nonActivatedBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 5);
		isSelected = false;
		setRightBorder();
		this.setIcon(new ImageIcon(scaledImage));
		addMouseListener(this);
		this.setToolTipText("Click me to get me to pop up.");
	}


	/**
	 * 
	 */
	private void setRightBorder() {
		if (isSelected) {
			setBorder(activatedBorder);
		} else {
			setBorder(nonActivatedBorder);
		}
	}

	/**
	 * @param im
	 * @param height
	 * @return
	 */
	private BufferedImage scale(BufferedImage im, int height) {
		
		float image_ratio = (float)im.getWidth() / (float)im.getHeight();
		int w = (int) ((float)height * image_ratio);
		BufferedImage resizedImg = new BufferedImage(w, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	    g2.drawImage(im, 0, 0, w, height, null);
	    g2.dispose();
	    
		return resizedImg;
	}

	/**
	 * @return
	 */
	public BufferedImage getInitialImage() {
		return initialImage;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		Log.log("Thumbnail: I got Clicked!");
		if (!isSelected) {
			owner.thumbnailRequestsActivation(this);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {}

	/**
	 * @param b
	 */
	public void setActivated(boolean b) {
		isSelected = b;
		setRightBorder();
	}

	/**
	 * @param im
	 * @return
	 */
	public boolean hasSamePicture(BufferedImage im) {
		if (im == null || initialImage == null)
			return false;
		if (initialImage == im)
			return true;
		if (initialImage.getWidth() != im.getWidth())
			return false;
		if (initialImage.getHeight() != im.getHeight())
			return false;
		if (initialImage.getData().hashCode() != im.getData().hashCode())
			return false;

		return imageDataIsSame(im, initialImage);
	}


	private boolean imageDataIsSame(BufferedImage im1, BufferedImage im2) {
		
		WritableRaster raster = im1.getRaster();
		DataBufferByte data1   = (DataBufferByte) raster.getDataBuffer();
		raster = im2.getRaster();
		DataBufferByte data2   = (DataBufferByte) raster.getDataBuffer();
		
		if (data1.getSize() != data2.getSize())
			return false;
		
		for (int i=0; i<data1.getSize(); i++) {
			Log.log(data1.getElem(i));
		}
		
		return true;
	}
	
}
