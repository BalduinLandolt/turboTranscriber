/**
 * 
 */
package ch.blandolt.turboTranscriber.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ch.blandolt.turboTranscriber.util.Log;

// import ch.unibas.landolt.balduin.XMLSpeedupV6.src.application.main.SpeedUP;

/**
 * @author Balduin Landolt
 *
 */
@SuppressWarnings("serial")
public class CustomImagePanel extends JPanel implements MouseListener, MouseMotionListener, ComponentListener {
	
	//private SpeedUP parent;
	private BufferedImage panelImage;
	private BufferedImage loadedImage;
	private BufferedImage scaledImage;
	private Rectangle selection;
	private Rectangle provisoricalSelection;
	private Point mouseDown;

	/**
//	 * @param parent
	public CustomImagePanel(SpeedUP parent) {
		this.parent = parent;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addComponentListener(this);
		this.setPreferredSize(new Dimension(100, 100));
		panelImage = new BufferedImage(getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
	}

	 */

	public CustomImagePanel(){
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addComponentListener(this);
		this.setPreferredSize(new Dimension(100, 100));
		panelImage = new BufferedImage(getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);

	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (loadedImage != null) {
			paintPanelImage();
			g.drawImage(panelImage, 0, 0, null);
		}
	}


	/**
	 * 
	 */
	private void paintPanelImage() {
		Graphics2D g = (Graphics2D) panelImage.getGraphics();
		g.setColor(this.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(scaledImage, 0, 0, null);
		if (selection != null) {
			g.drawImage(getPartiallyTransparentOverlay(scaledImage, selection), 0, 0, null);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(4));
			g.drawRect(selection.x, selection.y, selection.width, selection.height);
		}
		if (provisoricalSelection != null) {
			g.setColor(Color.GREEN);
			g.setStroke(new BasicStroke(2));
			g.drawRect(provisoricalSelection.x, provisoricalSelection.y, provisoricalSelection.width, provisoricalSelection.height);
		}
	}
	private BufferedImage getPartiallyTransparentOverlay(BufferedImage im, Rectangle rect) {
		BufferedImage res = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = res.createGraphics();
//		g.setColor(Color.WHITE);
//		g.fillRect(0, 0, im.getWidth(), im.getHeight());
		g.setColor(new Color(0, 0, 0, 127));
//		g.fillRect(0, 0, im.getWidth(), im.getHeight());
		g.fillRect(0, 0, im.getWidth(), rect.y);
		g.fillRect(0, rect.y+rect.height, im.getWidth(), im.getHeight());
		g.fillRect(0, rect.y, rect.x, rect.height);
		g.fillRect(rect.x+rect.width, rect.y, im.getWidth()-(rect.x+rect.width), rect.height);
//		g.setColor(new Color(0, 0, 0, 0));
//		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		return res;
	}


	private BufferedImage getScaledImage(BufferedImage srcImg, int w, int h){
		float image_ratio = (float)srcImg.getWidth() / (float)srcImg.getHeight();
		float component_ratio = (float)getWidth() / (float)getHeight();

		if (image_ratio > component_ratio) {
			h = (int) ((float)w / image_ratio);
		} else {
			w = (int) ((float)h * image_ratio);
		}
		
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	private void scaleImage() {
		if (loadedImage == null)
			return;
			
		scaledImage = getScaledImage(loadedImage, getWidth(), getHeight());
	}


	/**
	 * @param loadedImage
	 */
	public void setImage(BufferedImage loadedImage) {
		if (loadedImage == null) {
			this.loadedImage = loadedImage;
			repaint();
			return;
		}
		
		selection = null;
		this.loadedImage = loadedImage;
		int w = Math.min(loadedImage.getWidth(), getWidth());
		int h = Math.min(loadedImage.getHeight(), getHeight());
		panelImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		this.setPreferredSize(new Dimension(w, h));
		scaleImage();
		repaint();
	}


	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}
		selection = null;
		int x = Math.min(mouseDown.x, e.getX());
		int y = Math.min(mouseDown.y, e.getY());
		int w = Math.abs(mouseDown.x - e.getX());
		int h = Math.abs(mouseDown.y - e.getY());
		provisoricalSelection = new Rectangle(x, y, w, h);
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Log.log("Mouse Clicked: "+e.getPoint());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = e.getPoint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		provisoricalSelection = null;
		int x = Math.min(mouseDown.x, e.getX());
		int y = Math.min(mouseDown.y, e.getY());
		int w = Math.abs(mouseDown.x - e.getX());
		int h = Math.abs(mouseDown.y - e.getY());
		selection = new Rectangle(x, y, w, h);
		if (w < 5 || h < 5) {
			selection = null;
		}
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent arg0) {
		scaleImage();
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
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	@Override
	public void componentShown(ComponentEvent arg0) {}


	/**
	 * @return
	 */
	public BufferedImage getCroppOfSelection() {
		if (selection == null || loadedImage == null || scaledImage == null)
			return loadedImage;
		
		float ratio = (float)scaledImage.getWidth() / (float)loadedImage.getWidth();
		
		int x = (int) (selection.x/ratio);
		int y = (int) (selection.y/ratio);
		int w = (int) (selection.width/ratio);
		int h = (int) (selection.height/ratio);
		
		BufferedImage res = loadedImage.getSubimage(x, y, w, h);
		
		return res;
	}
	
}
