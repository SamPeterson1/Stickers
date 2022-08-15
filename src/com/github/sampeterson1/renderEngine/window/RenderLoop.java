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

import com.github.sampeterson1.cube.util.CubeUtil;
import com.github.sampeterson1.ivyCube.IvyCube;
import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.display.PuzzleDisplay;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Puzzle;
import com.github.sampeterson1.pyraminx.pieces.Pyraminx;
import com.github.sampeterson1.renderEngine.gui.GUIButton;
import com.github.sampeterson1.renderEngine.gui.GUIEvent;
import com.github.sampeterson1.renderEngine.gui.GUIEventListener;
import com.github.sampeterson1.renderEngine.gui.GUIEventType;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.rendering.CameraSettings;
import com.github.sampeterson1.renderEngine.rendering.MasterRenderer;
import com.github.sampeterson1.renderEngine.rendering.OrbitalCamera;
import com.github.sampeterson1.renderEngine.rendering.Scene;
import com.github.sampeterson1.renderEngine.text.Font;
import com.github.sampeterson1.renderEngine.text.FontUtil;
import com.github.sampeterson1.renderEngine.text.Text;

public class RenderLoop implements Runnable, GUIEventListener {

	private PuzzleDisplay display;
	private OrbitalCamera camera;
	private MasterRenderer renderer;
	
	private float lastX = 0;
	private float lastY = 0;
	
	private float rotX = 0;
	private float rotY = 0;
	
	private float cameraDist = 50f;
	
	private int size = 10;
	private int movePointer = 0;
	private int movesPerFrame = 50;
	
	private float intensity = 0;
	private String lastName;
	
	private Algorithm alg;
	private Puzzle puzzle;

	@Override
	public void run() {
		Window.initGLContext();
		CameraSettings settings = new CameraSettings();
		Scene.setCameraSettings(settings);
		Scene.setLightDirection(new Vector3f(0f, 0f, 1f));

		Font arial = new Font("arial.fnt", "arial.png", 0.3f);
		
		GUIButton button1 = new GUIButton("Red!", 0.1f, 0.5f, 0.2f, 0.05f);
		float xOff1 = FontUtil.getWidth(arial, "Red!") / 2.0f;
		Text text1 = new Text("button1Text", "Red!", arial, 0.5f - xOff1 / button1.getWidth(), 0.9f);
		text1.offsetColor = new Vector3f(0.8f, 0, 0);
		text1.offset = new Vector2f(0.006f, 0.006f);
		text1.setParent(button1);
		button1.baseColor = new Vector3f(1, 0, 0);
		button1.highlightColor = new Vector3f(1, 0.3f, 0.3f);
		button1.shadowColor = new Vector3f(0.8f, 0, 0);
		
		GUIButton button2 = new GUIButton("Green!", 0.4f, 0.5f, 0.2f, 0.05f);
		float xOff2 = FontUtil.getWidth(arial, "Green!") / 2.0f;
		Text text2 = new Text("button2Text", "Green!", arial, 0.5f - xOff2 / button2.getWidth(), 0.9f);
		text2.offsetColor = new Vector3f(0, 0.8f, 0);
		text2.offset = new Vector2f(0.006f, 0.006f);
		text2.setParent(button2);
		button2.baseColor = new Vector3f(0, 1, 0);
		button2.highlightColor = new Vector3f(0.3f, 1, 0.3f);
		button2.shadowColor = new Vector3f(0, 0.8f, 0);
		
		GUIButton button3 = new GUIButton("Blue!", 0.7f, 0.5f, 0.2f, 0.05f);
		float xOff3 = FontUtil.getWidth(arial, "Blue!") / 2.0f;
		Text text3 = new Text("button3Text", "Blue!", arial, 0.5f - xOff3 / button3.getWidth(), 0.9f);
		text3.offsetColor = new Vector3f(0, 0, 0.8f);
		text3.offset = new Vector2f(0.006f, 0.006f);
		text3.setParent(button3);
		button3.baseColor = new Vector3f(0, 0, 1);
		button3.highlightColor = new Vector3f(0.3f, 0.3f, 1f);
		button3.shadowColor = new Vector3f(0, 0, 0.8f);
		
		
		CubeUtil.init();
		Pyraminx.init();
		this.display = new PuzzleDisplay(new IvyCube(size), 450f);
		display.setAnimate(true);
		display.setAnimationSpeed(20);
		this.puzzle = new IvyCube(size);
		
		this.camera = new OrbitalCamera(50f);
		renderer = new MasterRenderer(camera);
		
		GUIMaster.addEventListener(this);
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
				GUIMaster.handleEvent(e);
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
			alg = puzzle.scramble(100);
		} else if(key == 'S') {
			alg = puzzle.solve();
		}
		
		Move move = null;

		if (key == 'L')
			move = new Move(Axis.IL, 1, true);
		if (key == 'R')
			move = new Move(Axis.IR, 1, true);
		if (key == 'D')
			move = new Move(Axis.ID, 1, true);
		if (key == 'B')
			move = new Move(Axis.IB, 1, true);

		if (key == 'P')
			puzzle.print();
		
		if(move != null && !display.isAnimating()) {
			makeMove(move);
		}
	}

	@Override
	public void handleEvent(GUIEvent e) {
		if(e.getType() == GUIEventType.BUTTON_RELEASE) {
			String name = e.getComponent().getName();
			if(!name.equals(lastName)) {
				lastName = name;
				intensity = 0;
			}
			intensity += 0.1f;
			if(name.equals("Red!")) {
				Window.setBackgroundColor(intensity, 0, 0);
			} else if(name.equals("Green!")) {
				Window.setBackgroundColor(0, intensity, 0);
			} else if(name.equals("Blue!")) {
				Window.setBackgroundColor(0, 0, intensity);
			}
		}
	}
	
}
