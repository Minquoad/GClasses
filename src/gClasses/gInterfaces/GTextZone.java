package gClasses.gInterfaces;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class GTextZone extends JScrollPane {
	private static final long serialVersionUID = 6219847190534874494L;

	private boolean centrer = false;

	private JTextPane textPane = new JTextPane();

	private String text_d_origin;

	public GTextZone() {
		this.constuct();
	}

	public GTextZone(String text) {
		this.constuct();
		this.setText(text);
	}

	private void constuct() {
		textPane.setEditable(false);
		textPane.setBackground(new Color(238, 238, 238));
		textPane.setContentType("text/html");

		this.setViewportView(textPane);
	}

	public JTextPane getJTextPane() {
		return textPane;
	}

	public synchronized void setText(String text) {
		text_d_origin = new String(text);

		if (text.length() <= 5) {
			text = this.ajouterBaliseBody(text);
		} else {
			String body = "<body";
			boolean bool = true;
			for (int i = 0; i <= 4; i++) {
				bool = bool && text.getBytes()[i] == body.getBytes()[i];
			}
			if (!bool) {
				text = this.ajouterBaliseBody(text);
			}
		}

		textPane.setText(text);
	}

	private String ajouterBaliseBody(String text) {
		if (centrer)
			return "<body style='text-align: center;font-family: arial;'>" + text + "</body>";
		else
			return "<body style='font-family: arial;'>" + text + "</body>";
	}

	public void setCenter(boolean centrer) {
		if (this.centrer != centrer) {
			this.centrer = centrer;
			this.setText(text_d_origin);
		}
	}

	public String getText() {
		return text_d_origin;
	}

	public void addText(String text) {
		this.setText(this.getText() + text);
	}

}
