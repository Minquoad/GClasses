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
import gClasses.GRessourcesCollector;

public class GFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 981627542880662376L;

	private GCardLayout cardLayout;

	public GFrame(String titre, int largeur, int hauteur) {
		this.construct(titre, largeur, hauteur, null);
	}

	public GFrame(String titre, int largeur, int hauteur, Image icone, File framePreferencesFile) {
		if (icone != null) {
			this.setIconImage(icone);
		}

		this.construct(titre, largeur, hauteur, framePreferencesFile);
	}

	public GFrame(String titre, int largeur, int hauteur, String icone, File framePreferencesFile) {
		if (icone != null) {
			this.setIconImage(GRessourcesCollector.getImage(icone));
		}

		this.construct(titre, largeur, hauteur, framePreferencesFile);
	}

	private void construct(String titre, int largeur, int hauteur, File framePreferencesFile) {
		if (titre != null) {
			this.setTitle(titre);
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(largeur, hauteur));
		this.setLocationRelativeTo(null);
		this.cardLayout = new GCardLayout();
		this.getContentPane().setLayout(cardLayout);

		this.framePreferencesFile = framePreferencesFile;
		if (framePreferencesFile != null) {
			try {
				DataAssociator da = new DataAssociator(framePreferencesFile);
				if (da.exists("frame_x_LocationOnScreen") == false) {
					da.addValue("frame_x_LocationOnScreen", 50);
				}
				if (da.exists("frame_y_LocationOnScreen") == false) {
					da.addValue("frame_y_LocationOnScreen", 50);
				}
				if (da.exists("frame_Width") == false) {
					da.addValue("frame_Width", largeur);
				}
				if (da.exists("frame_Height") == false) {
					da.addValue("frame_Height", hauteur);
				}
				if (da.exists("frame_ExtendedState") == false) {
					da.addValue("frame_ExtendedState", 0);
				}

				da.save();

				this.setLocation(da.getValueInt("frame_x_LocationOnScreen"),
						da.getValueInt("frame_y_LocationOnScreen"));
				this.setSize(new Dimension(da.getValueInt("frame_Width"), da.getValueInt("frame_Height")));
				this.setExtendedState(da.getValueInt("frame_ExtendedState"));

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

	public void dispose() {
		this.saveState();
		super.dispose();
	}

	private File framePreferencesFile;

	public void saveState() {
		if (framePreferencesFile != null) {
			try {
				DataAssociator da = new DataAssociator(framePreferencesFile);

				da.setValue("frame_ExtendedState", getExtendedState());

				if (getExtendedState() != 6) {
					da.setValue("frame_x_LocationOnScreen", getLocationOnScreen().x);
					da.setValue("frame_y_LocationOnScreen", getLocationOnScreen().y);
					da.setValue("frame_Width", getWidth());
					da.setValue("frame_Height", getHeight());
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
