package gClasses.gInterfaces;


import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;

public class GDialog extends JDialog implements WindowFocusListener
{
private static final long serialVersionUID = 871269463178323187L;
	
	
	public GTextZone textZone;
	
	public GDialog(String nam, String text, int width, int height, boolean centre)
	{
		addWindowFocusListener(this);
		setResizable(false);
		setSize(width, height);
		setTitle(nam);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		this.setAlwaysOnTop(true);
		
		textZone = new GTextZone(text);
		textZone.setCenter(centre);
		
		this.add(textZone);
	}
	
	private boolean fixFocus = false;
	public void fixFocus(boolean bool)
	{
		this.fixFocus = bool;
		this.toFront();
	}
	
	public void windowLostFocus(WindowEvent arg0)
	{
		if (fixFocus) this.toFront();
	}
	
	
	public void windowGainedFocus(WindowEvent arg0) {}
	
}
