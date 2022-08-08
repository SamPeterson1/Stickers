package com.github.yoshiapolis.pyraminx.solvers;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Axis;
import com.github.yoshiapolis.puzzle.lib.Color;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;
import com.github.yoshiapolis.puzzle.lib.PieceType;
import com.github.yoshiapolis.pyraminx.pieces.Pyraminx;
import com.github.yoshiapolis.pyraminx.util.PyraminxCenterUtil;

public class PyraminxCenterSolver {
	
	private Pyraminx pyr;
	private int puzzleSize;
	private int centerSize;
	private int centerIndex;
	private int centerLayer;
	private int aboveCenterIndex;
	
	
	private boolean first = true;
	
	public PyraminxCenterSolver(Pyraminx pyr) {
		this.pyr = pyr;
		this.puzzleSize = pyr.getSize();
		this.centerLayer = puzzleSize / 3;
		this.centerSize = puzzleSize - 3;
		this.centerIndex = PyraminxCenterUtil.getCenterIndex(centerSize);
		this.aboveCenterIndex = PyraminxCenterUtil.rotateIndexCW(centerIndex - 1, centerSize);
	}

	public List<Piece> findPieces(Color color, int index) {
		List<Piece> pieces = new ArrayList<Piece>();
		
		for(int i = 0; i < 4; i ++) {
			PieceGroup center = pyr.getGroup(PieceType.CENTER, i);
			for(Piece piece : center.getPieces()) {
				if(piece.getColor() == color) {
					int pieceIndex = piece.getIndex();
					for(int j = 0; j < 3; j ++) {
						if(pieceIndex == index) {
							pieces.add(piece);
						}

						pieceIndex = PyraminxCenterUtil.rotateIndexCW(pieceIndex, centerSize);
					}
				}
			}
		}
		
		return pieces;
	}
	
	public Piece findUnsolvedPiece(Color color, int index, int layer) {
		List<Piece> pieces = findPieces(color, index);
		for(Piece piece : pieces) {
			if(!isSolved(piece, layer)) {
				return piece;
			}
		}
		
		return null;
	}
	
	public boolean isSolved(Piece piece, int layer) {
		if(piece.getIndex() == centerIndex) return true;
		Axis face = Pyraminx.getFace(piece);
		
		int dLayer = PyraminxCenterUtil.getLayer(piece, Axis.PD) - 1;
		return (face == Axis.PR && dLayer == layer)
				|| (face == Axis.PF && dLayer < layer);
	}
	
	public void movePiece_LF(Piece piece, int pairingLayer) {
		int layer = PyraminxCenterUtil.getLayer(piece, Axis.PD);
		while(layer == pairingLayer + 1) {
			pyr.makeMove(new Move(Axis.PL, true));
			layer = PyraminxCenterUtil.getLayer(piece, Axis.PD);
		}
		pyr.makeMove(new Move(Axis.PD, layer, true));
	}
	
	public void movePiece_FL(Piece piece, int pairingLayer) {
		int layer = PyraminxCenterUtil.getLayer(piece, Axis.PD) - 1;
		boolean restoreNeeded = (layer == pairingLayer);
		
		pyr.makeMove(new Move(Axis.PD, layer + 1, false));
		while(layer == pairingLayer) {	
			pyr.makeMove(new Move(Axis.PL, 0, true));
			layer = PyraminxCenterUtil.getLayer(piece, Axis.PD) - 1;
		}
		
		if(restoreNeeded) {
			pyr.makeMove(new Move(Axis.PD, pairingLayer + 1, true));
		}
		
		if(Pyraminx.getFace(piece) != Axis.PL) {
			System.out.println("Badd");
		}
	}
	
	public void movePiece_RL(Piece piece, boolean keepDFace) {
		pyr.makeMove(new Move(Axis.PR, true));
		int layer = PyraminxCenterUtil.getLayer(piece, Axis.PF);
		pyr.makeMove(new Move(Axis.PF, layer, false));
		
		if(keepDFace) {
			int newLayer = layer;
			while(newLayer == layer) {
				pyr.makeMove(new Move(Axis.PL, true));
				newLayer = PyraminxCenterUtil.getLayer(piece, Axis.PF);
			}
			
			pyr.makeMove(new Move(Axis.PF, layer, true));
		}
		
		pyr.makeMove(new Move(Axis.PR, false));
	}

