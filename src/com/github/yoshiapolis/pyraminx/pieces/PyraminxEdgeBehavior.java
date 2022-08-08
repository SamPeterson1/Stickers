package com.github.yoshiapolis.pyraminx.pieces;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.puzzle.lib.Axis;
import com.github.yoshiapolis.puzzle.lib.Color;
import com.github.yoshiapolis.puzzle.lib.Move;
import com.github.yoshiapolis.puzzle.lib.Piece;
import com.github.yoshiapolis.puzzle.lib.PieceBehavior;
import com.github.yoshiapolis.puzzle.lib.PieceGroup;
import com.github.yoshiapolis.puzzle.lib.PieceType;
import com.github.yoshiapolis.pyraminx.util.PyraminxEdgeUtil;

public class PyraminxEdgeBehavior implements PieceBehavior {
	
	private static final PieceType type = PieceType.EDGE;
	
	@Override
	public Piece createPiece(int position, int index, int puzzleSize) {
		Color[] colors = PyraminxEdgeUtil.getColors(position);
		Piece edge = new Piece(PieceType.EDGE, position, index, puzzleSize);
		edge.setColor(0, colors[0]);
		edge.setColor(1, colors[1]);
		
		return edge;
	}

	@Override
	public List<Piece> getAffectedPieces(Move move, PieceGroup group) {
		Axis face = move.getFace();
		int layer = move.getLayer();
		int position = group.getPosition();
		int edgeSize = group.getNumPieces();
		int puzzleSize = group.getPuzzleSize();

		Axis face1 = PyraminxEdgeUtil.getFace(position, 0);
		Axis face2 = PyraminxEdgeUtil.getFace(position, 1);
		List<Piece> retVal = new ArrayList<Piece>();

		//System.out.println(position + " " + face1 + " " + face2 + " " + face);
		
		if (face1 == face || face2 == face) {
			if (layer == 0) {
				//System.out.println("returned all at position " + position);
				return group.getPieces();
			}
		} else if(layer > 0 && layer < puzzleSize - 1){
			Piece simulateFlips = new Piece(PieceType.EDGE, position, 0, puzzleSize);
			int targetPosition = 0;
			Move simulateMove = new Move(face, true);
			
			if (face == Axis.PR) {
				targetPosition = 5;
			} else if(face == Axis.PL) {
				targetPosition = 3;
			} else if(face == Axis.PF) {
				targetPosition = 4;
			}
			
			while(simulateFlips.getPosition() != targetPosition) {
				movePiece(simulateMove, simulateFlips);
			}
			
			int direction = 1;
			if(simulateFlips.getIndex() != 0) {
				layer = puzzleSize - layer - 1;
				direction = -1;
			}
			
			int pieceIndex = 2*(layer - 1);
			int nextPieceIndex = pieceIndex + direction;
			retVal.add(group.getPiece(pieceIndex));
			
			if (nextPieceIndex < edgeSize && nextPieceIndex >= 0) {
				retVal.add(group.getPiece(nextPieceIndex));
			}
		}
		
		return retVal;
	}

	@Override
	public void movePiece(Move move, Piece piece) {
		int iters = move.isCW() ? 1 : 2;
		int edgeSize = 2 * (piece.getPuzzleSize() - 3) + 1;
		Axis face = move.getFace();

		for (int i = 0; i < iters; i++) {
			int edgeMapVal = PyraminxEdgeUtil.getEdgeMapVal(face, piece.getPosition());
			boolean flip = (edgeMapVal < 0);
			int newPosition = Math.abs(edgeMapVal) - 1;

			if (flip) {
				Color[] colorsCpy = new Color[] {piece.getColor(0), piece.getColor(1)};
				piece.setColor(0, colorsCpy[1]);
				piece.setColor(1, colorsCpy[0]);
				piece.setIndex(edgeSize - piece.getIndex() - 1);
			}
			
			piece.setPosition(newPosition);		
		}
	}

	@Override
	public int getNumPieces(int puzzleSize, int position) {
		return 2 * (puzzleSize - 3) + 1;
	}

	@Override
	public PieceType getType() {
		return type;
	}

}
