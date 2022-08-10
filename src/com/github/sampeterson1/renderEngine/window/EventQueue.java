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

import java.util.LinkedList;
import java.util.Queue;

public class EventQueue {
	
	private static final int MAX_QUEUE_LENGTH = 50;
	
	private volatile boolean lock;
	private volatile Queue<Event> eventQueue;
	
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
		while(lock);
		if(event != null) {
			if(this.eventQueue.size() >= MAX_QUEUE_LENGTH) {
				this.eventQueue.poll();
			}
			this.eventQueue.add(event);
		}
	}
	
	public void lockQueue() {
		this.lock = true;
	}
	
	public void unlockQueue() {
		this.lock = false;
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
