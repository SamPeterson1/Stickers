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

package com.github.sampeterson1.puzzles.cube.solvers;

import java.util.ArrayList;

import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzles.cube.meta.Cube;
import com.github.sampeterson1.puzzles.cube.util.CubeCenterUtil;
import com.github.sampeterson1.puzzles.cube.util.CubeUtil;

/*
 * This class solves the centers on a cube using the reduction method.
 * 
 * This implementation is not meant to be readable.
 * See https://www.youtube.com/watch?v=Uzqo_L-1BMM for more information.
 */
public class CenterSolver {

	private Cube cube;
	private float progress = 0;

	public CenterSolver(Cube cube) {
		this.cube = cube;
	}

	public ArrayList<Piece> findPieces(int index, Color color) {
		ArrayList<Piece> retVal = new ArrayList<Piece>();

		for (Axis face : CubeUtil.getFaces()) {
			PieceGroup center = cube.getCenter(face);
			int j = index;
			for (int i = 0; i < 4; i++) {
				Piece piece = center.getPiece(j);
				if (piece.getColor() == color) {
					retVal.add(piece);
				}
				j = CubeCenterUtil.rotateCW(j, cube.getSize() - 2);
			}
		}

		return retVal;
	}

	public Piece getUnsolvedPiece(ArrayList<Piece> pieces, int line, int index, boolean vertical) {
		int size = cube.getSize() - 2;
		for (Piece p : pieces) {
			// ignore the center piece
			if (size % 2 != 1 || p.getIndex() != size * size / 2) {
				// ignore solved pieces on the U face
				if (!(p.getIndex() % size < line && CubeUtil.getFace(p.getPosition()) == Axis.U)) {
					// ignore solved pieces on the F face
					if (!vertical
							&& !(p.getIndex() / size == size - line - 1 && CubeUtil.getFace(p.getPosition()) == Axis.F)) {
						return p;
					} else if (vertical && !(p.getIndex() % size == line && CubeUtil.getFace(p.getPosition()) == Axis.F)) {
						return p;
					}
				}
			} else {
				return null;
			}
			if (p.getIndex() == index && CubeUtil.getFace(p.getPosition()) == Axis.F) {
				return null;
			}
		}

		return null;
	}