	public void insertPiece(Piece piece, int tgtIndex, boolean tilted, boolean keepDFace) {
		if(tilted) pyr.makeMove(new Move(Axis.PR, false));
		
		Axis face = Pyraminx.getFace(piece);
		int index = PyraminxCenterUtil.mapIndex(face, Axis.PR, Axis.PF, piece.getIndex(), puzzleSize);
		
		while(index != tgtIndex) {
			pyr.makeMove(new Move(face, true));
			index = PyraminxCenterUtil.mapIndex(face, Axis.PR, Axis.PF, piece.getIndex(), puzzleSize);
		}
		
		int layer = PyraminxCenterUtil.getLayer(piece, Axis.PF);
		int numMoves = 0;
		while(Pyraminx.getFace(piece) != Axis.PR) {
			pyr.makeMove(new Move(Axis.PF, layer, true));
			numMoves ++;
		}
		
		int pairingLayer = 0;
		if(keepDFace) {
			Piece atIndex = new Piece(PieceType.CENTER, Pyraminx.getAxisIndex(Axis.PR), tgtIndex, puzzleSize);
			if(tilted) {
				pairingLayer = PyraminxCenterUtil.getLayer(atIndex, Axis.PL);
			} else {
				pairingLayer = PyraminxCenterUtil.getLayer(atIndex, Axis.PD);
			}
			
			pyr.makeMove(new Move(Axis.PR, !tilted));
			for(int i = 0; i < numMoves; i ++) {
				pyr.makeMove(new Move(Axis.PF, layer, false));
			}
			pyr.makeMove(new Move(Axis.PR, tilted));
			
			if(pairingLayer == layer) {
				pyr.makeMove(new Move(Axis.PL, true));
				pyr.makeMove(new Move(Axis.PD, layer, false));
			}
		}
		
		if(tilted && pairingLayer != layer) {
			pyr.makeMove(new Move(Axis.PR, true));
		}
	}
	
	public void solvePiece(Piece piece, int tgtIndex, int layer, boolean tilted, boolean keepDFace) {
		Axis face = Pyraminx.getFace(piece);
		
		if(face == Axis.PF) {
			movePiece_FL(piece, layer);
		} else if(face == Axis.PR) {
			movePiece_RL(piece, keepDFace);
		}

		insertPiece(piece, tgtIndex, tilted, keepDFace);
	}
	
	public void firstTwoCenters() {
		for(int iters = 0; iters < 2; iters ++) {
			boolean keepDFace = (iters == 1);
			Color color = (iters == 0) ? Color.GREEN : Color.BLUE;
			for(int layer = 0; layer < centerSize; layer ++) {
				System.out.println("Layer " + layer + "/" + centerSize);
				for(int i = 0; i < centerSize - layer; i ++) {
					int layerOff = centerSize * centerSize - (centerSize - layer) * (centerSize - layer);
					int tgtIndex = layerOff + 2*i;
					
					Piece piece = findUnsolvedPiece(color, tgtIndex, layer);
					Piece inSlot = pyr.getGroup(PieceType.CENTER, Axis.PR).getPiece(tgtIndex);

					if(piece != null && inSlot.getColor() != color) {
						solvePiece(piece, tgtIndex, layer, false, keepDFace);
					}
						
					tgtIndex = layerOff + 2*i + 1;
					tgtIndex = PyraminxCenterUtil.rotateIndexCCW(tgtIndex, centerSize);
					piece = findUnsolvedPiece(color, tgtIndex, layer);
					
					if(piece != null && i < centerSize - layer - 1) {
						solvePiece(piece, tgtIndex, layer, true, keepDFace);						
					}
					
				}
				
				pyr.makeMove(new Move(Axis.PD, layer + 1, false));
			}
			
			pyr.makeRotation(Axis.PL, true);
		}
		
		pyr.makeRotation(Axis.PR, false);
	}

	public int getIndexOff(int index, int z) {
		int layerOff = centerSize * centerSize - (centerSize - z) * (centerSize - z);
		int layerSize = (centerSize - z) * 2 - 1;
		int twiceIndexOff = layerSize - (index - layerOff) - 1;
		
		return twiceIndexOff/2;
	}
	
