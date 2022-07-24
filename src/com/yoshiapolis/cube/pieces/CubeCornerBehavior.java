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

public class CubeCornerBehavior implements PieceBehavior {
	
	@Override
	public PuzzlePiece createPiece(int position, int index) {
		PuzzlePiece piece = new PuzzlePiece(PieceType.CORNER, position, index, 3);
		Color[] colors = CubeCornerUtil.getColors(position);
		for(int i = 0; i < 3; i ++) piece.setColor(i, colors[i]);
		return piece;
	}

	@Override
	public List<PuzzlePiece> getAffectedPieces(Move move, PuzzlePieceGroup group) {
		if(move.isCubeRotation()) return group.getPieces();
		
		Face face = move.getFace();
		int layer = move.getLayer();
		if(layer == group.getPuzzleSize() - 1) {
			layer = 0;
			face = Cube.getOpposingFace(face);
		}
		
		List<PuzzlePiece> retVal = new ArrayList<PuzzlePiece>();
		
		if(layer == 0) {
			Integer[] facePositions = CubeCornerUtil.getPositions(face);
			for(int i = 0; i < 4; i ++) {
				if(facePositions[i] == group.getPosition()) {
					retVal.add(group.getPiece());
				}
			}
		}
		
		return retVal;
	}

	@Override
	public void movePiece(Move move, PuzzlePiece piece, int puzzleSize) {
		move = CubeMoveUtil.normalize(move, puzzleSize);
		int iters = (move.isCW() ? 1 : 3);
				
		for(int i = 0; i < iters; i ++) {
			int[] mapping = CubeCornerUtil.mapCorner(move, piece.getPosition(), puzzleSize);
			piece.setPosition(mapping[0]);
			for(int j = 0; j < mapping[1]; j ++) {
				CubeCornerUtil.rotateCW(piece);
			}
		}
	}

	@Override
	public int getNumPieces(int puzzleSize) {
		return 1;
	}
}
