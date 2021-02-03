package kth.ag2411.project;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapPanel extends JPanel { // MapPanel is a subclass of JPanel and thus
	// inherits all its attributes and methods.
	
	// ATTRIBUTES
	// All the attributes of JPanel (which are automatically inherited), plus:
	public BufferedImage image;
	public Double scale;
	
	// CONSRUCTORS
	// All the constructors of JPanel (which are automatically inherited), plus:
	public MapPanel(BufferedImage image, Double scale) {
	super(); // first, instantiate a MapPanel in the same way JPanel does.
	this.image = image; // then, initialize additional attributes
	this.scale = scale;
	}
	
	// All the other methods of JPanel (which are automatically inherited), plus:
	@Override
	protected void paintComponent(Graphics g) {
	super.paintComponent(g); // first, do what JPanel would normally do. Then do:
	this.setSize((int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
	g.drawImage(image, 0, 0, (int) (image.getWidth() * scale), (int) (image.getHeight() * scale), this);
	}
	
	protected void paintPreview() {
		
	}
	
	// The @Override tag will be ignored by the complier. It just signifies that
	// MapPanel modifies JPanel��s paintComponent() method.
	}
