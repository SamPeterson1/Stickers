/*
    PrimePuzzle Twisty Puzzle Simulator and Solver
    Copyright (C) 2022 Sam Peterson
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.github.yoshiapolis.main;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import com.github.yoshiapolis.math.Matrix3D;
import com.github.yoshiapolis.math.Vector3f;
import com.github.yoshiapolis.renderEngine.loaders.ModelLoader;
import com.github.yoshiapolis.renderEngine.loaders.OBJLoader;
import com.github.yoshiapolis.renderEngine.models.ColorPalette;
import com.github.yoshiapolis.renderEngine.models.ColoredModel;
import com.github.yoshiapolis.renderEngine.rendering.CameraSettings;
import com.github.yoshiapolis.renderEngine.rendering.Entity;
import com.github.yoshiapolis.renderEngine.rendering.Renderer;
import com.github.yoshiapolis.renderEngine.rendering.Scene;
import com.github.yoshiapolis.renderEngine.window.Event;
import com.github.yoshiapolis.renderEngine.window.Window;

public class Main {
	
	public static void main(String[] args) {
		Window.init(600, 600, "Test");
		ColorPalette.loadPaletteTexture("ColorPalette.png");
		ColoredModel model = OBJLoader.loadColoredModel("cube.obj");
		model.setBaseColor(1);
		model.setAccentColor(4);
		model.setAccentColor("XPos", 2);
		model.setBaseColor("YPos", 3);
		
		Entity entity = new Entity(model);
		
		CameraSettings settings = new CameraSettings();
		Scene.setCameraSettings(settings);
		Scene.setLightDirection(new Vector3f(0f, 0f, 1f));
		Scene.addEntity(entity);
		
		Renderer renderer = new Renderer();
		
		long startTime = System.currentTimeMillis();
		
		while(Window.isOpen()) {
			Window.clear();
			
			Event event = Window.pollEvent();
			if(event.getMouseButton() == Event.MOUSE_LEFT_BUTTON) {
				if(event.getType() == Event.EVENT_MOUSE_BUTTON_PRESS) {
					System.out.println("Down");
				} else if(event.getType() == Event.EVENT_MOUSE_BUTTON_RELEASE) {
					System.out.println("Release");
				} else if(event.getType() == Event.EVENT_MOUSE_DRAG) {
					System.out.println("Drag x: " + event.getMouseX() + " y: " + event.getMouseY());
				}
				
			}
			
			float seconds = (System.currentTimeMillis() - startTime) / 1000.0f;
			Matrix3D mat = new Matrix3D();
			mat.rotateXYZ(seconds, seconds, 0);
			mat.translateZ(-5);
			entity.setTransformationMatrix(mat);
			
			renderer.render();			
			Window.update();
		}
		
		renderer.cleanUp();
		ModelLoader.cleanUp();
	}

}

/*
package com.github.yoshiapolis.main;

import com.github.yoshiapolis.cube.pieces.Cube;
import com.github.yoshiapolis.megaminx.Megaminx;
import com.github.yoshiapolis.puzzle.display.PuzzleDisplay;
import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Puzzle;
import com.github.yoshiapolis.pyraminx.pieces.Pyraminx;

import processing.core.PApplet;

public class Main extends PApplet {
	
	public static PApplet app;
	
	public static void main(String[] args) {
		PApplet.main(new String[] { Main.class.getName() });
	}
	
	PuzzleDisplay display;
	Puzzle puzzle;

	Algorithm alg;
	
	int size = 12;
	int layer = 0;
	int movesPerFrame = 1;
	
	boolean cw = true;
	boolean scrambling = false;
	boolean scrambleSimple = false;
	boolean animate = true;
	
	float rotX = 0;
	float rotY = 0;
	
	int clickX = 0;
	int clickY = 0;

	int movePointer = 0;

	float animationSpeed = 1;
	
	public void settings() {
		size(1000, 1000, P3D);
	}

	public void setup() {
		/*
		Main.app = this;
		Cube.init();
		Pyraminx.init();
		//puzzle = new Pyraminx(size);
		//display = new PuzzleDisplay(this, new PyraminxStickerPlacement(), new PyraminxLayerBlocker(), size, 600);
		display = new PuzzleDisplay(new Cube(size), 400);
		//display = new PuzzleDisplay(this, new MegaminxStickerPlacement(), new MegaminxLayerBlocker(), size, 200);
		display.setAnimate(animate);
		display.setAnimationSpeed(animationSpeed);
		
	}
	
	public void draw() {
		/*
		//noStroke();
		strokeWeight(4);
		background(0);

		if (!display.isAnimating()) {
			int num = animate ? 1 : movesPerFrame;
			for (int i = 0; i < num; i++) {
				if (scrambling) {
					for (int j = 0; j < 1; j++) {
						randMove(Cube.faces);
					}
				}
			}
		}

		if (alg != null && !display.isAnimating()) {
			for (int i = 0; i < 1 && alg != null; i++) {
				if (movePointer < alg.length()) {
					display.setAnimate(true);
					display.makeMove(alg.getMove(movePointer));
					movePointer++;
				} else {
					alg = null;
					movePointer = 0;
				}
			}
		}

		translate(width / 2, height / 2);
		rotateX(rotX);
		rotateY(rotY);
		display.show();
		
	}

	public void keyPressed() {
		if (key == 'q')
			scrambling = !scrambling;
		if (key == 's')
			alg = puzzle.solve();
		if (key == 'p')
			puzzle.print();
		if (key == 'c')
			cw = !cw;
		if (layer < size - 1 && key == '.')
			layer++;
		if (layer > 0 && key == ',')
			layer--;
		if (key == '=')
			animationSpeed *= 2;
		if (key == '-')
			animationSpeed /= 2;

		display.setAnimationSpeed(animationSpeed);
		if (!display.isAnimating()) {
			Move move = null;
			
			if (key == 'f')
				move = new Move(Face.F, layer, cw);
			if (key == 'l')
				move = new Move(Face.L, layer, cw);
			if (key == 'r')
				move = new Move(Face.R, layer, cw);
			if (key == 'd')
				move = new Move(Face.D, layer, cw);
			if (key == 'b')
				move = new Move(Face.B, layer, cw);
			if (key == 'u')
				move = new Move(Face.U, layer, cw);

			/*
			if (key == 'f')
				move = new Move(Face.PF, layer, cw);
			if (key == 'l')
				move = new Move(Face.PL, layer, cw);
			if (key == 'r')
				move = new Move(Face.PR, layer, cw);
			if (key == 'd')
				move = new Move(Face.PD, layer, cw);
			

			if (move != null)
				makeMove(move);
		}
	}

	public void mouseDragged() {
		float dx = mouseX - clickX;
		float dy = mouseY - clickY;

		rotX -= 5 * dy / height;
		rotY += 5 * dx / width;

		clickX = mouseX;
		clickY = mouseY;
	}

	public void mousePressed() {
		clickX = mouseX;
		clickY = mouseY;
	}

	public void mouseReleased() {
		float dx = mouseX - clickX;
		float dy = mouseY - clickY;

		rotX -= 5 * dy / height;
		rotY += 5 * dx / width;
	}

	private void makeMove(Move move) {
		//puzzle.makeMove(move);
		display.makeMove(move);
	}

	private void randMove(Face[] faces) {
		int i = (int) random(0, faces.length);
		Face f = faces[i];

		int layer = (int) random(0, size);
		if (scrambleSimple)
			layer = 0;
		boolean cw = (random(1) < 0.5);

		Move move = new Move(f, layer, cw);
		makeMove(move);
	}

}
*/
