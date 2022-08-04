package com.github.yoshiapolis.renderEngine.window;

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
	
	public static Event fromKeyCallback(int key, int scancode, int action, int mods) {
		Event event = null;
		if(key != GLFW.GLFW_KEY_UNKNOWN) {
			event = new Event();
			
			event.keyCode = key;
			event.keyChar = (char) key;
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
		
		return event;
	}
	
	public static Event fromCursorPosCallback(double xPos, double yPos) {
		Event event = new Event();
		
		event.type = currentMouseDown ? EVENT_MOUSE_DRAG : EVENT_MOUSE_MOVE;
		
		currentMouseX = (int) xPos;
		currentMouseY = (int) yPos;
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

	public boolean hasMod(int mod) {
		int modBit = (this.mods >> mod) & LSD_BITMASK;
		return (modBit == 1);
	}

}
