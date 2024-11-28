package com.github.sampeterson1.puzzle.lib;

import com.github.sampeterson1.puzzle.moves.Axis;

public interface Rotateable {
	
	//maps a move axis to a new axis depending on the current puzzle rotations
	public abstract Axis transposeAxis(Axis face);
	
}
