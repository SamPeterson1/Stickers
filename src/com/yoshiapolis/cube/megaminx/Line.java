package com.yoshiapolis.cube.megaminx;

public class Line {
	public float x1, y1;
	public float x2, y2;

	public Line(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public float[] intersect(Line other) {
		float m1 = (y1 - y2) / (x1 - x2);
		float m2 = (other.y1 - other.y2) / (other.x1 - other.x2);
		float xSolution = (m1 * x1 - m2 * other.x1 + other.y1 - y1) / (m1 - m2);
		float ySolution = m1 * (xSolution - x1) + y1;

		return new float[] { xSolution, ySolution };
	}
}
