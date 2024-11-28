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

package com.github.sampeterson1.puzzle.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.puzzle.lib.Piece;
import com.github.sampeterson1.puzzle.lib.PieceBehavior;
import com.github.sampeterson1.puzzle.lib.PieceGroup;
import com.github.sampeterson1.puzzle.lib.PieceType;
import com.github.sampeterson1.puzzle.lib.PuzzleType;
import com.github.sampeterson1.puzzle.moves.Move;

//Provides a template and base functionality for a puzzle that can be organized into piece groups (e.g Rubik's Cube, Pyraminx, Megaminx)
public abstract class GroupedPuzzle extends Puzzle {
	
	private List<Piece> allPieces;
	private Map<PieceType, Map<Integer, PieceGroup>> groupsByType;
		
	private int size;
	
	public GroupedPuzzle(PuzzleType type, int size) {
		super(type);
		
		this.size = size;
		this.groupsByType = new HashMap<PieceType,  Map<Integer, PieceGroup>>();
		this.allPieces = new ArrayList<Piece>();
	}

	private void addAllPieces(PieceGroup group) {
		for(Piece piece : group.getPieces()) {
			allPieces.add(piece);
		}
	}
	
	protected void createPieceGroup(PieceBehavior behavior, int position) {
		PieceGroup group = new PieceGroup(behavior, this, position);
		PieceType type = behavior.getType();
		
		if(groupsByType.containsKey(type)) {
			groupsByType.get(type).put(position, group);
		} else {
			Map<Integer, PieceGroup> groups = new HashMap<Integer, PieceGroup>();
			groups.put(position, group);
			groupsByType.put(type, groups);
		}
		
		addAllPieces(group);
	}
	
	protected void createPieces(PieceBehavior behavior, int numGroups) {
		Map<Integer, PieceGroup> groups = new HashMap<Integer, PieceGroup>();
		for(int i = 0; i < numGroups; i ++) {
			groups.put(i, new PieceGroup(behavior, this, i));
		}
		
		groupsByType.put(behavior.getType(), groups);
		
		for(PieceGroup group : groups.values()) {
			addAllPieces(group);
		}
	}

	public int getSize() {
		return this.size;
	}
	
	public PieceGroup getGroup(PieceType type, int position) {
		return this.groupsByType.get(type).get(position);
	}
	
	public Map<Integer, PieceGroup> getGroups(PieceType type) {
		return this.groupsByType.get(type);
	}
	
	public Map<PieceType, Map<Integer, PieceGroup>> getAllGroups() {
		return this.groupsByType;
	}
	
	public List<Piece> getAllPieces() {
		return this.allPieces;
	}
	
	@Override
	public List<Piece> getAffectedPieces(Move move) {
		List<Piece> affectedPieces = new ArrayList<Piece>();
		
		for(Map<Integer, PieceGroup> groups : groupsByType.values()) {
			for(PieceGroup group : groups.values()) {
				List<Piece> groupAffectedPieces = group.getAffectedPieces(move);
				affectedPieces.addAll(groupAffectedPieces);
			}
		}
		
		return affectedPieces;
	}
	
	@Override
	public void movePieces(Move move) {	
		for(Map<Integer, PieceGroup> groups : groupsByType.values()) {
			for(PieceGroup group : groups.values()) {
				group.makeMove(move);
			}
		}
		
		for(Map<Integer, PieceGroup> groups : groupsByType.values()) {
			for(PieceGroup group : groups.values()) {
				group.applyMoves();
			}
		}
	}

}
