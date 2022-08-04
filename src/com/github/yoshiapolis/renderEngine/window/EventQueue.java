package com.github.yoshiapolis.renderEngine.window;

import java.util.LinkedList;
import java.util.Queue;

public class EventQueue {
	
	private static final int MAX_QUEUE_LENGTH = 5;
	
	private Queue<Event> eventQueue;
	
	public EventQueue() {
		this.eventQueue = new LinkedList<Event>();
	}
	
	public void handleKeyCallback(int key, int scancode, int action, int mods) {
		addEvent(Event.fromKeyCallback(key, scancode, action, mods));
	}
	
	public void handleMouseButtonCallback(int button, int action, int mods) {
		addEvent(Event.fromMouseButtonCallback(button, action, mods));
	}
	
	public void handleCursorPosCallback(double xPos, double yPos) {
		addEvent(Event.fromCursorPosCallback(xPos, yPos));
	}
	
	public void handleScrollCallback(double xOffset, double yOffset) {
		addEvent(Event.fromScrollCallback(xOffset, yOffset));
	}
	
	private void addEvent(Event event) {
		if(event != null && this.eventQueue.size() < MAX_QUEUE_LENGTH) {
			this.eventQueue.add(event);
		}
	}
	
	public boolean hasEventToProcess() {
		return (this.eventQueue.size() > 0);
	}
	
	public Event pollEvent() {
		return this.eventQueue.poll();
	}
	
	public void flush() {
		this.eventQueue.clear();
	}
}
