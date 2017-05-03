package gClasses.gInterfaces.gPanel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class GLayout extends FlowLayout {

	private ArrayList<ComponentWithRelativeRectangle> componentWithRelativeRectangles = new ArrayList<ComponentWithRelativeRectangle>();
	private ArrayList<ComponentWithRelativeLocation> componentWithRelativeLocations = new ArrayList<ComponentWithRelativeLocation>();
	private ArrayList<ComponentAnchored> componentAnchored = new ArrayList<ComponentAnchored>();
	private ArrayList<ComponentAnchoredToRight> componentAnchoredToRights = new ArrayList<ComponentAnchoredToRight>();
	private ArrayList<ComponentAnchoredToBottom> componentAnchoredToBottoms = new ArrayList<ComponentAnchoredToBottom>();
	private ArrayList<ComponentBoundsSetter> componentBoundsSetters = new ArrayList<ComponentBoundsSetter>();

	public void addLayoutComponent(Component comp, float x, float y) {
		ComponentWithRelativeLocation cwrl = new ComponentWithRelativeLocation();
		cwrl.comp = comp;
		cwrl.x = x;
		cwrl.y = y;
		componentWithRelativeLocations.add(cwrl);
	}

	public void addLayoutComponent(Component comp, float x, float y, float w, float h) {
		ComponentWithRelativeRectangle cwrr = new ComponentWithRelativeRectangle();
		cwrr.comp = comp;
		cwrr.x = x;
		cwrr.y = y;
		cwrr.w = w;
		cwrr.h = h;
		componentWithRelativeRectangles.add(cwrr);
	}

	public void addLayoutComponentAnchored(Component comp, Component anchor, int xPadding, int yPadding) {
		ComponentAnchored catr = new ComponentAnchored();
		catr.comp = comp;
		catr.anchor = anchor;
		catr.xPadding = xPadding;
		catr.yPadding = yPadding;
		componentAnchored.add(catr);
	}

	public void addLayoutComponentAnchoredToRight(Component comp, Component anchor, int xPadding, int yPadding) {
		ComponentAnchoredToRight catr = new ComponentAnchoredToRight();
		catr.comp = comp;
		catr.anchor = anchor;
		catr.xPadding = xPadding;
		catr.yPadding = yPadding;
		componentAnchoredToRights.add(catr);
	}

	public void addLayoutComponentAnchoredToBottom(Component comp, Component anchor, int xPadding, int yPadding) {
		ComponentAnchoredToBottom catr = new ComponentAnchoredToBottom();
		catr.comp = comp;
		catr.anchor = anchor;
		catr.xPadding = xPadding;
		catr.yPadding = yPadding;
		componentAnchoredToBottoms.add(catr);
	}

	@Override
	public void layoutContainer(Container parent) {
		for (ComponentWithRelativeRectangle cwrr : componentWithRelativeRectangles) {
			float x = (float) parent.getWidth() * cwrr.x;
			float y = (float) parent.getHeight() * cwrr.y;
			float w = (float) parent.getWidth() * cwrr.w;
			float h = (float) parent.getHeight() * cwrr.h;

			int xi = (int) (x + 0.5f);
			int wi = (int) (x + w + 0.5f) - xi;

			int yi = (int) (y + 0.5f);
			int hi = (int) (y + h + 0.5f) - yi;

			cwrr.comp.setBounds(xi, yi, wi, hi);
		}
		for (ComponentWithRelativeLocation cwrl : componentWithRelativeLocations) {
			float x = (float) parent.getWidth() * cwrl.x;
			float y = (float) parent.getHeight() * cwrl.y;

			int xi = (int) (x + 0.5f);
			int yi = (int) (y + 0.5f);

			cwrl.comp.setBounds(xi, yi, cwrl.comp.getPreferredSize().width, cwrl.comp.getPreferredSize().height);
		}
		
		callComponentBoundsSetters(parent);
		
		ArrayList<Component> toBePlacedComponents = new ArrayList<Component>();
		for (int i = 0; i < componentAnchoredToRights.size(); i++) {
			toBePlacedComponents.add(componentAnchoredToRights.get(i).comp);
		}
		for (int i = 0; i < componentAnchoredToBottoms.size(); i++) {
			toBePlacedComponents.add(componentAnchoredToBottoms.get(i).comp);
		}
		for (int i = 0; i < componentAnchored.size(); i++) {
			toBePlacedComponents.add(componentAnchored.get(i).comp);
		}
		boolean aComponentHasBeenPlaced = true;
		while (!toBePlacedComponents.isEmpty() && aComponentHasBeenPlaced) {
			aComponentHasBeenPlaced = false;
			for (int i = 0; i < componentAnchored.size(); i++) {
				if (toBePlacedComponents.contains(componentAnchored.get(i).comp)
						&& !toBePlacedComponents.contains(componentAnchored.get(i).anchor)) {
					ComponentAnchored ca = componentAnchored.get(i);
					ca.comp.setBounds(
							ca.anchor.getX() + ca.xPadding,
							ca.anchor.getY() + ca.yPadding,
							ca.comp.getPreferredSize().width,
							ca.comp.getPreferredSize().height);
					toBePlacedComponents.remove(ca.comp);
					aComponentHasBeenPlaced = true;
				}
			}
			for (int i = 0; i < componentAnchoredToBottoms.size(); i++) {
				if (toBePlacedComponents.contains(componentAnchoredToBottoms.get(i).comp)
						&& !toBePlacedComponents.contains(componentAnchoredToBottoms.get(i).anchor)) {
					ComponentAnchoredToBottom catb = componentAnchoredToBottoms.get(i);
					catb.comp.setBounds(
							catb.anchor.getX() + catb.xPadding,
							catb.anchor.getY() + catb.anchor.getHeight() + catb.yPadding,
							catb.comp.getPreferredSize().width,
							catb.comp.getPreferredSize().height);
					toBePlacedComponents.remove(catb.comp);
					aComponentHasBeenPlaced = true;
				}
			}
			for (int i = 0; i < componentAnchoredToRights.size(); i++) {
				if (toBePlacedComponents.contains(componentAnchoredToRights.get(i).comp)
						&& !toBePlacedComponents.contains(componentAnchoredToRights.get(i).anchor)) {
					ComponentAnchoredToRight catr = componentAnchoredToRights.get(i);
					catr.comp.setBounds(
							catr.anchor.getX() + catr.anchor.getWidth() + catr.xPadding,
							catr.anchor.getY() + catr.yPadding,
							catr.comp.getPreferredSize().width,
							catr.comp.getPreferredSize().height);
					toBePlacedComponents.remove(catr.comp);
					aComponentHasBeenPlaced = true;
				}
			}
		}
	}

	private void callComponentBoundsSetters(Container parent) {
		for (ComponentBoundsSetter componentBoundsSetter : componentBoundsSetters) {
			componentBoundsSetter.layoutContainer(parent);
		}
	}

	public interface ComponentBoundsSetter {
		public void layoutContainer(Container parent);
	}

	public void addComponentBoundsSetter(ComponentBoundsSetter componentBoundsSetter) {
		componentBoundsSetters.add(componentBoundsSetter);
	}

	public void removeComponentBoundsSetter(ComponentBoundsSetter componentBoundsSetter) {
		componentBoundsSetters.remove(componentBoundsSetter);
	}
	
	@Override
	public void removeLayoutComponent(Component comp) {
		ArrayList<Object> elementToRemove = new ArrayList<Object>();
		for (ComponentWithRelativeRectangle componentWithRelativeRectangle : componentWithRelativeRectangles) {
			if (componentWithRelativeRectangle.comp == comp) {
				elementToRemove.add(componentWithRelativeRectangle);
			}
		}
		for (ComponentWithRelativeLocation componentWithRelativeLocation : componentWithRelativeLocations) {
			if (componentWithRelativeLocation.comp == comp) {
				elementToRemove.add(componentWithRelativeLocation);
			}
		}
		for (ComponentAnchoredToRight componentAnchoredToRight : componentAnchoredToRights) {
			if (componentAnchoredToRight.comp == comp) {
				elementToRemove.add(componentAnchoredToRight);
			}
		}
		for (ComponentAnchoredToBottom componentAnchoredToBottom : componentAnchoredToBottoms) {
			if (componentAnchoredToBottom.comp == comp) {
				elementToRemove.add(componentAnchoredToBottom);
			}
		}
		componentWithRelativeRectangles.removeAll(elementToRemove);
		componentWithRelativeLocations.removeAll(elementToRemove);
		componentAnchoredToRights.removeAll(elementToRemove);
		componentAnchoredToBottoms.removeAll(elementToRemove);
	}

	private class ComponentWithRelativeRectangle {
		public Component comp;
		public float x;
		public float y;
		public float w;
		public float h;
	}

	private class ComponentWithRelativeLocation {
		public Component comp;
		public float x;
		public float y;
	}

	private class ComponentAnchored {
		public Component comp;
		public Component anchor;
		public int xPadding;
		public int yPadding;
	}

	private class ComponentAnchoredToRight {
		public Component comp;
		public Component anchor;
		public int xPadding;
		public int yPadding;
	}

	private class ComponentAnchoredToBottom {
		public Component comp;
		public Component anchor;
		public int xPadding;
		public int yPadding;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension();
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return parent.getSize();
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		super.addLayoutComponent(name, comp);
	}

}
