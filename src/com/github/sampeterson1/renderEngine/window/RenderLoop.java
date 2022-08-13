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
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.rendering.CameraSettings;
import com.github.sampeterson1.renderEngine.rendering.MasterRenderer;
import com.github.sampeterson1.renderEngine.rendering.OrbitalCamera;
import com.github.sampeterson1.renderEngine.rendering.Scene;
import com.github.sampeterson1.renderEngine.text.Font;
import com.github.sampeterson1.renderEngine.text.Text;

public class RenderLoop implements Runnable {

	private PuzzleDisplay display;
	private OrbitalCamera camera;
	private MasterRenderer renderer;
	
	private float lastX = 0;
	private float lastY = 0;
	
	private float rotX = 0;
	private float rotY = 0;
	
	private float cameraDist = 50f;
	
	private int size = 30;
	private int movePointer = 0;
	private int movesPerFrame = 50;
	
	private Algorithm alg;
	private Puzzle puzzle;

	@Override
	public void run() {
		Window.initGLContext();
		CameraSettings settings = new CameraSettings();
		Scene.setCameraSettings(settings);
		Scene.setLightDirection(new Vector3f(0f, 0f, 1f));
		
		float fontSize = 0.35f;
		Font arial = new Font("arial.fnt", "arial.png", fontSize);
		Font calibri = new Font("calibri.fnt", "calibri.png", fontSize);
		Font harrington = new Font("harrington.fnt", "harrington.png", fontSize);
		Font sans = new Font("sans.fnt", "sans.png", fontSize);
		Font segoe = new Font("segoe.fnt", "segoe.png", fontSize);
		Font segoeUI = new Font("segoeUI.fnt", "segoeUI.png", fontSize);
		Font tahoma = new Font("tahoma.fnt", "tahoma.png", fontSize);
		String text = "Lorem ipsum dolor sit amet";
		
		float xOff = -0.95f;
		float yOff = 0.75f;
		
		new Text("arial", xOff, -0.21f + yOff, text, arial);
		new Text("calibri", xOff, -0.14f + yOff, text, calibri);
		new Text("harrington", xOff, -0.07f + yOff, text, harrington);
		new Text("sans", xOff, yOff, text, sans);
		new Text("segoe", xOff, 0.07f + yOff, text, segoe);
		new Text("segoeUI", xOff, 0.14f + yOff, text, segoeUI);
		new Text("tahoma", xOff, 0.21f + yOff, text, tahoma);
		
		CubeUtil.init();
		Pyraminx.init();
		this.display = new PuzzleDisplay(new Cube(size), 450f);
		display.setAnimate(false);
		display.setAnimationSpeed(30);
		this.puzzle = new Cube(size);
		
		this.camera = new OrbitalCamera(50f);
		renderer = new MasterRenderer(camera);

        while(Window.isOpen()) { 	
			Window.clear();		
			handleEvents();
			render();		
			Window.update();		
        }
        
        renderer.dispose();
		Loader.free();
	}
	
	private void makeMove(Move move) {
		puzzle.makeMove(move);
		display.makeMove(move);
	}
	
	private void render() {
		for(int i = 0; i < movesPerFrame; i ++) {
			if(!display.isAnimating() && alg != null) {
				if(movePointer < alg.length()) {
					display.makeMove(alg.getMove(movePointer));
					movePointer ++;
				} else {
					movePointer = 0;
					alg = null;
				}
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
			alg = puzzle.scramble(1000);
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
