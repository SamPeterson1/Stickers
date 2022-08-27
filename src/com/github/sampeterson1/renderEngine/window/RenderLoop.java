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

import com.github.sampeterson1.math.Mathf;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.puzzle.display.PuzzleMaster;
import com.github.sampeterson1.puzzles.cube.util.CubeUtil;
import com.github.sampeterson1.puzzles.pyraminx.pieces.Pyraminx;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.gui.PuzzleControlGUI;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.rendering.CameraSettings;
import com.github.sampeterson1.renderEngine.rendering.MasterRenderer;
import com.github.sampeterson1.renderEngine.rendering.OrbitalCamera;
import com.github.sampeterson1.renderEngine.rendering.Scene;

public class RenderLoop implements Runnable {

	private OrbitalCamera camera;
	private MasterRenderer renderer;
	
	private float lastX = 0;
	private float lastY = 0;
	
	private float rotX = 0;
	private float rotY = 0;
	
	private float cameraDist = 50f;

	@Override
	public void run() {
		Window.initGLContext();
		CameraSettings settings = new CameraSettings();
		Scene.setCameraSettings(settings);
		Scene.setLightDirection(new Vector3f(0f, 0f, 1f));

		new PuzzleControlGUI();
		CubeUtil.init();
		Pyraminx.init();
		
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
	
}