	public void executeAlternateCommutator(int tgtIndex) {
		pyr.makeMove(new Move(Axis.PD, false));
		
		int z = PyraminxCenterUtil.getZPosition(tgtIndex, centerSize);
		int indexOff = getIndexOff(tgtIndex, z);
		int fMoves = 0;
		
		while(indexOff != z) {
			pyr.makeMove(new Move(Axis.PF, true));
			fMoves ++;
			
			tgtIndex = PyraminxCenterUtil.rotateIndexCW(tgtIndex, centerSize);
			z = PyraminxCenterUtil.getZPosition(tgtIndex, centerSize);
			indexOff = getIndexOff(tgtIndex, z);		
		}
		
		int layer = z + 1;
		pyr.makeMove(new Move(Axis.PF, layer, false));
		pyr.makeMove(new Move(Axis.PL, layer, true));
		pyr.makeMove(new Move(Axis.PF, layer, true));
		pyr.makeMove(new Move(Axis.PL, layer, false));
		
		pyr.makeMove(new Move(Axis.PL, 0, false));
		
		pyr.makeMove(new Move(Axis.PR, layer, false));
		pyr.makeMove(new Move(Axis.PL, layer, false));
		pyr.makeMove(new Move(Axis.PR, layer, true));
		pyr.makeMove(new Move(Axis.PL, layer, true));
		
		for(int i = 0; i < fMoves; i ++) {
			pyr.makeMove(new Move(Axis.PF, false));
		}
	}
	
	public boolean executeCommutator(Piece piece, Axis pivot) {

		int layer = PyraminxCenterUtil.getLayer(piece, pivot);
		boolean cw = (pivot == Axis.PR) ? true : false;
		
		pyr.makeMove(new Move(pivot, layer, cw));
		pyr.makeMove(new Move(Axis.PF, !cw));
		
		int nextLayer = PyraminxCenterUtil.getLayer(piece, pivot);
		if(nextLayer == layer) {
			pyr.makeMove(new Move(Axis.PF, cw));
			pyr.makeMove(new Move(pivot, layer, !cw));
			
			return false;
		}
			
		pyr.makeMove(new Move(pivot, nextLayer, cw));		
		pyr.makeMove(new Move(Axis.PF, cw));
		
		pyr.makeMove(new Move(pivot, layer, !cw));
		pyr.makeMove(new Move(Axis.PF, !cw));
		
		
		pyr.makeMove(new Move(pivot, nextLayer, !cw));
		pyr.makeMove(new Move(Axis.PF, cw));
		
		return true;
	}
	
	public Piece prepareCommutator(int tgtIndex, Axis pivot, Color color) {
		if(tgtIndex == centerIndex) return null;
		
		List<Piece> pieces = findPieces(Color.RED, tgtIndex);
		Piece piece = null;
		
		for(Piece candidate : pieces) {
			if(Pyraminx.getFace(candidate) == Axis.PD) {
				piece = candidate;
				break;
			}
		}
		
		if(piece != null) {
			int mappedIndex = PyraminxCenterUtil.mapIndex(Axis.PD, Axis.PF, pivot, tgtIndex, puzzleSize);
			while(mappedIndex != tgtIndex) {
				pyr.makeMove(new Move(Axis.PD, true));
				mappedIndex = PyraminxCenterUtil.mapIndex(Axis.PD, Axis.PF, pivot, piece.getIndex(), puzzleSize);
			}
		}
		
		return piece;
	}
	
