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

package com.yoshiapolis.cube.solvers;

import java.util.ArrayList;

import com.yoshiapolis.cube.pieces.Cube;
import com.yoshiapolis.cube.pieces.CubeCenterUtil;
import com.yoshiapolis.puzzle.Algorithm;
import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PuzzlePiece;
import com.yoshiapolis.puzzle.PuzzlePieceGroup;

public class CenterSolver {
	
	private Cube cube;
	private float progress = 0;
	
	public CenterSolver(Cube cube) {
		this.cube = cube;
	}
	
	public ArrayList<PuzzlePiece> findPieces(int index, Color color) {
		ArrayList<PuzzlePiece> retVal = new ArrayList<PuzzlePiece>();
		
		for(Face face : Cube.faces) {
			PuzzlePieceGroup center = cube.getCenter(face);
			int j = index;
			for(int i = 0; i < 4; i ++) {
				PuzzlePiece piece = center.getPiece(j);
				if(piece.getColor() == color) {
					retVal.add(piece);
				}
				j = CubeCenterUtil.rotateCW(j, cube.getSize()-2);
			}
		}
		
		return retVal;
	}
	
	public PuzzlePiece getUnsolvedPiece(ArrayList<PuzzlePiece> pieces, int line, int index, boolean vertical) {
		int size = cube.getSize() - 2;
		for(PuzzlePiece p : pieces) {
			//ignore the center piece
			if(size % 2 != 1 || p.getIndex() != size*size/2) { 
				//ignore solved pieces on the U face
				if(!(p.getIndex() % size < line && Cube.getFace(p.getPosition()) == Face.U)) { 
					//ignore solved pieces on the F face
					if(!vertical && !(p.getIndex() / size == size-line-1 && Cube.getFace(p.getPosition()) == Face.F)) {
						return p;
					} else if(vertical && !(p.getIndex() % size == line && Cube.getFace(p.getPosition()) == Face.F)) {
						return p;
					}
				}
			} else {
				return null;
			}
			if(p.getIndex() == index && Cube.getFace(p.getPosition()) == Face.F) {
				return null;
			}
		}
		
		return null;
	}

	//moves a center piece from the U face or the D face
	public void moveCenter_UD(PuzzlePiece piece, int line, boolean safe) {
		Face face = Cube.getFace(piece.getPosition());
		int size = cube.getSize() - 2;
		
		if(!safe && CubeCenterUtil.getLayer(piece, Face.L, size)-1 > line && (size % 2 == 0 || CubeCenterUtil.getLayer(piece, Face.L, size)-1 != size/2)) {
			if(face == Face.U) {
				cube.makeMove(new Move(Face.R, CubeCenterUtil.getLayer(piece, Face.R, size), true));
			} else if(face == Face.D) {
				cube.makeMove(new Move(Face.R, CubeCenterUtil.getLayer(piece, Face.R, size), false));
			}
		} else {
			cube.makeMove(new Move(Face.U, 0, true));
			int layer = CubeCenterUtil.getLayer(piece, Face.F, size);
			cube.makeMove(new Move(Face.F, layer, true));
			if(safe || CubeCenterUtil.getLayer(piece, Face.B, size) - 1 < line || (size % 2 == 1 && CubeCenterUtil.getLayer(piece, Face.F, size) - 1 == size/2)) {
				while(CubeCenterUtil.getLayer(piece, Face.F, size) == layer) {
					cube.makeMove(new Move(Face.R, CubeCenterUtil.getLayer(piece, Face.R, size), true));
				}
				cube.makeMove(new Move(Face.F, layer, false));
			}
			cube.makeMove(new Move(Face.U, 0, false));
			cube.makeMove(new Move(Face.R, CubeCenterUtil.getLayer(piece, Face.R, size), true));
		}
	}
	
	//move a center piece from the F face if the lines are constructed vertically
	public void moveCenterVertical_F(PuzzlePiece piece, int index) {
		int size = cube.getSize() - 2;
		if(CubeCenterUtil.getLayer(piece, Face.U, size)-1 < index) {
			cube.makeMove(new Move(Face.F, 0, true));
			cube.makeMove(new Move(Face.U, CubeCenterUtil.getLayer(piece, Face.U, size), true));
			cube.makeMove(new Move(Face.F, 0, false));
		} else {
			cube.makeMove(new Move(Face.U, CubeCenterUtil.getLayer(piece, Face.U, size), true));
		}
	}
	
