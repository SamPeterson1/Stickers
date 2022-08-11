package com.github.sampeterson1.renderEngine.gui;

public class GUIEvent {

	private GUIEventType type;
	private GUIComponent component;
		
	public GUIEvent(GUIEventType type, GUIComponent component) {
		this.type = type;
		this.component = component;
	}

	public GUIEventType getType() {
		return type;
	}
	
	public GUIComponent getComponent() {
		return component;
	}
	
}
