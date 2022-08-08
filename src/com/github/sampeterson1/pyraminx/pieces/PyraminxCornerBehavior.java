package com.github.sampeterson1.pyraminx.pieces;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Axis;
import com.github.sampeterson1.puzzle.lib.Color;
import com.github.sampeterson1.puzzle.lib.Move;
import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceBehavior;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.pyraminx.util.PyraminxCornerUtil;

public class PyraminxCornerBehavior implements PieceBehavior {

	private static final PieceType type = PieceType.CORNER;
	private static final Map<Axis, Integer> oppositeCorners = initOppositeCorners();
	
	private static Map<Axis, Integer> initOppositeCorners() {
		Map<Axis, Integer> oppositeCorners = new EnumMap<Axis, Integer>(Axis.class);
		oppositeCorners.put(Axis.PF, 2);
		oppositeCorners.put(Axis.PR, 3);
		oppositeCorners.put(Axis.PL, 1);
		oppositeCorners.put(Axis.PD, 0);
		
		return oppositeCorners;
	}
	
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
		Axis moveFace = move.getFace();
		
		List<Piece> pieces = new ArrayList<Piece>();
		if(position == oppositeCorners.get(moveFace)) {
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
	public int getNumPieces(int puzzleSize, int position) {
		return 2;
	}

	@Override
	public PieceType getType() {
		return type;
	}

}
