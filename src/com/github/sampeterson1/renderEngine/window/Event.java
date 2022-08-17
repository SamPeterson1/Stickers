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

package com.github.sampeterson1.renderEngine.window;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

public class Event {
	
	public static final int EVENT_NONE = 0;
	public static final int EVENT_KEY_PRESS = 1;
	public static final int EVENT_KEY_REPEAT = 2;
	public static final int EVENT_KEY_RELEASE = 3;
	public static final int EVENT_MOUSE_BUTTON_PRESS = 4;
	public static final int EVENT_MOUSE_BUTTON_RELEASE = 5;
	public static final int EVENT_MOUSE_DRAG = 6;
	public static final int EVENT_MOUSE_MOVE = 7;
	public static final int EVENT_SCROLL = 8;
	
	public static final int MOUSE_LEFT_BUTTON = 0;
	public static final int MOUSE_MIDDLE_BUTTON = 2;
	public static final int MOUSE_RIGHT_BUTTON = 1;
	
	public static final int SHIFT = 0;
	public static final int CONTROL = 1;
	public static final int ALT = 2;
	public static final int SUPER = 3;
	public static final int CAPS_LOCK = 4;
	public static final int NUM_LOCK = 5;
	
	private static final int LSD_BITMASK = 0x1;
	
	private static final Map<Character, Character> shiftMap = initShiftMap();
	
	private static int currentMouseX;
	private static int currentMouseY;
	private static boolean currentMouseDown;
	
	private int mouseX;
	private int mouseY;
	
	private double scrollAmount;
	private int mouseButton;
	
	private char keyChar;
	private int keyCode;
	
	private int type = EVENT_NONE;
	private int mods;
	
	private Event() {}
	
	private static Map<Character, Character> initShiftMap() {
		Map<Character, Character> map = new HashMap<Character, Character>();
		
		map.put('`', '~'); map.put('1', '!'); map.put('2', '@');
		map.put('3', '#'); map.put('4', '$'); map.put('5', '%');
		map.put('6', '^'); map.put('7', '&'); map.put('8', '*'); 
		map.put('9', '('); map.put('0', ')'); map.put('-', '_');
		map.put('=', '+'); map.put('[', '{'); map.put(']', '}');
		map.put('\\', '|'); map.put(';', ':'); map.put('\'', '\"');
		map.put(',', '<'); map.put('.', '>'); map.put('/', '?');
		
		return map;
	}
	
	public static Event fromKeyCallback(int key, int scancode, int action, int mods) {
		Event event = null;
		if(key != GLFW.GLFW_KEY_UNKNOWN) {
			event = new Event();
			
			event.keyCode = key;
			event.keyChar = getKeyChar(key, mods);
			event.mods = mods;
			
			if(action == GLFW.GLFW_PRESS) {
				event.type = EVENT_KEY_PRESS;
			} else if(action == GLFW.GLFW_REPEAT) {
				event.type = EVENT_KEY_REPEAT;
			} else if(action == GLFW.GLFW_RELEASE) {
				event.type = EVENT_KEY_RELEASE;
			}
		}
		
		return event;
	}
	
	private static char getKeyChar(int key, int mods) {
		boolean shift = hasMod(Event.SHIFT, mods);
		char keyChar = (char) key;
		
		if(key >= 'A' && key <= 'Z') {
			if(shift) {
				return keyChar;
			} else {
				return Character.toLowerCase(keyChar);
			}
		} else if(key >= 32 && key <= 126) {
			if(shift) {
				if(shiftMap.containsKey(keyChar)) {
					return shiftMap.get(keyChar);
				}
			} else {
				return keyChar;
			}
		}
		
		return (char) 0;
	}
	
	public static Event fromMouseButtonCallback(int button, int action, int mods) {
		Event event = new Event();
		
		event.mouseButton = button;
		event.mods = mods;
		
		if(action == GLFW.GLFW_PRESS) {
			event.type = EVENT_MOUSE_BUTTON_PRESS;
		} else if(action == GLFW.GLFW_RELEASE) {
			event.type = EVENT_MOUSE_BUTTON_RELEASE;
		}
		
		if(button == MOUSE_LEFT_BUTTON) {
			if(event.type == EVENT_MOUSE_BUTTON_PRESS) {
				currentMouseDown = true;
			} else if(event.type == EVENT_MOUSE_BUTTON_RELEASE) {
				currentMouseDown = false;
			}
		}
		
		event.mouseX = currentMouseX;
		event.mouseY = currentMouseY;
		
		return event;
	}
	
	public static Event fromCursorPosCallback(double xPos, double yPos) {
		Event event = new Event();
		
		event.type = currentMouseDown ? EVENT_MOUSE_DRAG : EVENT_MOUSE_MOVE;
		
		currentMouseX = (int) xPos;
		currentMouseY = (int) (Window.getHeight() - yPos);
		event.mouseX = currentMouseX;
		event.mouseY = currentMouseY;
		
		return event;
	}
	
	public static Event fromScrollCallback(double xOffset, double yOffset) {
		Event event = new Event();
		
		event.type = EVENT_SCROLL;
		event.scrollAmount = yOffset;
		
		return event;
	}

	public int getMouseX() {
		return this.mouseX;
	}

	public int getMouseY() {
		return this.mouseY;
	}

	public double getScrollAmount() {
		return this.scrollAmount;
	}

	public int getMouseButton() {
		return this.mouseButton;
	}

	public char getKeyChar() {
		return this.keyChar;
	}

	public int getKeyCode() {
		return this.keyCode;
	}

	public int getType() {
		return this.type;
	}

	private static boolean hasMod(int mod, int mods) {
		int modBit = (mods >> mod) & LSD_BITMASK;
		return (modBit == 1);
	}
	
	public boolean hasMod(int mod) {
		return hasMod(mod, mods);
	}

}
