package gClasses.gInterfaces;

import java.awt.Color;
import java.awt.Graphics;

import gClasses.gInterfaces.gPanel.GPanel;

public class GLoadingBar extends GPanel {
	
	private Color foreground = new Color(19, 71, 84);
	private float progression = 0;
	
	public GLoadingBar() {
		this.setBackground(new Color(23, 23, 20));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(foreground);
		g.fillRect(0, 0, (int)(progression*(float)this.getWidth()), this.getHeight());
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public float getProgression() {
		return progression;
	}

	public void setProgression(float progression) {
		this.progression = progression;
	}
	
}