	public void insertHorizontalLine(Color color, int line, boolean safe) {
		int size = cube.getSize() - 2;
		if (line == size / 2 && size % 2 == 1) {
			while (cube.getCenter(Axis.F).getPiece(size * size / 2).getColor() != color) {
				cube.makeMove(new Move(Axis.R, size / 2 + 1, false));
			}
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.R, size / 2 + 1, true));
		} else if (safe) {
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.L, line + 1, true));
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.L, line + 1, false));
		} else {
			// regular case
			cube.makeMove(new Move(Axis.F, 0, true));
			cube.makeMove(new Move(Axis.L, line + 1, false));
		}
	}

	public void insertVerticalLine(int line, boolean safe) {
		int size = cube.getSize() - 2;
		if (line == size / 2 && size % 2 == 1) {
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.R, size / 2 + 1, false));
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.R, size / 2 + 1, true));
		} else if (safe) {
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.L, line + 1, true));
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.F, 0, false));
			cube.makeMove(new Move(Axis.L, line + 1, false));
		} else {
			cube.makeMove(new Move(Axis.L, line + 1, false));
		}
	}

	// moves a center piece from the U face or the D face
	public void moveCenter_UD(Piece piece, int line, boolean safe) {
		Axis face = CubeUtil.getFace(piece.getPosition());
		int size = cube.getSize() - 2;

		if (!safe && CubeCenterUtil.getLayer(piece, Axis.L, size) - 1 > line
				&& (size % 2 == 0 || CubeCenterUtil.getLayer(piece, Axis.L, size) - 1 != size / 2)) {
			if (face == Axis.U) {
				cube.makeMove(new Move(Axis.R, CubeCenterUtil.getLayer(piece, Axis.R, size), true));
			} else if (face == Axis.D) {
				cube.makeMove(new Move(Axis.R, CubeCenterUtil.getLayer(piece, Axis.R, size), false));
			}
		} else {
			cube.makeMove(new Move(Axis.U, 0, true));
			int layer = CubeCenterUtil.getLayer(piece, Axis.F, size);
			cube.makeMove(new Move(Axis.F, layer, true));
			if (safe || CubeCenterUtil.getLayer(piece, Axis.B, size) - 1 < line
					|| (size % 2 == 1 && CubeCenterUtil.getLayer(piece, Axis.F, size) - 1 == size / 2)) {
				while (CubeCenterUtil.getLayer(piece, Axis.F, size) == layer) {
					cube.makeMove(new Move(Axis.R, CubeCenterUtil.getLayer(piece, Axis.R, size), true));
				}
				cube.makeMove(new Move(Axis.F, layer, false));
			}
			cube.makeMove(new Move(Axis.U, 0, false));
			cube.makeMove(new Move(Axis.R, CubeCenterUtil.getLayer(piece, Axis.R, size), true));
		}
	}

	// move a center piece from the F face if the lines are constructed horizontally
	public void moveCenterHorizontal_F(Piece piece) {
		int size = cube.getSize() - 2;
		cube.makeMove(new Move(Axis.F, 0, true));
		int layer = CubeCenterUtil.getLayer(piece, Axis.R, size);
		cube.makeMove(new Move(Axis.R, layer, false));
		while (CubeCenterUtil.getLayer(piece, Axis.R, size) == layer) {
			cube.makeMove(new Move(Axis.D, 0, true));
		}
		cube.makeMove(new Move(Axis.R, layer, true));
		cube.makeMove(new Move(Axis.F, 0, false));
	}

	// move a center piece from the U face if the lines are constructed horizontally
	public void moveCenterHorizontal_U(Piece piece, int index, boolean safe) {
		int size = cube.getSize() - 2;
		int layer = CubeCenterUtil.getLayer(piece, Axis.R, size);
		if (safe) {
			cube.makeMove(new Move(Axis.R, layer, true));
			cube.makeMove(new Move(Axis.R, layer, true));

			if (size - layer - 2 < index % size || safe) {
				while (CubeCenterUtil.getLayer(piece, Axis.R, size) == layer) {
					cube.makeMove(new Move(Axis.D, 0, true));
				}
				cube.makeMove(new Move(Axis.R, layer, false));
				cube.makeMove(new Move(Axis.R, layer, false));
			}
		} else {
			cube.makeMove(new Move(Axis.R, layer, true));

			if (size - layer - 2 < index % size || safe) {
				while (CubeCenterUtil.getLayer(piece, Axis.R, size) == layer) {
					cube.makeMove(new Move(Axis.B, 0, true));
				}
				cube.makeMove(new Move(Axis.R, layer, false));
			}
		}
	}

	// move a center piece from the F face if the lines are constructed vertically
	public void moveCenterVertical_F(Piece piece, int index) {
		int size = cube.getSize() - 2;
		if (CubeCenterUtil.getLayer(piece, Axis.U, size) - 1 < index) {
			cube.makeMove(new Move(Axis.F, 0, true));
			cube.makeMove(new Move(Axis.U, CubeCenterUtil.getLayer(piece, Axis.U, size), true));
			cube.makeMove(new Move(Axis.F, 0, false));
		} else {
			cube.makeMove(new Move(Axis.U, CubeCenterUtil.getLayer(piece, Axis.U, size), true));
		}
	}

	public void solve() {
		if(cube.getSize() > 3) {
			cube.pushRotations();
			step1();
			step2();
			step3();
			cube.popRotations();
		}
		
	}

	// solve two opposite centers
	public void step1() {

		int size = cube.getSize() - 2;
		
		for (int i = 0; i < 2; i++) {
			Color color;
			if(size % 2 == 0) {
				color = (i == 1) ? Color.WHITE : Color.YELLOW;
			} else {
				color = cube.getSolveColor(Axis.U);
			}
			boolean safe = (i == 1);
			for (int line = 0; line < size; line++) {
				progress += 1.0f / (size * 5);
				printProgress();
				for (int index = line; index < size * size; index += size) {
					ArrayList<Piece> pieces = findPieces(index, color);
					Piece piece = getUnsolvedPiece(pieces, line, index, true);
					if (piece != null) {
						Axis face = CubeUtil.getFace(piece.getPosition());
						if (face == Axis.U || face == Axis.D) {
							moveCenter_UD(piece, line, safe);
						} else if (face == Axis.F) {
							moveCenterVertical_F(piece, index);
						}

						while (piece.getIndex() != index) {
							cube.makeMove(new Move(CubeUtil.getFace(piece.getPosition()), 0, true));
						}

						while (CubeUtil.getFace(piece.getPosition()) != Axis.F) {
							cube.makeMove(new Move(Axis.U, CubeCenterUtil.getLayer(piece, Axis.U, size), true));
						}
					}
				}

				insertVerticalLine(line, safe);
			}

			cube.makeRotation(Axis.F, true);
			cube.makeRotation(Axis.F, true);
		}

		cube.makeRotation(Axis.F, true);
	}

	// solve two adjacent centers
	public void step2() {

		int size = cube.getSize() - 2;

		for (int i = 0; i < 2; i++) {
			boolean safe = (i == 1);
			Color color;
			if(size % 2 == 0) {
				color = (i == 0) ? Color.ORANGE : Color.BLUE;
			} else {
				color = cube.getSolveColor(Axis.U);
			}

			for (int line = 0; line < size; line++) {
				progress += 1.0f / (size * 5);
				printProgress();

				for (int index = size * (size - line - 1); index < size * (size - line); index++) {
					ArrayList<Piece> pieces = findPieces(index, color);
					Piece piece = getUnsolvedPiece(pieces, line, index, false);

					if (piece != null) {
						if (CubeUtil.getFace(piece.getPosition()) == Axis.U) {
							moveCenterHorizontal_U(piece, index, safe);
						} else if (CubeUtil.getFace(piece.getPosition()) == Axis.F) {
							moveCenterHorizontal_F(piece);
						}

						// move piece to the F face in the right position
						int fIndex = CubeCenterUtil.mapIndex(Axis.R, CubeUtil.getFace(piece.getPosition()), Axis.F,
								piece.getIndex(), size);
						while (fIndex != index) {
							cube.makeMove(new Move(CubeUtil.getFace(piece.getPosition()), 0, true));
							fIndex = CubeCenterUtil.mapIndex(Axis.R, CubeUtil.getFace(piece.getPosition()), Axis.F,
									piece.getIndex(), size);
						}

						// insert piece into the horizontal line
						int rMoves = 0;
						int fMoves = 0;
						int rLayer = CubeCenterUtil.getLayer(piece, Axis.R, size);
						while (CubeUtil.getFace(piece.getPosition()) != Axis.F) {
							cube.makeMove(new Move(Axis.R, rLayer, true));
							rMoves++;
						}

						if (index % size < line || safe) {
							while (CubeCenterUtil.getLayer(piece, Axis.R, size) == rLayer || fMoves % 2 == 0) {
								cube.makeMove(new Move(Axis.F, 0, true));
								fMoves++;
							}
							for (int j = 0; j < rMoves; j++) {
								cube.makeMove(new Move(Axis.R, rLayer, false));
							}
							for (int j = 0; j < fMoves; j++) {
								cube.makeMove(new Move(Axis.F, 0, false));
							}
						}
					}
				}

				insertHorizontalLine(color, line, safe);
			}
			cube.makeRotation(Axis.R, true);
		}
	}

	// solve last two centers
	public void step3() {

		int size = cube.getSize() - 2;

		Color color;
		if(size % 2 == 0) {
			color = Color.RED;
		} else {
			color = cube.getSolveColor(Axis.U);
		}

		PieceGroup uCenter = cube.getCenter(Axis.U);
		for (int i = 0; i < size * size; i++) {
			if (i % size == 0) {
				progress += 1.0f / (size * 5);
				printProgress();
			}

			Piece toReplace = uCenter.getPiece(i);
			if (toReplace.getColor() != color) {
				ArrayList<Piece> pieces = findPieces(i, color);
				Piece toMove = null;
				// find a pair of pieces to exchange
				for (Piece piece : pieces) {
					if (CubeUtil.getFace(piece.getPosition()) == Axis.F) {
						toMove = piece;
						break;
					}
				}

				// move the F face until the 2 pieces allign
				while (toMove.getIndex() != i) {
					cube.makeMove(new Move(Axis.F, 0, true));
				}

				// use a commutator to exchange 2 unsolved center pieces
				int layer1 = CubeCenterUtil.getLayer(toMove, Axis.R, size);
				cube.makeMove(new Move(Axis.R, layer1, true));
				cube.makeMove(new Move(Axis.U, 0, true));
				boolean cw = true;
				if (CubeCenterUtil.getLayer(toMove, Axis.R, size) == layer1) {
					cube.makeMove(new Move(Axis.U, 0, false));
					cube.makeMove(new Move(Axis.U, 0, false));
					cw = false;
				}
				int layer2 = CubeCenterUtil.getLayer(toMove, Axis.R, size);
				cube.makeMove(new Move(Axis.R, layer2, true));
				cube.makeMove(new Move(Axis.U, 0, !cw));
				cube.makeMove(new Move(Axis.R, layer1, false));
				cube.makeMove(new Move(Axis.U, 0, cw));
				cube.makeMove(new Move(Axis.R, layer2, false));
				cube.makeMove(new Move(Axis.U, 0, !cw));
			}
		}

		cube.makeRotation(Axis.R, true);
	}

	private void printProgress() {
		System.out.println("Solving Centers... " + ((int) (progress * 100)) + "%");
	}

}
