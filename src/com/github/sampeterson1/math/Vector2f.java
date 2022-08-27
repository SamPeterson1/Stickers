package com.github.sampeterson1.math;

//A 2D float vector
public class Vector2f {
	
	public float x;
	public float y;
	
	public Vector2f(float a) {
		this(a, a);
	}
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f mult(Vector2f other) {
		return new Vector2f(x * other.x, y * other.y);
	}
	
}
