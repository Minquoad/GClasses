package gClasses.gInterfaces;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import gClasses.gInterfaces.gPanel.GPanel;

public class GTablePanel extends GPanel {

	private Color headerForeground = new Color(255, 255, 255);
	private Color headerBackground = new Color(0, 0, 0);
	private Color foreground = new Color(0, 0, 0);
	private Color background = new Color(255, 255, 255);
	private Color headerCellBorderColor = new Color(127, 127, 127);
	private Color cellBorderColor = new Color(127, 127, 127);
	private Color selectedBackground = new Color(184, 207, 229);
	private Color selectedForeground = new Color(255 - 184, 255 - 207, 255 - 229);

	protected JTable table;
	protected JScrollPane scrollPane;

	public void setTable(JTable table) {

		this.table = table;

		setTheme(table);

		if (this.scrollPane == null) {
			this.scrollPane = new JScrollPane(table);
			this.scrollPane.setOpaque(false);
			this.scrollPane.getViewport().setOpaque(false);
			this.scrollPane.setBorder(BorderFactory.createEmptyBorder());
			this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			this.add(this.scrollPane, 0f, 0f, 1f, 1f);
		} else {
			this.scrollPane.setViewportView(table);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (scrollPane != null && table != null) {
			g.setColor(headerBackground);
			g.fillRect(
					this.getWidth() - scrollPane.getVerticalScrollBar().getWidth(), 
					0, 
					scrollPane.getVerticalScrollBar().getWidth(), 
					table.getTableHeader().getHeight());
		}
	}

	private void setTheme(JTable table) {

		table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				if (component instanceof DefaultTableCellRenderer) {
					DefaultTableCellRenderer defaultRenderer = (DefaultTableCellRenderer) component;

					defaultRenderer.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, headerCellBorderColor));
					defaultRenderer.setBackground(headerBackground);
					defaultRenderer.setForeground(headerForeground);
					defaultRenderer.setHorizontalAlignment(SwingConstants.CENTER);
				}

				return component;
			}
		});
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				if (component instanceof DefaultTableCellRenderer) {
					DefaultTableCellRenderer defaultRenderer = (DefaultTableCellRenderer) component;

					if (isSelected) {
						defaultRenderer.setBackground(selectedBackground);
						defaultRenderer.setForeground(selectedForeground);
					} else {
						defaultRenderer.setBackground(background);
						defaultRenderer.setForeground(foreground);
					}
					defaultRenderer.setHorizontalAlignment(SwingConstants.CENTER);
				}

				return component;
			}
		});

		table.setGridColor(cellBorderColor);
		table.setOpaque(false);

		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);

		table.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
	}

	public void generateGridColor() {
		cellBorderColor = new Color(
				(foreground.getRed() + background.getRed() * 4) / 5,
				(foreground.getGreen() + background.getGreen() * 4) / 5,
				(foreground.getBlue() + background.getBlue() * 4) / 5);
	}

	public Color getHeaderForeground() {
		return headerForeground;
	}

	public void setHeaderForeground(Color headerForeground) {
		this.headerForeground = headerForeground;
	}

	public Color getHeaderBackground() {
		return headerBackground;
	}

	public void setHeaderBackground(Color headerBackground) {
		this.headerBackground = headerBackground;
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public Color getHeaderCellBorderColor() {
		return headerCellBorderColor;
	}

	public void setHeaderCellBorderColor(Color headerCellBorderColor) {
		this.headerCellBorderColor = headerCellBorderColor;
	}

	public Color getCellBorderColor() {
		return cellBorderColor;
	}

	public void setCellBorderColor(Color cellBorderColor) {
		this.cellBorderColor = cellBorderColor;
	}

	public Color getSelectedBackground() {
		return selectedBackground;
	}

	public void setSelectedBackground(Color selectedBackground) {
		this.selectedBackground = selectedBackground;
	}

	public Color getSelectedForeground() {
		return selectedForeground;
	}

	public void setSelectedForeground(Color selectedForeground) {
		this.selectedForeground = selectedForeground;
	}

}
