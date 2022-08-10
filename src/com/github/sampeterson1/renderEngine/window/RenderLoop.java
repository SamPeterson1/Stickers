package com.github.sampeterson1.renderEngine.window;

import org.lwjgl.opengl.GL11;

import com.github.sampeterson1.cube.pieces.Cube;
import com.github.sampeterson1.cube.util.CubeUtil;
import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.display.PuzzleDisplay;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.pyraminx.pieces.Pyraminx;
import com.github.sampeterson1.renderEngine.loaders.ModelLoader;
import com.github.sampeterson1.renderEngine.rendering.CameraSettings;
import com.github.sampeterson1.renderEngine.rendering.OrbitalCamera;
import com.github.sampeterson1.renderEngine.rendering.Renderer;
import com.github.sampeterson1.renderEngine.rendering.Scene;

public class RenderLoop implements Runnable {
	

	private PuzzleDisplay display;
	private OrbitalCamera camera;
	private Renderer renderer;
	
	private float lastX = 0;
	private float lastY = 0;
	
	private float rotX = 0;
	private float rotY = 0;
	
	private float cameraDist = 50f;
	
	private int size = 3;
	private int movePointer = 0;
	
	private Algorithm alg;
	private Puzzle puzzle;

	@Override
	public void run() {
		Window.initGLContext();
		System.out.println(GL11.glGetString(GL11.GL_VERSION));
		CameraSettings settings = new CameraSettings();
		Scene.setCameraSettings(settings);
		Scene.setLightDirection(new Vector3f(0f, 0f, 1f));
		
		CubeUtil.init();
		Pyraminx.init();
		this.display = new PuzzleDisplay(new Cube(size), 450f);
		display.setAnimate(true);
		display.setAnimationSpeed(30);
		this.puzzle = new Cube(size);
		
		this.camera = new OrbitalCamera(50f);
		renderer = new Renderer(camera);

        while(Window.isOpen()) { 	
			Window.clear();		
			handleEvents();
			render();		
			Window.update();		
        }
        
        renderer.cleanUp();
		ModelLoader.cleanUp();
	}
	
	private void makeMove(Move move) {
		puzzle.makeMove(move);
		display.makeMove(move);
	}
	
	private void render() {
		if(!display.isAnimating() && alg != null) {
			if(movePointer < alg.length()) {
				display.makeMove(alg.getMove(movePointer));
				movePointer ++;
			} else {
				movePointer = 0;
				alg = null;
			}
		}
		
		display.update();
		renderer.render();	
	}
	
	private void handleEvents() {
		Window.lockEvents();
		while(Window.hasEventToProcess()) {
			Event e = Window.getEvent();
			if(e != null) {
				if(e.getType() == Event.EVENT_MOUSE_DRAG) {
					mouseDragged(e);
				} else if(e.getType() == Event.EVENT_MOUSE_BUTTON_PRESS) {
					mousePressed(e);
				} else if(e.getType() == Event.EVENT_KEY_PRESS) {
					keyPressed(e);
				} else if(e.getType() == Event.EVENT_SCROLL) {
					mouseScrolled(e);
				}
			}
		}
		Window.unlockEvents();
	}
	
	private void mouseDragged(Event e) {
		rotY -= Mathf.min(0.01f, (e.getMouseX() - lastX) / 100.0f);
		rotX -= Mathf.min(0.01f, (e.getMouseY() - lastY) / 100.0f);
		lastX = e.getMouseX();
		lastY = e.getMouseY();
		
		camera.setRotX(rotX);
		camera.setRotY(rotY);
	}
	
	private void mouseScrolled(Event e) {
		double scrollAmt = e.getScrollAmount();
		cameraDist -= (float) 2 * scrollAmt;
		camera.setDistance(cameraDist);
	}
	
	private void mousePressed(Event e) {
		if(e.getMouseButton() == Event.MOUSE_LEFT_BUTTON) {
			lastX = e.getMouseX();
			lastY = e.getMouseY();
		}
	}
	
	private void keyPressed(Event e) {
		char key = e.getKeyChar();
		
		if(key == 'Q') {
			alg = puzzle.scramble(30);
		} else if(key == 'S') {
			alg = puzzle.solve();
		}
		
		Move move = null;

		if (key == 'L')
			move = new Move(Axis.PL, 1, true);
		if (key == 'R')
			move = new Move(Axis.PR, 1, true);
		if (key == 'D')
			move = new Move(Axis.PD, 1, true);
		if (key == 'F')
			move = new Move(Axis.PF, 1, true);

		if (key == 'P')
			puzzle.print();
		
		if(move != null && !display.isAnimating()) {
			makeMove(move);
		}
	}
	
}