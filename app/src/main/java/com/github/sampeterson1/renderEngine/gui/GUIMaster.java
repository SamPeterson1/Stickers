/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.renderEngine.gui;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.renderEngine.rendering.MeshType;
import com.github.sampeterson1.renderEngine.window.Event;

public class GUIMaster {
	
	private static Map<MeshType, List<GUIComponent>> componentsByType = new EnumMap<MeshType, List<GUIComponent>>(MeshType.class);
	private static Map<String, GUIComponent> componentsByName = new HashMap<String, GUIComponent>();
	private static List<GUIEventListener> listeners = new ArrayList<GUIEventListener>();
		
	private static final float Z_FAR = 10;
	private static final float Z_NEAR = 0;
	public static final Matrix3D orthoProjection = Matrix3D.createOrthoProjectionMatrix(-1, 1, -1, 1, Z_NEAR, Z_FAR);
	
	public static void addEventListener(GUIEventListener listener) {
		listeners.add(listener);
	}
	
	public static void addComponent(GUIComponent component) {
		String name = component.getName();
		componentsByName.put(name, component);
		
		MeshType type = component.getMesh().getType();
		if(componentsByType.containsKey(type)) {
			List<GUIComponent> components = componentsByType.get(type);
			components.add(component);
		} else {
			List<GUIComponent> components = new ArrayList<GUIComponent>();
			components.add(component);
			componentsByType.put(type, components);
		}
	}
	
	public static List<GUIComponent> getComponentsByType(MeshType type) {
		return componentsByType.get(type);
	}
	
	public static GUIComponent getComponent(String name) {
		return componentsByName.get(name);
	}
	
	public static void handleEvent(Event e) {
		for(GUIComponent component : componentsByName.values()) {
			component.handleEvent(e);
		}
	}
	
	public static void createGUIEvent(GUIEvent e) {
		for(GUIEventListener listener : listeners) {
			listener.handleEvent(e);
		}
	}
	
}
