package com.github.sampeterson1.renderEngine.window;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class Window {
	
	private static long windowID;
	
	private static float bgRed;
	private static float bgGreen;
	private static float bgBlue;
	
	private static EventQueue eventQueue;
		
	public static void init(int width, int height, String title) {
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		windowID = glfwCreateWindow(width, height, title, NULL, NULL);
		if (windowID == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwMakeContextCurrent(windowID);
		glfwSwapInterval(1);
		glfwShowWindow(windowID);
		setEventCallbacks();
		
        GL.createCapabilities();
	}
	
	public static void initGLContext() {
		System.out.println("Initializing GL Context");
		glfwMakeContextCurrent(windowID);
		GL.createCapabilities();
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
	
	public static float getAspectRatio() {
		int[] size = getSize();
		return (float) size[0] / size[1];
	}
	
	public static int[] getSize() {
		try (MemoryStack stack = stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			
			glfwGetWindowSize(windowID, width, height);
			return new int[] { width.get(0), height.get(0) };
		}
	}
	
	public static boolean isOpen() {
		return !glfwWindowShouldClose(windowID);
	}
	
	public static void clear() {
		glClearColor(bgRed, bgGreen, bgBlue, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public static void update() {
		glfwSwapBuffers(windowID);
	}
}
