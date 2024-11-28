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

package com.github.sampeterson1.puzzles.ivyCube.pieces;

import java.util.HashMap;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.SimplePieceBehavior;
import com.github.sampeterson1.puzzle.moves.Axis;
import com.github.sampeterson1.puzzle.moves.Move;
import com.github.sampeterson1.puzzle.templates.Puzzle;
import com.github.sampeterson1.puzzles.ivyCube.util.IvyCubeUtil;

//Defines how the center pieces on an ivy cube move
public class IvyCubeCenterBehavior extends SimplePieceBehavior {
	
	//key-value pairs to be put into the position maps below
	private static final int[][] rPositionArr = { {IvyCubeUtil.GREEN_CENTER, IvyCubeUtil.RED_CENTER}, {IvyCubeUtil.RED_CENTER, IvyCubeUtil.YELLOW_CENTER}, {IvyCubeUtil.YELLOW_CENTER, IvyCubeUtil.GREEN_CENTER} };
	private static final int[][] lPositionArr = { {IvyCubeUtil.WHITE_CENTER, IvyCubeUtil.GREEN_CENTER}, {IvyCubeUtil.GREEN_CENTER, IvyCubeUtil.ORANGE_CENTER}, {IvyCubeUtil.ORANGE_CENTER, IvyCubeUtil.WHITE_CENTER} };
	private static final int[][] dPositionArr = { {IvyCubeUtil.BLUE_CENTER, IvyCubeUtil.ORANGE_CENTER}, {IvyCubeUtil.ORANGE_CENTER, IvyCubeUtil.YELLOW_CENTER}, {IvyCubeUtil.YELLOW_CENTER, IvyCubeUtil.BLUE_CENTER} };
	private static final int[][] bPositionArr = { {IvyCubeUtil.WHITE_CENTER, IvyCubeUtil.BLUE_CENTER}, {IvyCubeUtil.BLUE_CENTER, IvyCubeUtil.RED_CENTER}, {IvyCubeUtil.RED_CENTER, IvyCubeUtil.WHITE_CENTER} };
	
	//Takes a face position and maps it to a new face given a move
	private static final Map<Integer, Integer> rPositionMap = initPositionMap(rPositionArr);
	private static final Map<Integer, Integer> lPositionMap = initPositionMap(lPositionArr);
	private static final Map<Integer, Integer> dPositionMap = initPositionMap(dPositionArr);
	private static final Map<Integer, Integer> bPositionMap = initPositionMap(bPositionArr);
	
	private static Map<Integer, Integer> initPositionMap(int[][] positionArr) {
		Map<Integer, Integer> positionMap = new HashMap<Integer, Integer>();
		for(int i = 0; i < 3; i ++) {
			positionMap.put(positionArr[i][0], positionArr[i][1]);
		}
		
		return positionMap;
	}
	
	private static Map<Integer, Integer> getPositionMap(Axis axis) {
		if(axis == Axis.IR) {
			return rPositionMap;
		} else if(axis == Axis.IL) {
			return lPositionMap;
		} else if(axis == Axis.ID) {
			return dPositionMap;
		} else if(axis == Axis.IB) {
			return bPositionMap;
		}
		
		return null;
	}
	
	public IvyCubeCenterBehavior(Puzzle puzzle) {
		super(PieceType.CENTER, puzzle);
	}
	
	@Override
	public Piece createPiece(int position) {
		Piece center = new Piece(super.getPuzzle(), PieceType.CENTER, position);
		center.setColor(IvyCubeUtil.centerColors[position]);
		
		return center;
	}
	
	@Override
	public boolean affectedByMove(Move move, Piece piece) {
		Axis axis = move.getAxis();
		int position = piece.getPosition();
		Map<Integer, Integer> positionMap = getPositionMap(axis);
		
		return positionMap.containsKey(position);
	}
	
	@Override
	public void movePiece(Move move, Piece piece) {
		Axis axis = move.getAxis();
		int position = piece.getPosition();
		Map<Integer, Integer> positionMap = getPositionMap(axis);
		
		int newPosition = positionMap.get(position);
		if(move.isCCW()) {
			newPosition = positionMap.get(newPosition);
		}
		
		piece.setPosition(newPosition);
	}
}
