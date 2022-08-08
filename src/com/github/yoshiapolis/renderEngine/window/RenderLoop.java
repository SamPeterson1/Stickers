package com.github.yoshiapolis.renderEngine.window;

import com.github.yoshiapolis.cube.pieces.Cube;
import com.github.yoshiapolis.math.Mathf;
import com.github.yoshiapolis.math.Vector3f;
import com.github.yoshiapolis.puzzle.display.PuzzleDisplay;
import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Axis;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Puzzle;
import com.github.yoshiapolis.pyraminx.pieces.Pyraminx;
import com.github.yoshiapolis.renderEngine.loaders.ModelLoader;
import com.github.yoshiapolis.renderEngine.rendering.CameraSettings;
import com.github.yoshiapolis.renderEngine.rendering.OrbitalCamera;
import com.github.yoshiapolis.renderEngine.rendering.Renderer;
import com.github.yoshiapolis.renderEngine.rendering.Scene;

public class RenderLoop implements Runnable {
	

	private PuzzleDisplay display;
	private OrbitalCamera camera;
	private Renderer renderer;
	
	private float lastX = 0;
	private float lastY = 0;
	
	private float rotX = 0;
	private float rotY = 0;
	
	private float cameraDist = 50f;
	
	private int size = 20;
	private int movePointer = 0;
	
	private Algorithm alg;
	private Puzzle puzzle;
	
	public RenderLoop() {		
		CameraSettings settings = new CameraSettings();
		Scene.setCameraSettings(settings);
		Scene.setLightDirection(new Vector3f(0f, 0f, 1f));
		
		Cube.init();
		Pyraminx.init();
		this.display = new PuzzleDisplay(new Pyraminx(size), 450f);
		display.setAnimate(false);
		display.setAnimationSpeed(20);
		this.puzzle = new Pyraminx(size);
		
		this.camera = new OrbitalCamera(50f);
		renderer = new Renderer(camera);
	}
	
	@Override
	public void run() {
		Window.initGLContext();
		
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
			move = new Move(Axis.PL, 2, true);
		if (key == 'R')
			move = new Move(Axis.PR, 2, true);
		if (key == 'D')
			move = new Move(Axis.PD, 2, true);
		if (key == 'F')
			move = new Move(Axis.PF, 2, true);

		if (key == 'P')
			puzzle.print();
		
		if(move != null && !display.isAnimating()) {
			makeMove(move);
		}
	}
	
}
