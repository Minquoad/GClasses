package gClasses.gInterfaces.gPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import gClasses.gInterfaces.gPanel.GLayout.ComponentBoundsSetter;

public class GPanel extends JPanel {

	private BufferedImage backgroundPicture = null;
	private BackgroundDisplayType backgroundDisplayType;

	public enum BackgroundDisplayType {
		FIT, FILL, STRETCH
	}

	public GPanel() {
		this.setOpaque(false);
		this.setLayout(new GLayout());
	}

	public void setBackground(Color bg) {
		this.setOpaque(true);
		super.setBackground(bg);
	}

	public void add(Component comp, float x, float y) {
		super.add(comp);
		if (this.getLayout() instanceof GLayout) {
			GLayout layout = (GLayout) this.getLayout();
			layout.addLayoutComponent(comp, x, y);
		}
	}

	public void add(Component comp, float x, float y, float w, float h) {
		super.add(comp);
		if (this.getLayout() instanceof GLayout) {
			GLayout layout = (GLayout) this.getLayout();
			layout.addLayoutComponent(comp, x, y, w, h);
		}
	}

	public void addAnchored(Component comp, Component anchor, int xPadding, int yPadding) {
		super.add(comp);
		if (this.getLayout() instanceof GLayout) {
			GLayout layout = (GLayout) this.getLayout();
			layout.addLayoutComponentAnchored(comp, anchor, xPadding, yPadding);
		}
	}

	public void addAnchoredToRight(Component comp, Component anchor, int xPadding, int yPadding) {
		super.add(comp);
		if (this.getLayout() instanceof GLayout) {
			GLayout layout = (GLayout) this.getLayout();
			layout.addLayoutComponentAnchoredToRight(comp, anchor, xPadding, yPadding);
		}
	}

	public void addAnchoredToBottom(Component comp, Component anchor, int xPadding, int yPadding) {
		super.add(comp);
		if (this.getLayout() instanceof GLayout) {
			GLayout layout = (GLayout) this.getLayout();
			layout.addLayoutComponentAnchoredToBottom(comp, anchor, xPadding, yPadding);
		}
	}

	public void setBackgroundPicture(BufferedImage backgroundPicture, BackgroundDisplayType backgroundDisplayType) {
		this.backgroundPicture = backgroundPicture;
		this.backgroundDisplayType = backgroundDisplayType;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (backgroundPicture != null) {
			switch (backgroundDisplayType) {
			case FIT:
				float rate = Math.min((float) this.getWidth() / (float) backgroundPicture.getWidth(),
						(float) this.getHeight() / (float) backgroundPicture.getHeight());
				int imW = (int) ((float) backgroundPicture.getWidth() * rate);
				int imH = (int) ((float) backgroundPicture.getHeight() * rate);

				BufferedImage scaled = getScaledInstance(backgroundPicture, imW, imH);

				g.drawImage(scaled, (this.getWidth() - imW) / 2, (this.getHeight() - imH) / 2, imW, imH, this);
				break;
			case FILL:
				float rate1 = Math.max((float) this.getWidth() / (float) backgroundPicture.getWidth(),
						(float) (this.getHeight()) / (float) backgroundPicture.getHeight());
				int imW1 = (int) ((float) backgroundPicture.getWidth() * rate1);
				int imH1 = (int) ((float) backgroundPicture.getHeight() * rate1);

				BufferedImage scaled1 = getScaledInstance(backgroundPicture, imW1, imH1);
				g.drawImage(scaled1, (this.getWidth() - imW1) / 2, (this.getHeight() - imH1) / 2, imW1, imH1, this);
				break;
			case STRETCH:
				g.drawImage(backgroundPicture, 0, 0, this.getWidth(), this.getHeight(), null);
				break;
			}
		}

	}

	public void addComponentBoundsSetter(ComponentBoundsSetter componentBoundsSetter) {
		if (this.getLayout() instanceof GLayout) {
			GLayout layout = (GLayout) this.getLayout();
			layout.addComponentBoundsSetter(componentBoundsSetter);
		}
	}

	public void removeComponentBoundsSetter(ComponentBoundsSetter componentBoundsSetter) {
		if (this.getLayout() instanceof GLayout) {
			GLayout layout = (GLayout) this.getLayout();
			layout.removeComponentBoundsSetter(componentBoundsSetter);
		}
	}

	public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight) {
		boolean higherQuality = img.getWidth() > targetWidth;
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			w = img.getWidth();
			h = img.getHeight();
		} else {
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}
			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}
			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

}
