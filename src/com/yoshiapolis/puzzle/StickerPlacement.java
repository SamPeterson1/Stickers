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

package com.yoshiapolis.puzzle;

import java.util.List;

import processing.core.PApplet;

public interface StickerPlacement {
	
	public float getRotationAmt();
	
	public float getMinY(int cubeSize, float drawSize);
	
	public float getYStep(int cubeSize, float drawSize);
	
	public List<PuzzleSticker> createStickerFace(int cubeSize, float drawSize);
	
	public Face[] getFaces();
	
	public void drawLayerBlocker(PApplet app, int layer, int cubeSize, float drawSize);

}
