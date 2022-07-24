package com.yoshiapolis.cube.pieces;

import java.util.ArrayList;
import java.util.List;

import com.yoshiapolis.puzzle.Color;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;
import com.yoshiapolis.puzzle.PieceBehavior;
import com.yoshiapolis.puzzle.PieceType;
import com.yoshiapolis.puzzle.PuzzlePiece;
import com.yoshiapolis.puzzle.PuzzlePieceGroup;

public class CubeEdgeBehavior implements PieceBehavior {

	@Override
	public PuzzlePiece createPiece(int position, int index) {
		PuzzlePiece edge = new PuzzlePiece(PieceType.EDGE, position, index, 2);
		Color[] colors = CubeEdgeUtil.getColors(position);
		edge.setColor(0, colors[0]);
		edge.setColor(1, colors[1]);
		
		return edge;
	}
	
	@Override
	public void movePiece(Move move, PuzzlePiece piece, int puzzleSize) {
		PuzzlePiece mapped = CubeEdgeUtil.mapEdge(move, piece, puzzleSize);
		
		piece.setColor(0, mapped.getColor(0));
		piece.setColor(1, mapped.getColor(1));
		piece.setPosition(mapped.getPosition());
		piece.setIndex(mapped.getIndex());
	}

	@Override
	public List<PuzzlePiece> getAffectedPieces(Move move, PuzzlePieceGroup group) {
		if(move.isCubeRotation()) return group.getPieces();
		
		int size = group.getPuzzleSize() - 2;
		int position = group.getPosition();
		
		move = CubeMoveUtil.normalize(move, size+2);
		Face moveFace = move.getFace();
		Face oppMoveFace = Cube.getOpposingFace(moveFace);
		Face edgeFace1 = CubeEdgeUtil.getFace(position, 0);
		Face edgeFace2 = CubeEdgeUtil.getFace(position, 1);
		
		List<PuzzlePiece> retVal = new ArrayList<PuzzlePiece>();
		
		if((move.getLayer() == 0 && (moveFace == edgeFace1 || moveFace == edgeFace2)) ||
				(move.getLayer() == size+1 && (oppMoveFace == edgeFace1 || oppMoveFace == edgeFace2))) {
			return group.getPieces();
		} else if(move.getLayer() != 0 && move.getLayer() != size+1 
				&& moveFace != edgeFace1 && moveFace != edgeFace2 && 
				oppMoveFace != edgeFace1 && oppMoveFace != edgeFace2) {
			PuzzlePiece calc = null;
			if(moveFace == Face.R) {
				calc = new PuzzlePiece(PieceType.EDGE, 0, size-move.getLayer(), 2);	
			} else if(moveFace == Face.U) {
				calc = new PuzzlePiece(PieceType.EDGE, 4, size-move.getLayer(), 2);	
			} else if(moveFace == Face.F) {
				calc = new PuzzlePiece(PieceType.EDGE, 1, move.getLayer()-1, 2);	
			}
			
			while(calc.getPosition() != position) {
				calc = CubeEdgeUtil.mapEdge(move, calc, size+2);
			}
			
			int index = calc.getIndex();
			retVal.add(group.getPiece(index));
		}
		
		return retVal;
	}

	@Override
	public int getNumPieces(int puzzleSize) {
		return puzzleSize - 2;
	}

}
