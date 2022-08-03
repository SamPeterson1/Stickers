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
/*
package com.github.yoshiapolis.puzzle.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.yoshiapolis.main.Main;
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.Puzzle;

import processing.core.PMatrix3D;
import processing.core.PVector;

public abstract class FaceStickerPlacement implements PiecePlacement {

	public abstract float getMinY(int puzzleSize, float drawSize);
	
	public abstract float getYStep(int puzzleSize, float drawSize);
	
	protected abstract Face[] getFaces();
	
	protected abstract Map<Piece, Sticker> createStickerFace(Puzzle puzzle, float drawSize);
	
	@Override
	public Map<Piece, List<DisplayPiece>> createPuzzlePieces(Puzzle puzzle, float drawSize) {
		Face[] faces = getFaces();
		Map<Piece, Sticker> stickersByPosition = createStickerFace(puzzle, drawSize);
		Map<Piece, List<DisplayPiece>> pieces = new HashMap<Piece, List<DisplayPiece>>();
		
		for(Piece position : stickersByPosition.keySet()) {
			Sticker sticker = stickersByPosition.get(position);
			for(Face f : faces) {
				PMatrix3D rot = f.getRotationMat();
				PVector colorVec = f.getColor().getRGB();
				int color = Main.app.color(colorVec.x, colorVec.y, colorVec.z);
				
				Sticker clone = sticker.cloneTranslation();
				clone.applyRotationMatrix(rot);
				clone.setColor(color);
				
				if(pieces.containsKey(position)) {
					pieces.get(position).add(sticker);
				} else {
					List<DisplayPiece> displayPieces = new ArrayList<DisplayPiece>();
					displayPieces.add(sticker);
					pieces.put(position, displayPieces);
				}
			}
		}
		
		return pieces;
	}
}
*/