package com.github.yoshiapolis.puzzle.lib;

import java.util.List;

public interface MoveSimplificationRule {

	List<Move> simplify(List<Move> moves);
	
}
