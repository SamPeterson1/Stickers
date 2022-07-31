package com.github.yoshiapolis.pyraminx.solvers;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.puzzle.lib.Algorithm;
import com.github.yoshiapolis.puzzle.lib.Color;
import com.github.yoshiapolis.puzzle.lib.Face;
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
		Face face = Pyraminx.getFace(piece);
		
		int dLayer = PyraminxCenterUtil.getLayer(piece, Face.PD, puzzleSize) - 1;
		return (face == Face.PR && dLayer == layer)
				|| (face == Face.PF && dLayer < layer);
	}
	
	public void movePiece_LF(Piece piece, int pairingLayer) {
		int layer = PyraminxCenterUtil.getLayer(piece, Face.PD, puzzleSize);
		while(layer == pairingLayer + 1) {
			pyr.makeMove(new Move(Face.PL, true));
			layer = PyraminxCenterUtil.getLayer(piece, Face.PD, puzzleSize);
		}
		pyr.makeMove(new Move(Face.PD, layer, true));
	}
	
	public void movePiece_FL(Piece piece, int pairingLayer) {
		int layer = PyraminxCenterUtil.getLayer(piece, Face.PD, puzzleSize) - 1;
		boolean restoreNeeded = (layer == pairingLayer);
		
		pyr.makeMove(new Move(Face.PD, layer + 1, false));
		while(layer == pairingLayer) {	
			pyr.makeMove(new Move(Face.PL, 0, true));
			layer = PyraminxCenterUtil.getLayer(piece, Face.PD, puzzleSize) - 1;
		}
		
		if(restoreNeeded) {
			pyr.makeMove(new Move(Face.PD, pairingLayer + 1, true));
		}
		
		if(Pyraminx.getFace(piece) != Face.PL) {
			System.out.println("Badd");
		}
	}
	
	public void movePiece_RL(Piece piece, boolean keepDFace) {
		pyr.makeMove(new Move(Face.PR, true));
		int layer = PyraminxCenterUtil.getLayer(piece, Face.PF, puzzleSize);
		pyr.makeMove(new Move(Face.PF, layer, false));
		
		if(keepDFace) {
			int newLayer = layer;
			while(newLayer == layer) {
				pyr.makeMove(new Move(Face.PL, true));
				newLayer = PyraminxCenterUtil.getLayer(piece, Face.PF, puzzleSize);
			}
			
			pyr.makeMove(new Move(Face.PF, layer, true));
		}
		
		pyr.makeMove(new Move(Face.PR, false));
	}

	public void insertPiece(Piece piece, int tgtIndex, boolean tilted, boolean keepDFace) {
		if(tilted) pyr.makeMove(new Move(Face.PR, false));
		
		Face face = Pyraminx.getFace(piece);
		int index = PyraminxCenterUtil.mapIndex(face, Face.PR, Face.PF, piece.getIndex(), puzzleSize);
		
		while(index != tgtIndex) {
			pyr.makeMove(new Move(face, true));
			index = PyraminxCenterUtil.mapIndex(face, Face.PR, Face.PF, piece.getIndex(), puzzleSize);
		}
		
		int layer = PyraminxCenterUtil.getLayer(piece, Face.PF, puzzleSize);
		int numMoves = 0;
		while(Pyraminx.getFace(piece) != Face.PR) {
			pyr.makeMove(new Move(Face.PF, layer, true));
			numMoves ++;
		}
		
		int pairingLayer = 0;
		if(keepDFace) {
			Piece atIndex = new Piece(PieceType.CENTER, Face.PR.getIndex(), tgtIndex);
			if(tilted) {
				pairingLayer = PyraminxCenterUtil.getLayer(atIndex, Face.PL, puzzleSize);
			} else {
				pairingLayer = PyraminxCenterUtil.getLayer(atIndex, Face.PD, puzzleSize);
			}
			
			pyr.makeMove(new Move(Face.PR, !tilted));
			for(int i = 0; i < numMoves; i ++) {
				pyr.makeMove(new Move(Face.PF, layer, false));
			}
			pyr.makeMove(new Move(Face.PR, tilted));
			
			if(pairingLayer == layer) {
				pyr.makeMove(new Move(Face.PL, true));
				pyr.makeMove(new Move(Face.PD, layer, false));
			}
		}
		
		if(tilted && pairingLayer != layer) {
			pyr.makeMove(new Move(Face.PR, true));
		}
	}
	
	public void solvePiece(Piece piece, int tgtIndex, int layer, boolean tilted, boolean keepDFace) {
		Face face = Pyraminx.getFace(piece);
		
		if(face == Face.PF) {
			movePiece_FL(piece, layer);
		} else if(face == Face.PR) {
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
					Piece inSlot = pyr.getGroup(PieceType.CENTER, Face.PR.getIndex()).getPiece(tgtIndex);

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
				
				pyr.makeMove(new Move(Face.PD, layer + 1, false));
			}
			
			pyr.makeRotation(Face.PL, true);
		}
		
		pyr.makeRotation(Face.PR, false);
	}

	public int getIndexOff(int index, int z) {
		int layerOff = centerSize * centerSize - (centerSize - z) * (centerSize - z);
		int layerSize = (centerSize - z) * 2 - 1;
		int twiceIndexOff = layerSize - (index - layerOff) - 1;
		
		return twiceIndexOff/2;
	}
	
	public void executeAlternateCommutator(int tgtIndex) {
		pyr.makeMove(new Move(Face.PD, false));
		
		int z = PyraminxCenterUtil.getZPosition(tgtIndex, centerSize);
		int indexOff = getIndexOff(tgtIndex, z);
		int fMoves = 0;
		
		while(indexOff != z) {
			pyr.makeMove(new Move(Face.PF, true));
			fMoves ++;
			
			tgtIndex = PyraminxCenterUtil.rotateIndexCW(tgtIndex, centerSize);
			z = PyraminxCenterUtil.getZPosition(tgtIndex, centerSize);
			indexOff = getIndexOff(tgtIndex, z);		
		}
		
		int layer = z + 1;
		pyr.makeMove(new Move(Face.PF, layer, false));
		pyr.makeMove(new Move(Face.PL, layer, true));
		pyr.makeMove(new Move(Face.PF, layer, true));
		pyr.makeMove(new Move(Face.PL, layer, false));
		
		pyr.makeMove(new Move(Face.PL, 0, false));
		
		pyr.makeMove(new Move(Face.PR, layer, false));
		pyr.makeMove(new Move(Face.PL, layer, false));
		pyr.makeMove(new Move(Face.PR, layer, true));
		pyr.makeMove(new Move(Face.PL, layer, true));
		
		for(int i = 0; i < fMoves; i ++) {
			pyr.makeMove(new Move(Face.PF, false));
		}
	}
	
	public boolean executeCommutator(Piece piece, Face pivot) {

		int layer = PyraminxCenterUtil.getLayer(piece, pivot, puzzleSize);
		boolean cw = (pivot == Face.PR) ? true : false;
		
		pyr.makeMove(new Move(pivot, layer, cw));
		pyr.makeMove(new Move(Face.PF, !cw));
		
		int nextLayer = PyraminxCenterUtil.getLayer(piece, pivot, puzzleSize);
		if(nextLayer == layer) {
			pyr.makeMove(new Move(Face.PF, cw));
			pyr.makeMove(new Move(pivot, layer, !cw));
			
			return false;
		}
			
		pyr.makeMove(new Move(pivot, nextLayer, cw));		
		pyr.makeMove(new Move(Face.PF, cw));
		
		pyr.makeMove(new Move(pivot, layer, !cw));
		pyr.makeMove(new Move(Face.PF, !cw));
		
		
		pyr.makeMove(new Move(pivot, nextLayer, !cw));
		pyr.makeMove(new Move(Face.PF, cw));
		
		return true;
	}
	
	public Piece prepareCommutator(int tgtIndex, Face pivot, Color color) {
		if(tgtIndex == centerIndex) return null;
		
		List<Piece> pieces = findPieces(Color.RED, tgtIndex);
		Piece piece = null;
		
		for(Piece candidate : pieces) {
			if(Pyraminx.getFace(candidate) == Face.PD) {
				piece = candidate;
				break;
			}
		}
		
		if(piece != null) {
			int mappedIndex = PyraminxCenterUtil.mapIndex(Face.PD, Face.PF, pivot, tgtIndex, puzzleSize);
			while(mappedIndex != tgtIndex) {
				pyr.makeMove(new Move(Face.PD, true));
				mappedIndex = PyraminxCenterUtil.mapIndex(Face.PD, Face.PF, pivot, piece.getIndex(), puzzleSize);
			}
		}
		
		return piece;
	}
	
	public void threeCenterCycle(boolean cw) {
		if(puzzleSize % 3 == 1) {
			for(int i = 0; i < 2; i ++) {
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Face.PL, j, !cw));
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Face.PD, j, !cw));
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Face.PL, j, cw));
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Face.PD, j, !cw));
			}	
			
			for(int i = 0; i < 2; i ++) {
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Face.PL, j, !cw));
				for(int j = centerLayer + 1; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Face.PD, j, cw));
				for(int j = centerLayer; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Face.PL, j, cw));
				for(int j = centerLayer + 1; j <= 2*centerLayer; j ++)
					pyr.makeMove(new Move(Face.PD, j, cw));
			}
		} else {
			for(int i = 0; i < 2; i ++) {
				for(int j = centerLayer + 1; j < puzzleSize; j ++)
					pyr.makeMove(new Move(Face.PR, j, !cw));
				pyr.makeMove(new Move(Face.PL, centerLayer, !cw));
				for(int j = centerLayer + 1; j < puzzleSize; j ++)
					pyr.makeMove(new Move(Face.PR, j, cw));
				pyr.makeMove(new Move(Face.PL, centerLayer, !cw));
			}
			
			if(cw) {
				pyr.makeMove(new Move(Face.PR, true));
				pyr.makeRotation(Face.PF, true);
				executeAlternateCommutator(aboveCenterIndex);
			} else {
				pyr.makeRotation(Face.PD, false);
				executeAlternateCommutator(centerIndex + 1);
			}
		}
	}
	
	public void fourCenterSwap() {
		for(int i = 0; i < 3; i ++) {
			pyr.makeMove(new Move(Face.PL, centerLayer, true));
			pyr.makeMove(new Move(Face.PR, centerLayer, false));
			pyr.makeMove(new Move(Face.PL, centerLayer, false));
			pyr.makeMove(new Move(Face.PR, centerLayer, true));
		}
	}
	
	public void solveMiddleCenterPieces() {
		Face solvedFace = null;
		boolean solved = true;
		for(int i = 0; i < 4; i ++) {
			PieceGroup face = pyr.getGroup(PieceType.CENTER, i);
			Piece centerPiece = face.getPiece(centerIndex);
			Piece adjacent = face.getPiece(centerIndex + 1);
			if(centerPiece.getColor() == adjacent.getColor()) {
				solvedFace = Pyraminx.getFace(centerPiece);
			} else {
				solved = false;
			}
		}
		
		PieceGroup fFace = pyr.getGroup(PieceType.CENTER, Face.PF.getIndex());
		PieceGroup dFace = pyr.getGroup(PieceType.CENTER, Face.PD.getIndex());
		
		if(solvedFace == null) {
			Color fMiddleColor = fFace.getPiece(centerIndex).getColor();
			Color dFaceColor = dFace.getPiece(centerIndex + 1).getColor();
			
			while(fMiddleColor != dFaceColor) {
				pyr.makeRotation(Face.PD, true);
				fMiddleColor = fFace.getPiece(centerIndex).getColor();
				dFaceColor = dFace.getPiece(centerIndex + 1).getColor();
			}
			
			fourCenterSwap();
		} else if(!solved) {
			if(solvedFace == Face.PD) {
				pyr.makeRotation(Face.PF, true);
			} else if(solvedFace == Face.PR) {
				pyr.makeRotation(Face.PD, true);
			} else if(solvedFace == Face.PF) {
				pyr.makeRotation(Face.PD, false);
			}
			
			Color dMiddleColor = dFace.getPiece(centerIndex).getColor();
			Color fFaceColor = fFace.getPiece(centerIndex + 1).getColor();
			
			boolean cw = (dMiddleColor == fFaceColor);
			threeCenterCycle(cw);
		}
	}
	
	public boolean isSolved(Face face) {
		PieceGroup faceGroup = pyr.getGroup(PieceType.CENTER, face);
		Color middleColor = faceGroup.getPiece(centerIndex).getColor();
		Color faceColor = faceGroup.getPiece().getColor();
		
		return (middleColor == faceColor);
	}
	
	public void lastTwoCenters() {
		
		//boolean swap = !(isSolved(Face.PR) && isSolved(Face.PL));
		//Face colorFace = swap ? Face.PD : Face.PF;
		
		//Color color = pyr.getGroup(PieceType.CENTER, colorFace).getPiece(centerIndex).getColor();
		Color color = Color.RED;
		for(int layer = 0; layer < centerSize; layer ++) {
			System.out.println("Layer " + layer + "/" + centerSize);
			for(int i = 0; i < centerSize - layer; i ++) {
				int layerOff = centerSize * centerSize - (centerSize - layer) * (centerSize - layer);
				int tgtIndex = layerOff + 2*i;
	
				
				Piece piece = prepareCommutator(tgtIndex, Face.PL, color);
				if(piece != null) {
					if(!executeCommutator(piece, Face.PL)) {
						executeAlternateCommutator(tgtIndex);
					}
				}
				
				
				if(i < centerSize - layer - 1) {
					tgtIndex = layerOff + 2*i + 1;
					piece = prepareCommutator(tgtIndex, Face.PR, color);
					if(piece != null) {
						if(!executeCommutator(piece, Face.PR)) {
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
		
		//executeAlternateCommutator(Face.PL, centerIndex - 1);
	
		
		firstTwoCenters();
		lastTwoCenters();
		
		if(puzzleSize % 3 != 0)
			solveMiddleCenterPieces();
		
		
		//threeCenterCycle(true);
		
		
		

		pyr.setLogMoves(false);
		pyr.popRotations();
		
		Algorithm alg = pyr.getMoveLog();
		
		return alg;
	}
	
}
