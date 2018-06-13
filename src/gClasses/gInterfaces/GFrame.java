package gClasses.gInterfaces;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import gClasses.DataAssociator;
import gClasses.GResourcesCollector;

public class GFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 981627542880662376L;

	private static final String X_LOCATION_ON_SCREEN_KEY = "gFrameXLocationOnScreenKey";
	private static final String Y_LOCATION_ON_SCREEN_KEY = "gFrameYLocationOnScreenKey";
	private static final String WIDTH_KEY = "gFrameWidthKey";
	private static final String HEIGHT_KEY = "gFrameHeightKey";
	private static final String EXTENDED_STATE_KEY = "gFrameExtendedStateKey";
	
	private GCardLayout cardLayout;

	private GFrame() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.cardLayout = new GCardLayout();
		this.getContentPane().setLayout(cardLayout);
	}
	
	public GFrame(int largeur, int hauteur) {
		this();
		this.setMinimumSize(new Dimension(largeur, hauteur));
	}

	public GFrame(String titre, int largeur, int hauteur) {
		this(largeur, hauteur);
		if (titre != null) {
			this.setTitle(titre);
		}
	}

	public GFrame(String titre, int largeur, int hauteur, File framePreferencesFile) {
		this(titre, largeur, hauteur);
		this.framePreferencesFile = framePreferencesFile;
		if (framePreferencesFile != null) {
			try {
				DataAssociator da = new DataAssociator(framePreferencesFile);
				if (da.exists(X_LOCATION_ON_SCREEN_KEY) == false) {
					da.addValue(X_LOCATION_ON_SCREEN_KEY, 50);
				}
				if (da.exists(Y_LOCATION_ON_SCREEN_KEY) == false) {
					da.addValue(Y_LOCATION_ON_SCREEN_KEY, 50);
				}
				if (da.exists(WIDTH_KEY) == false) {
					da.addValue(WIDTH_KEY, this.getMinimumSize().width);
				}
				if (da.exists(HEIGHT_KEY) == false) {
					da.addValue(HEIGHT_KEY, this.getMinimumSize().height);
				}
				if (da.exists(EXTENDED_STATE_KEY) == false) {
					da.addValue(EXTENDED_STATE_KEY, 0);
				}
				da.save();

				this.setLocation(da.getValueInt(X_LOCATION_ON_SCREEN_KEY),
						da.getValueInt(Y_LOCATION_ON_SCREEN_KEY));
				this.setSize(new Dimension(da.getValueInt(WIDTH_KEY), da.getValueInt(HEIGHT_KEY)));
				this.setExtendedState(da.getValueInt(EXTENDED_STATE_KEY));

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			this.addWindowListener(new WindowListener() {
				public void windowOpened(WindowEvent e) {
				}

				public void windowIconified(WindowEvent e) {
				}

				public void windowDeiconified(WindowEvent e) {
				}

				public void windowDeactivated(WindowEvent e) {
				}

				public void windowClosed(WindowEvent e) {
				}

				public void windowActivated(WindowEvent e) {
				}

				public void windowClosing(WindowEvent e) {
					saveState();
				}
			});
		}
	}

	public GFrame(String titre, int largeur, int hauteur, File framePreferencesFile, Image icone) {
		this(titre, largeur, hauteur, framePreferencesFile);
		if (icone != null) {
			this.setIconImage(icone);
		}
	}

	public GFrame(String titre, int largeur, int hauteur, File framePreferencesFile, String icone) {
		this(titre, largeur, hauteur, framePreferencesFile, GResourcesCollector.getImage(icone));
	}

	public void dispose() {
		this.saveState();
		super.dispose();
	}

	private File framePreferencesFile;

	public void saveState() {
		if (framePreferencesFile != null) {
			try {
				DataAssociator da = new DataAssociator(framePreferencesFile);

				da.setValue(EXTENDED_STATE_KEY, getExtendedState());

				if (getExtendedState() != 6) {
					da.setValue(X_LOCATION_ON_SCREEN_KEY, getLocationOnScreen().x);
					da.setValue(Y_LOCATION_ON_SCREEN_KEY, getLocationOnScreen().y);
					da.setValue(WIDTH_KEY, getWidth());
					da.setValue(HEIGHT_KEY, getHeight());
				}

				da.save(framePreferencesFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public final void actionPerformed(ActionEvent e) {
		int i = 0;
		while (i < menuItemList.size() && menuItemList.get(i) != e.getSource()) {
			i++;
		}
		if (i != this.menuItemList.size()) {
			cardLayout.show(menuItemList.get(i).getName());
		}
	}

	private Component showedComp;

	public Component getshowedComponent() {
		return showedComp;
	}

	private ArrayList<Component> ComponentList = new ArrayList<Component>();
	private ArrayList<JMenu> menuList = new ArrayList<JMenu>();
	private ArrayList<Component> menuItemList = new ArrayList<Component>();

	public void addMenu(String nom) {
		if (this.getJMenuBar() == null)
			this.setJMenuBar(new JMenuBar());
		JMenu menu = new JMenu(nom);
		menu.setName(nom);
		this.getJMenuBar().add(menu);
		this.menuList.add(menu);
	}

	public void addSubMenu(String nom, String superMenu) {
		JMenu menu = new JMenu(nom);
		menu.setName(nom);
		this.menuList.add(menu);

		getMenu(superMenu).add(menu);
	}

	public void addPanelToMenu(Component comp, String nom, String menuName) {
		if (showedComp == null)
			showedComp = comp;
		this.getContentPane().add(comp, nom);
		JMenuItem mi = new JMenuItem(nom);
		mi.setName(nom);
		mi.addActionListener(this);
		this.menuItemList.add(mi);
		this.ComponentList.add(comp);
		getMenu(menuName).add(mi);
	}

	public void addPanelToMenu(Component comp, String nom, String menuName, int at) {
		if (showedComp == null)
			showedComp = comp;
		this.getContentPane().add(comp, nom, at);
		JMenuItem mi = new JMenuItem(nom);
		mi.setName(nom);
		mi.addActionListener(this);
		this.menuItemList.add(mi);
		this.ComponentList.add(comp);
		getMenu(menuName).add(mi, at);
	}

	public JMenu getMenu(String menuName) {
		int i = 0;
		while (i < this.menuList.size() && this.menuList.get(i).getName() != menuName) {
			i++;
		}
		if (i != this.menuList.size()) {
			return this.menuList.get(i);
		} else
			return null;
	}

	public void remove(Component comp) {
		super.remove(comp);

		menuList.remove(comp);
		menuItemList.remove(comp);
		ComponentList.remove(comp);
	}

	public GCardLayout getCardLayout() {
		return cardLayout;
	}

	public class GCardLayout extends CardLayout {

		public void show(String name) {
			int i = 0;
			while (i < menuItemList.size() && menuItemList.get(i).getName() != name) {
				i++;
			}
			if (i != menuItemList.size()) {
				showedComp = ComponentList.get(i);
			}
			super.show(getContentPane(), name);
		}

		private static final long serialVersionUID = 1L;
	}
}