	public void threeCenterCycle(boolean cw) {
		if(puzzleSize % 3 == 1) {
			for(int i = 0; i < 2; i ++) {
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Axis.PL, j, !cw));
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Axis.PD, j, !cw));
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Axis.PL, j, cw));
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Axis.PD, j, !cw));
			}	
			
			for(int i = 0; i < 2; i ++) {
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Axis.PL, j, !cw));
				for(int j = centerLayer + 1; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Axis.PD, j, cw));
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Axis.PL, j, cw));
				for(int j = centerLayer + 1; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Axis.PD, j, cw));
			}
		} else {
			for(int i = 0; i < 2; i ++) {
				for(int j = centerLayer + 1; j < puzzleSize; j ++)
					pyr.makeMove(new Move(Axis.PR, j, !cw));
				pyr.makeMove(new Move(Axis.PL, centerLayer, !cw));
				for(int j = centerLayer + 1; j < puzzleSize; j ++)
					pyr.makeMove(new Move(Axis.PR, j, cw));
				pyr.makeMove(new Move(Axis.PL, centerLayer, !cw));
			}
			
			if(cw) {
				pyr.makeMove(new Move(Axis.PR, true));
				pyr.makeRotation(Axis.PF, true);
				executeAlternateCommutator(aboveCenterIndex);
			} else {
				pyr.makeRotation(Axis.PD, false);
				executeAlternateCommutator(centerIndex + 1);
			}
		}
	}
	
	public void fourCenterSwap() {
		for(int i = 0; i < 3; i ++) {
			pyr.makeMove(new Move(Axis.PL, centerLayer, true));
			pyr.makeMove(new Move(Axis.PR, centerLayer, false));
			pyr.makeMove(new Move(Axis.PL, centerLayer, false));
			pyr.makeMove(new Move(Axis.PR, centerLayer, true));
		}
	}
	
	public void solveMiddleCenterPieces() {
		Axis solvedAxis = null;
		boolean solved = true;
		for(int i = 0; i < 4; i ++) {
			PieceGroup face = pyr.getGroup(PieceType.CENTER, i);
			Piece centerPiece = face.getPiece(centerIndex);
			Piece adjacent = face.getPiece(centerIndex + 1);
			if(centerPiece.getColor() == adjacent.getColor()) {
				solvedAxis = Pyraminx.getFace(centerPiece);
			} else {
				solved = false;
			}
		}
		
		PieceGroup fAxis = pyr.getGroup(PieceType.CENTER, Axis.PF);
		PieceGroup dAxis = pyr.getGroup(PieceType.CENTER, Axis.PD);
		
		if(solvedAxis == null) {
			Color fMiddleColor = fAxis.getPiece(centerIndex).getColor();
			Color dFaceColor = dAxis.getPiece(centerIndex + 1).getColor();
			
			while(fMiddleColor != dFaceColor) {
				pyr.makeRotation(Axis.PD, true);
				fMiddleColor = fAxis.getPiece(centerIndex).getColor();
				dFaceColor = dAxis.getPiece(centerIndex + 1).getColor();
			}
			
			fourCenterSwap();
		} else if(!solved) {
			if(solvedAxis == Axis.PD) {
				pyr.makeRotation(Axis.PF, true);
			} else if(solvedAxis == Axis.PR) {
				pyr.makeRotation(Axis.PD, true);
			} else if(solvedAxis == Axis.PF) {
				pyr.makeRotation(Axis.PD, false);
			}
			
			Color dMiddleColor = dAxis.getPiece(centerIndex).getColor();
			Color fFaceColor = fAxis.getPiece(centerIndex + 1).getColor();
			
			boolean cw = (dMiddleColor == fFaceColor);
			threeCenterCycle(cw);
		}
	}
	
	public boolean isSolved(Axis face) {
		PieceGroup faceGroup = pyr.getGroup(PieceType.CENTER, face);
		Color middleColor = faceGroup.getPiece(centerIndex).getColor();
		Color faceColor = faceGroup.getPiece().getColor();
		
		return (middleColor == faceColor);
	}
	
	public void lastTwoCenters() {
		
		//boolean swap = !(isSolved(Axis.PR) && isSolved(Axis.PL));
		//Axis colorAxis = swap ? Axis.PD : Axis.PF;
		
		//Color color = pyr.getGroup(PieceType.CENTER, colorFace).getPiece(centerIndex).getColor();
		Color color = Color.RED;
		for(int layer = 0; layer < centerSize; layer ++) {
			System.out.println("Layer " + layer + "/" + centerSize);
			for(int i = 0; i < centerSize - layer; i ++) {
				int layerOff = centerSize * centerSize - (centerSize - layer) * (centerSize - layer);
				int tgtIndex = layerOff + 2*i;
	
				
				Piece piece = prepareCommutator(tgtIndex, Axis.PL, color);
				if(piece != null) {
					if(!executeCommutator(piece, Axis.PL)) {
						executeAlternateCommutator(tgtIndex);
					}
				}
				
				
				if(i < centerSize - layer - 1) {
					tgtIndex = layerOff + 2*i + 1;
					piece = prepareCommutator(tgtIndex, Axis.PR, color);
					if(piece != null) {
						if(!executeCommutator(piece, Axis.PR)) {
							executeAlternateCommutator(tgtIndex);
						}
					}
				}
				
			}
		}
	}
	
	public Algorithm solve() {
		pyr.setLogMoves(true);
		pyr.clearMoveLog();
		pyr.pushRotations();
		
		firstTwoCenters();
		lastTwoCenters();
		
		if(puzzleSize % 3 != 0)
			solveMiddleCenterPieces();

		pyr.setLogMoves(false);
		pyr.popRotations();
		
		Algorithm alg = pyr.getMoveLog();
		
		return alg;
	}
	
}