	//move a center piece from the F face if the lines are constructed horizontally
	public void moveCenterHorizontal_F(PuzzlePiece piece) {
		int size = cube.getSize() - 2;
		cube.makeMove(new Move(Face.F, 0, true));
		int layer = CubeCenterUtil.getLayer(piece, Face.R, size);
		cube.makeMove(new Move(Face.R, layer, false));
		while(CubeCenterUtil.getLayer(piece, Face.R, size) == layer) {
			cube.makeMove(new Move(Face.D, 0, true));
		}
		cube.makeMove(new Move(Face.R, layer, true));
		cube.makeMove(new Move(Face.F, 0, false));
	}
	
	
	//move a center piece from the U face if the lines are constructed horizontally
	public void moveCenterHorizontal_U(PuzzlePiece piece, int index, boolean safe) {
		int size = cube.getSize() - 2;
		int layer = CubeCenterUtil.getLayer(piece, Face.R, size);
		if(safe) {
			cube.makeMove(new Move(Face.R, layer, true));
			cube.makeMove(new Move(Face.R, layer, true));
			
			if(size-layer-2 < index % size || safe) {
				while(CubeCenterUtil.getLayer(piece, Face.R, size) == layer) {
					cube.makeMove(new Move(Face.D, 0, true));
				}
				cube.makeMove(new Move(Face.R, layer, false));
				cube.makeMove(new Move(Face.R, layer, false));
			}
		} else {
			cube.makeMove(new Move(Face.R, layer, true));
			
			if(size-layer-2 < index % size || safe) {
				while(CubeCenterUtil.getLayer(piece, Face.R, size) == layer) {
					cube.makeMove(new Move(Face.B, 0, true));
				}
				cube.makeMove(new Move(Face.R, layer, false));
			}
		}
	}
	
