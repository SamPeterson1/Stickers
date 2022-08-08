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

package com.github.sampeterson1.puzzle.lib;

import java.util.List;

public interface PieceBehavior {

	public Piece createPiece(int position, int index, int puzzleSize);
	
	public List<Piece> getAffectedPieces(Move move, PieceGroup group);
	
	public void movePiece(Move move, Piece piece);
	
	public int getNumPieces(int puzzleSize, int position);
	
	public PieceType getType();
}
