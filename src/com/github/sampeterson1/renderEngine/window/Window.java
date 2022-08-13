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

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {
	
	private static long windowID;
	
	private static float bgRed;
	private static float bgGreen;
	private static float bgBlue;
	
	private static EventQueue eventQueue;
	
	public static void init(String title, float aspect) {
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		long primaryMonitor = GLFW.glfwGetPrimaryMonitor();
		GLFWVidMode vidMode = GLFW.glfwGetVideoMode(primaryMonitor);
		int height = vidMode.height();
		int width = (int) (height * aspect);
		
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

		windowID = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		if (windowID == 0)
			throw new RuntimeException("Failed to create the GLFW window");

		GLFW.glfwMakeContextCurrent(windowID);
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(windowID);
		setEventCallbacks();
		
        GL.createCapabilities();
	}
	
	public static void initGLContext() {
		GLFW.glfwMakeContextCurrent(windowID);
		GL.createCapabilities();
		System.out.println(GL11.glGetString(GL11.GL_VERSION));
	}
	
	private static void setEventCallbacks() {
		eventQueue = new EventQueue();
		
		GLFW.glfwSetKeyCallback(windowID, new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				eventQueue.handleKeyCallback(key, scancode, action, mods);
			}
		});
		GLFW.glfwSetMouseButtonCallback(windowID, new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				eventQueue.handleMouseButtonCallback(button, action, mods);
			}
		});
		GLFW.glfwSetCursorPosCallback(windowID, new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xPos, double yPos) {
				eventQueue.handleCursorPosCallback(xPos, yPos);
			}
		});
		GLFW.glfwSetScrollCallback(windowID, new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xOffset, double yOffset) {
				eventQueue.handleScrollCallback(xOffset, yOffset);
			}
		});
	}
	
	public static void lockEvents() {
		eventQueue.lockQueue();
	}
	
	public static void unlockEvents() {
		eventQueue.unlockQueue();
	}
	
	public static boolean hasEventToProcess() {
		return eventQueue.hasEventToProcess();
	}
	
	public static Event getEvent() {
		return eventQueue.pollEvent();
	}
	
	public static void pollEvents() {
		GLFW.glfwWaitEvents();
	}
	
	public static void setBackgroundColor(float r, float g, float b) {
		bgRed = r;
		bgGreen = g;
		bgBlue = b;
	}

	public static boolean isOpen() {
		return !GLFW.glfwWindowShouldClose(windowID);
	}
	
	public static void clear() {
		GL11.glClearColor(bgRed, bgGreen, bgBlue, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void update() {
		GLFW.glfwSwapBuffers(windowID);
	}
}