	public void insertHorizontalLine(Color color, int line, boolean safe) {
		int size = cube.getSize() - 2;
		if(line == size/2 && size % 2 == 1) {
			while(cube.getCenter(Face.F).getPiece(size*size/2).getColor() != color) {
				cube.makeMove(new Move(Face.R, size/2 + 1, false));
			}
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.R, size/2 + 1, true));
		} else if(safe) {
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.L, line+1, true));
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.L, line+1, false));
		} else {
			//regular case
			cube.makeMove(new Move(Face.F, 0, true));
			cube.makeMove(new Move(Face.L, line+1, false));
		}
	}
	
	public void insertVerticalLine(int line, boolean safe) {
		int size = cube.getSize() - 2;
		if(line == size/2 && size % 2 == 1) {
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.R, size/2 + 1, false));
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.R, size/2 + 1, true));
		} else if(safe) {
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.L, line+1, true));
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.F, 0, false));
			cube.makeMove(new Move(Face.L, line+1, false));	
		} else {
			cube.makeMove(new Move(Face.L, line+1, false));
		}
	}
	
	public Algorithm solve() {
		
		if(cube.getSize() < 4) return new Algorithm();
		
		cube.clearMoveLog();
		cube.setLogMoves(true);
		cube.pushRotations();
		
		step1();
		step2();
		step3();
		
		System.out.println("domne");
		cube.popRotations();
		System.out.println("poppp");
		cube.setLogMoves(false);
		Algorithm moves = cube.getMoveLog();
		moves.simplify();
				
		return moves;
	}
	
	private void printProgress() {
		System.out.println("Solving... " + ((int)(progress*100)) + "%");
	}
	
	//solve last two centers
	public void step3() {
		
		int size = cube.getSize()-2;
		
		Color color;
		if(size % 2 == 1) {
			color = cube.getCenter(Face.U).getPiece(size*size/2).getColor();
		} else {
			color = Color.GREEN;
		}
		
		PuzzlePieceGroup uCenter = cube.getCenter(Face.U);
		for(int i = 0; i < size * size; i ++) {
			if(i % size == 0) {
				progress += 1.0f / (size*5);
				printProgress();
			}
			
			PuzzlePiece toReplace = uCenter.getPiece(i);
			if(toReplace.getColor() != color) {
				ArrayList<PuzzlePiece> pieces = findPieces(i, color);
				PuzzlePiece toMove = null;
				//find a pair of pieces to exchange
				for(PuzzlePiece piece: pieces) {
					if(Cube.getFace(piece.getPosition()) == Face.F) {
						toMove = piece;
						break;
					}
				}
				
				//move the F face until the 2 pieces allign
				while(toMove.getIndex() != i) {
					cube.makeMove(new Move(Face.F, 0, true));
				}
				
				//use a commutator to exchange 2 unsolved center pieces
				int layer1 = CubeCenterUtil.getLayer(toMove, Face.R, size);
				cube.makeMove(new Move(Face.R, layer1, true));
				cube.makeMove(new Move(Face.U, 0, true));
				boolean cw = true;
				if(CubeCenterUtil.getLayer(toMove, Face.R, size) == layer1) {
					cube.makeMove(new Move(Face.U, 0, false));
					cube.makeMove(new Move(Face.U, 0, false));
					cw = false;
				}
				int layer2 = CubeCenterUtil.getLayer(toMove, Face.R, size);
				cube.makeMove(new Move(Face.R, layer2, true));
				cube.makeMove(new Move(Face.U, 0, !cw));
				cube.makeMove(new Move(Face.R, layer1, false));
				cube.makeMove(new Move(Face.U, 0, cw));
				cube.makeMove(new Move(Face.R, layer2, false));
				cube.makeMove(new Move(Face.U, 0, !cw));
			}
		}
		
		cube.makeRotation(Face.R, true);
	}
	
	//solve two adjacent centers
	public void step2() {
		
		int size = cube.getSize()-2;
		
		for(int i = 0; i < 2; i ++) {
			boolean safe = (i == 1);
			Color color;
			if(size % 2 == 1) {
				color = cube.getCenter(Face.U).getPiece(size*size/2).getColor();
			} else if(i == 1) {
				color = Color.ORANGE;
			} else {
				color = Color.BLUE;
			}
			
			for(int line = 0; line < size; line ++) {
				progress += 1.0f / (size*5);
				printProgress();
				
				for(int index = size*(size-line-1); index < size*(size-line); index ++) {
					ArrayList<PuzzlePiece> pieces = findPieces(index, color);
					PuzzlePiece piece = getUnsolvedPiece(pieces, line, index, false);
					
					if(piece != null) {
						if(Cube.getFace(piece.getPosition()) == Face.U) {
							moveCenterHorizontal_U(piece, index, safe);
						} else if(Cube.getFace(piece.getPosition()) == Face.F) {
							moveCenterHorizontal_F(piece);
						}
						
						//move piece to the F face in the right position
						int fIndex = CubeCenterUtil.mapIndex(Face.R, Cube.getFace(piece.getPosition()), Face.F, piece.getIndex(), size);
						while(fIndex != index) {
							cube.makeMove(new Move(Cube.getFace(piece.getPosition()), 0, true));
							fIndex = CubeCenterUtil.mapIndex(Face.R, Cube.getFace(piece.getPosition()), Face.F, piece.getIndex(), size);
						}
						
						//insert piece into the horizontal line
						int rMoves = 0;
						int fMoves = 0;
						int rLayer = CubeCenterUtil.getLayer(piece, Face.R, size);
						while(Cube.getFace(piece.getPosition()) != Face.F) {
							cube.makeMove(new Move(Face.R, rLayer, true));
							rMoves ++;
						}
						
						if(index % size < line || safe) {
							while(CubeCenterUtil.getLayer(piece, Face.R, size) == rLayer || fMoves % 2 == 0) {
								cube.makeMove(new Move(Face.F, 0, true));
								fMoves ++;
							}
							for(int j = 0; j < rMoves; j ++) {
								cube.makeMove(new Move(Face.R, rLayer, false));
							}
							for(int j = 0; j < fMoves; j ++) {
								cube.makeMove(new Move(Face.F, 0, false));
							}
						}
					}
				}
				
				insertHorizontalLine(color, line, safe);
			}
			cube.makeRotation(Face.R, true);
		}		
	}
	
	//solve two opposite centers
	public void step1() {

		int size = cube.getSize()-2;
		
		Color color;
		if(size % 2 == 1) {
			color = cube.getCenter(Face.U).getPiece(size*size/2).getColor();
		} else {
			color = Color.WHITE;
		}
		
		for(int i = 0; i < 2; i ++) {
			boolean safe = (i == 1);
			for(int line = 0; line < size; line ++) {
				progress += 1.0f / (size*5);
				printProgress();
				for(int index = line; index < size*size; index += size) {
					ArrayList<PuzzlePiece> pieces = findPieces(index, color);
					PuzzlePiece piece = getUnsolvedPiece(pieces, line, index, true);
					if(piece != null) {
						Face face = Cube.getFace(piece.getPosition());
						if(face == Face.U || face == Face.D) {
							moveCenter_UD(piece, line, safe);
						} else if(face == Face.F) {
							moveCenterVertical_F(piece, index);
						}

						while(piece.getIndex() != index) {
							cube.makeMove(new Move(Cube.getFace(piece.getPosition()), 0, true));
						}
						
						while(Cube.getFace(piece.getPosition()) != Face.F) {
							cube.makeMove(new Move(Face.U, CubeCenterUtil.getLayer(piece, Face.U, size), true));
						}
					}
				}
				
				insertVerticalLine(line, safe);
			}
			
			color = Cube.getOpposingColor(color);
			cube.makeRotation(Face.F, true);
			cube.makeRotation(Face.F, true);
		}		
		
		cube.makeRotation(Face.F, true);
	}
	
}
