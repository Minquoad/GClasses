package gClasses;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public abstract class GResourcesCollector {
	
	public static BufferedInputStream getBufferedInputStream(String url) {
		return new BufferedInputStream(GResourcesCollector.class.getClassLoader().getResourceAsStream(url));
	}

	public static Image getImage(String url) {
		return new ImageIcon(GResourcesCollector.class.getClassLoader().getResource(url)).getImage();
	}

	public static BufferedImage getBufferedImage(String url) {
		return toBufferedImage(getImage(url));
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}

	public static void playSound(String url) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					clip.open(AudioSystem.getAudioInputStream(GResourcesCollector.class.getResource(url)));
					clip.start();
				} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static Clip getClip(String url) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(GResourcesCollector.class.getResource(url)));
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
		}
		return clip;
	}
	
}
