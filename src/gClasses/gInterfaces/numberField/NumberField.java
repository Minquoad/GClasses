package gClasses.gInterfaces.numberField;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import gClasses.gInterfaces.gPanel.GPanel;

public abstract class NumberField<NumberType> extends GPanel {

	private Color borderColor = new Color(204, 204, 204);
	private Color foreground = new Color(204, 204, 204);

	private NumberType defaultValue;
	private NumberType maxValue;
	private NumberType minValue;

	private int maxDigit = 10;

	private JTextField textField = new JTextField();

	public NumberField() {
		updateBorder();
		textField.setOpaque(false);
		textField.setForeground(foreground);
		textField.setCaretColor(foreground);
		textField.setBorder(BorderFactory.createEmptyBorder());

		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				keyEventPosted();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyEventPosted();
			}

			@Override
			public void keyPressed(KeyEvent e) {
				keyEventPosted();
			}
		});

		this.add(textField);
		this.addComponentBoundsSetter(thisNf -> {
			textField.setBounds(2, 0, this.getWidth() - 2, this.getHeight());
		});
	}

	private void keyEventPosted() {
		String text = textField.getText();

		String newText = NumberField.this.formatString(text);

		newText = newText.substring(0, Math.min(newText.length(), maxDigit));

		if (!newText.equals("") && !newText.equals("-") && !newText.equals(".")) {
			NumberType n = parseT(newText);
			if (isToLow(n)) {
				textField.setText(minValue.toString());
			}
			if (isToHight(n)) {
				textField.setText(maxValue.toString());
			}
		}

		if (!text.equals(newText)) {
			textField.setText(newText);
		}
	}

	public void setEditable(boolean editable) {
		textField.setEditable(editable);
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public NumberType getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(NumberType defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		updateBorder();
	}

	private void updateBorder() {
		this.setBorder(BorderFactory.createLineBorder(borderColor));
	}

	protected String getText() {
		return textField.getText();
	}

	protected void setText(String str) {
		textField.setText(str);
	}

	public NumberType getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(NumberType maxValue) {
		this.maxValue = maxValue;
	}

	public NumberType getMinValue() {
		return minValue;
	}

	public void setMinValue(NumberType minValue) {
		this.minValue = minValue;
	}

	public NumberType getValue() {
		String str = this.formatString(this.getText());
		if (str.length() == 0 ||
				(str.length() == 1 && str.charAt(0) == '-')) {
			return this.getDefaultValue();
		} else {
			NumberType n = this.parseT(str);
			return n;
		}
	}

	public int getMaxDigit() {
		return maxDigit;
	}

	public void setMaxDigit(int maxDigit) {
		this.maxDigit = maxDigit;
	}

	public void setValue(NumberType value) {
		textField.setText(value.toString());
	}

	public void displayDefaultValue() {
		this.setValue(this.getDefaultValue());
	}

	public void addTextKeyListener(KeyListener l) {
		textField.addKeyListener(l);
	}

	public void forceText(String str) {
		textField.setText(str);
	}

	public abstract String formatString(String str);

	protected abstract NumberType parseT(String str);

	protected abstract boolean isToHight(NumberType n);

	protected abstract boolean isToLow(NumberType n);

}
