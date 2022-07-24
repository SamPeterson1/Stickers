package com.yoshiapolis.cube.pieces;

import java.util.Objects;

import com.yoshiapolis.puzzle.Face;

public class OrderedFacePair {
	
	Face a;
	Face b;
	
	public OrderedFacePair(Face a, Face b) {
		this.a = a;
		this.b = b;
	}
	
	public OrderedFacePair getInverse() {
		return new OrderedFacePair(b, a);
	}
	
	public Face getFaceA() {
		return this.a;
	}
	
	public Face getFaceB() {
		return this.b;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderedFacePair other = (OrderedFacePair) obj;
		return a == other.a && b == other.b;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}
	
}
