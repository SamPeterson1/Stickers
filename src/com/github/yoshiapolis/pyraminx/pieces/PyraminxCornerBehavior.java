package com.github.yoshiapolis.pyraminx.pieces;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.puzzle.lib.Color;
import com.github.yoshiapolis.puzzle.lib.Face;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceBehavior;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;
import com.github.yoshiapolis.puzzle.lib.PieceType;
import com.github.yoshiapolis.pyraminx.util.PyraminxCornerUtil;

public class PyraminxCornerBehavior implements PieceBehavior {

	private static int[] oppositeCorners = {2, 3, 1, 0};
	
	@Override
	public Piece createPiece(int position, int index, int puzzleSize) {
		Color[] colors = PyraminxCornerUtil.getColors(position);
		Piece corner = new Piece(PieceType.CORNER, position, index, puzzleSize);
		for(int i = 0; i < 3; i ++)
			corner.setColor(i, colors[i]);
		
		return corner;
	}

	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {
		int position = group.getPosition();
		int puzzleSize = group.getPuzzleSize();
		int layer = move.getLayer();
		Face moveFace = move.getFace();
		
		List<Piece> pieces = new ArrayList<Piece>();
		if(position == oppositeCorners[moveFace.getIndex()]) {
			if(layer == puzzleSize - 2) {
				pieces.add(group.getPiece(0)); 
			} else if(layer == puzzleSize - 1) {
				pieces.add(group.getPiece(1));
			}
		} else if(layer == 0) {
			return group.getPieces();
		}
		
		return pieces;
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		PyraminxCornerUtil.mapCorner(move, piece);
	}

	@Override
	public int getNumPieces(int puzzleSize) {
		return 2;
	}

}
