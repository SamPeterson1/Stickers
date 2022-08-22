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
import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.display.PuzzleDisplay;
import com.github.sampeterson1.puzzle.display.PuzzleMaster;
import com.github.sampeterson1.puzzle.lib.Algorithm;
import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.GroupedPuzzle;
import com.github.sampeterson1.pyraminx.pieces.Pyraminx;
import com.github.sampeterson1.renderEngine.gui.GUICheckbox;
import com.github.sampeterson1.renderEngine.gui.GUIDropdownBox;
import com.github.sampeterson1.renderEngine.gui.GUIEvent;
import com.github.sampeterson1.renderEngine.gui.GUIEventListener;
import com.github.sampeterson1.renderEngine.gui.GUIEventType;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.gui.GUISlider;
import com.github.sampeterson1.renderEngine.gui.PuzzleControlGUI;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.rendering.CameraSettings;
import com.github.sampeterson1.renderEngine.rendering.MasterRenderer;
import com.github.sampeterson1.renderEngine.rendering.OrbitalCamera;
import com.github.sampeterson1.renderEngine.rendering.Scene;
import com.github.sampeterson1.renderEngine.text.Font;

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
	
	private float r, g, b;
	private float maxR=1, maxG=1, maxB=1;

	private Algorithm alg;
	private GroupedPuzzle puzzle;

	@Override
	public void run() {
		Window.initGLContext();
		CameraSettings settings = new CameraSettings();
		Scene.setCameraSettings(settings);
		Scene.setLightDirection(new Vector3f(0f, 0f, 1f));

		Font arial = new Font("arial.fnt", "arial.png", 0.3f);
		
		/*
		GUIButton redButton = new GUIButton("redButton", 0.13f, 0.5f, 0.2f, 0.05f);
		redButton.createLabel("Red!", arial);
		redButton.setBaseColor(new Vector3f(1, 0, 0));
		redButton.setHighlightColor(new Vector3f(1, 0.3f, 0.3f));
		redButton.setShadowColor(new Vector3f(0.8f, 0, 0));
		
		GUIButton greenButton = new GUIButton("greenButton", 0.43f, 0.5f, 0.2f, 0.05f);
		greenButton.createLabel("Green!", arial);
		greenButton.setBaseColor(new Vector3f(0, 1, 0));
		greenButton.setHighlightColor(new Vector3f(0.3f, 1.0f, 0.3f));
		greenButton.setShadowColor(new Vector3f(0, 0.8f, 0));
		
		GUIButton blueButton = new GUIButton("blueButton", 0.73f, 0.5f, 0.2f, 0.05f);
		blueButton.createLabel("Blue!", arial);
		blueButton.setBaseColor(new Vector3f(0, 0, 1));
		blueButton.setHighlightColor(new Vector3f(0.3f, 0.3f, 1.0f));
		blueButton.setShadowColor(new Vector3f(0, 0, 0.8f));
		
		GUISlider redSlider = new GUISlider("redSlider", 0.2f - 0.125f, 0.43f, 0.25f);
		redSlider.setHandleBaseColor(new Vector3f(1, 0, 0));
		redSlider.setHandleHighlightColor(new Vector3f(1, 0.3f, 0.3f));
		
		GUISlider greenSlider = new GUISlider("greenSlider", 0.5f - 0.125f, 0.43f, 0.25f);
		greenSlider.setHandleBaseColor(new Vector3f(0, 1, 0));
		greenSlider.setHandleHighlightColor(new Vector3f(0.3f, 1.0f, 0.3f));
		
		GUISlider blueSlider = new GUISlider("blueSlider", 0.8f - 0.125f, 0.43f, 0.25f);
		blueSlider.setHandleBaseColor(new Vector3f(0, 0, 1));
		blueSlider.setHandleHighlightColor(new Vector3f(0.3f, 0.3f, 1.0f));
		
		GUICheckbox redBox = new GUICheckbox("redBox", 0.065f, 0.5f, 0.05f);
		redBox.setChecked(true);
		redBox.setCheckedColor(new Vector3f(1, 0, 0));
		redBox.setCheckedHighlightColor(new Vector3f(1.0f, 0.3f, 0.3f));
		
		GUICheckbox greenBox = new GUICheckbox("greenBox", 0.365f, 0.5f, 0.05f);
		greenBox.setChecked(true);
		greenBox.setCheckedColor(new Vector3f(0, 1, 0));
		greenBox.setCheckedHighlightColor(new Vector3f(0.3f, 1.0f, 0.3f));
		
		GUICheckbox blueBox = new GUICheckbox("blueBox", 0.665f, 0.5f, 0.05f);
		blueBox.setChecked(true);
		blueBox.setCheckedColor(new Vector3f(0, 0, 1));
		blueBox.setCheckedHighlightColor(new Vector3f(0.3f, 0.3f, 1.0f));
		*/
		
		new PuzzleControlGUI();
		CubeUtil.init();
		Pyraminx.init();
		//this.display = new PuzzleDisplay(new Cube(size));
		//display.setAnimate(true);
		//display.setAnimationSpeed(20);
		//this.puzzle = new Cube(size);
		
		
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
		PuzzleMaster.update();
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
				} else if(e.getType() == Event.EVENT_SCROLL) {
					mouseScrolled(e);
				}
			}
		}
		Window.unlockEvents();
	}
	
	private void mouseDragged(Event e) {
		rotY -= Mathf.min(0.01f, (e.getMouseX() - lastX) / 100.0f);
		rotX += Mathf.min(0.01f, (e.getMouseY() - lastY) / 100.0f);
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

		if(move != null && !display.isAnimating()) {
			makeMove(move);
		}
	}

	@Override
	public void handleEvent(GUIEvent e) {
		
		String name = e.getComponent().getName();
		GUIEventType eventType = e.getType();

		if(eventType == GUIEventType.BUTTON_RELEASE) {
			if(name.equals("redButton")) {
				r += 0.1f;
				r = Mathf.min(r, 1);
				GUISlider slider = (GUISlider) GUIMaster.getComponent("redSlider");
				slider.setValue(r);
			} else if(name.equals("greenButton")) {
				g += 0.1f;
				g = Mathf.min(g, 1);
				GUISlider slider = (GUISlider) GUIMaster.getComponent("greenSlider");
				slider.setValue(g);
			} else if(name.equals("blueButton")) {
				b += 0.1f;
				b = Mathf.min(b, 1);
				GUISlider slider = (GUISlider) GUIMaster.getComponent("blueSlider");
				slider.setValue(b);
			}		System.out.println(name);

		} else if(eventType == GUIEventType.SLIDER_MOVED) {
			GUISlider slider = (GUISlider) e.getComponent();
			if(name.equals("redSlider")) {
				r = slider.getValue();
			} else if(name.equals("greenSlider")) {
				g = slider.getValue();
			} else if(name.equals("blueSlider")) {
				b = slider.getValue();
			}
		} else if(eventType == GUIEventType.CHECKBOX_TOGGLE) {
			GUICheckbox checkbox = (GUICheckbox) e.getComponent();
			int val = checkbox.isChecked() ? 1 : 0;
			if(name.equals("redBox")) {
				maxR = val;
			} else if(name.equals("greenBox")) {
				maxG = val;
			} else if(name.equals("blueBox")) {
				maxB = val;
			}
		} else if(eventType == GUIEventType.DROPDOWN_SELECTED) {
			
		}

		Window.setBackgroundColor(Mathf.min(r, maxR), Mathf.min(g, maxG), Mathf.min(b, maxB));
	}
	
}
