/*
    PrimePuzzle Twisty Puzzle Simulator
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

package com.yoshiapolis.ui;

import com.yoshiapolis.cube.megaminx.Megaminx;
import com.yoshiapolis.cube.megaminx.MegaminxStickerPlacement;
import com.yoshiapolis.cube.pieces.Cube;
import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PuzzleDisplay;

import processing.core.PApplet;
import processing.video.Capture;

public class CubeSolver extends PApplet {

	PuzzleDisplay display;
	CubeVisualizer visual;
	Cube cube;
	Algorithm alg;
	
	int caseNum = 0;
	
	int size = 40;
	int layer = 0;
	int movesPerFrame = 10;
	int movePointer = 0;
		
	boolean cw = true;
	boolean scrambling = false;
	boolean scrambleSimple = false;
	boolean animate = false;
	
	float rotX = 0;
	float rotY = 0;
	
	int clickX = 0;
	int clickY = 0;
	
	float animationSpeed = 30;
	
	Capture cam; 
		
	public static void main(String[] args) {
		PApplet.main(new String[] {CubeSolver.class.getName()});
	}
	
	public void settings() {
		size(1000, 1000, P3D);
	}
	
	public void setup() {
		Cube.init();
		cube = new Cube(size);
		//display = new PuzzleDisplay(this, new PyraminxStickerPlacement(), size, 500);
		//display = new PuzzleDisplay(this, new CubeStickerPlacement(), size, 400);
		display = new PuzzleDisplay(this, new MegaminxStickerPlacement(), size, 200);
		display.setAnimate(animate);
		display.setAnimationSpeed(animationSpeed);
	}
	
	public void draw() {
		//noStroke();
		strokeWeight(2);
		background(0);
		
		if(!display.isAnimating()) {
			int num = animate ? 1 : movesPerFrame;
			for(int i = 0; i < num; i ++) {
				if(scrambling) {
					randMove(Megaminx.faces);
				}
			}
		}
		
		translate(width/2, height/2);
		rotateX(rotX);
		rotateY(rotY);
		display.show();
	}
	
	private void randMove(Face[] faces) {
		int i = (int)random(0, faces.length);
		Face f = faces[i];
		
		int layer = (int)random(0, size);
		if(scrambleSimple) layer = 0;
		boolean cw = (random(1) < 0.5); 
		
		Move move = new Move(f, layer, cw);
		makeMove(move);
	}
	
	private void makeMove(Move move) {
		display.makeMove(move);
	}
	
	public void mouseDragged() {
		float dx = mouseX - clickX;
		float dy = mouseY - clickY;
		
		rotX -= 5*dy/height;
		rotY += 5*dx/width;
		
		clickX = mouseX;
		clickY = mouseY;
	}
	
	public void mouseReleased() {
		float dx = mouseX - clickX;
		float dy = mouseY - clickY;
		
		rotX -= 5*dy/height;
		rotY += 5*dx/width;
	}
	
	public void mousePressed() {
		clickX = mouseX;
		clickY = mouseY;
	}
	
	public void keyPressed() {
		if(key == 'q') scrambling = !scrambling;
		if(key == 's') {
			alg = cube.solve();
			System.out.println("solved");
		}
		if(key == 'o') {
			cube.makeRotation(Face.F, true);
		}
		if(key == 'p') cube.print();
		
		if(key == 'c') cw = !cw;
		if(layer < size-1 && key == '.') layer ++;
		if(layer > 0 && key == ',') layer --;
		if(key == '=') animationSpeed *= 2;
		if(key == '-') animationSpeed /= 2;
		
		display.setAnimationSpeed(animationSpeed);
		if(!display.isAnimating()) {
			Move move = null;
			
			if(key == 'f') move = new Move(Face.M2, layer, cw);
			if(key == 'u') move = new Move(Face.M3, layer, cw);
			if(key == 'r') move = new Move(Face.M4, layer, cw);
			if(key == 'b') move = new Move(Face.M5, layer, cw);
			if(key == 'd') move = new Move(Face.M1, layer, cw);
			if(key == 'l') move = new Move(Face.M6, layer, cw);
			
			/*
			if(key == 'f') move = new Move(Face.PF, layer, cw);
			if(key == 'l') move = new Move(Face.PL, layer, cw);
			if(key == 'r') move = new Move(Face.PR, layer, cw);
			if(key == 'd') move = new Move(Face.PD, layer, cw);
			*/

			if(move != null) makeMove(move);
		}
	}

}
