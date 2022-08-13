package com.github.sampeterson1.renderEngine.gui;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIMaster {
	
	private static Map<String, GUIComponent> componentsByName = new HashMap<String, GUIComponent>();
	private static List<GUIEventListener> listeners;
	
	private static GUIComponent root;
	
	public static void addComponent(GUIComponent component) {
		String name = component.getName();
		componentsByName.put(name, component);
	}
	
	public static Collection<GUIComponent> getComponents() {
		return componentsByName.values();
	}
	
	public static GUIComponent getComponent(String name) {
		return componentsByName.get(name);
	}
	
	public static void invokeEvent(GUIEvent e) {
		for(GUIEventListener listener : listeners) {
			listener.handleEvent(e);
		}
	}
	
	public static GUIComponent getRoot() {
		return root;
	}
}
