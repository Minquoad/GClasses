package gClasses.gInterfaces;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public abstract class GChoixFichier {

	public enum Mode {
		SAVING, OPENING
	}

	public static boolean selectFileAndPerforme(Component parent, String basePath, Mode mode,
			FileActionListener actionListener) {
		if (mode == Mode.SAVING) {
			return selectFileAndPerformeSaving(parent, basePath, actionListener);
		} else {
			return selectFileAndPerformeOpening(parent, basePath, actionListener);
		}
	}

	public static boolean selectFileAndPerformeSaving(Component parent, String basePath,
			FileActionListener actionListener) {
		JFileChooser dialogue = creatFileChooser(basePath);
		int approval = dialogue.showSaveDialog(parent);
		File file = dialogue.getSelectedFile();
		if (approval == JFileChooser.APPROVE_OPTION) {
			actionListener.actionPerformed(file);
			return true;
		} else {
			return false;
		}
	}

	public static boolean selectFileAndPerformeOpening(Component parent, String basePath,
			FileActionListener actionListener) {
		JFileChooser dialogue = creatFileChooser(basePath);
		int approval = dialogue.showOpenDialog(parent);
		File file = dialogue.getSelectedFile();
		if (approval == JFileChooser.APPROVE_OPTION && file != null) {
			actionListener.actionPerformed(file);
			return true;
		} else {
			return false;
		}
	}

	public static boolean selectFolderAndPerformeOpening(Component parent, String basePath,
			FileActionListener actionListener) {
		JFileChooser dialogue = new JFileChooser();
		if (basePath != null) {
			File currentDirectory = new File(basePath);
			if (currentDirectory.exists()) {
				dialogue.setCurrentDirectory(new File(basePath));
			}
		}
		dialogue.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int approval = dialogue.showOpenDialog(parent);
		File file = dialogue.getSelectedFile();
		if (approval == JFileChooser.APPROVE_OPTION && file != null) {
			actionListener.actionPerformed(file);
			return true;
		} else {
			return false;
		}
	}

	private static JFileChooser creatFileChooser(String basePath) {
		JFileChooser dialogue = new JFileChooser();
		if (basePath != null) {
			File currentDirectory = new File(basePath);
			if (currentDirectory.exists()) {
				dialogue.setCurrentDirectory(new File(basePath));
			}
		}
		dialogue.setFileSelectionMode(JFileChooser.FILES_ONLY);
		return dialogue;
	}

	public interface FileActionListener {
		public void actionPerformed(File file);
	}
}
