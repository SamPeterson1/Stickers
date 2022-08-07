package com.github.yoshiapolis.math;

public class Vector3f {
	
	public static final Vector3f ZERO = new Vector3f(0, 0, 0);
	public static final Vector3f ONE = new Vector3f(1, 1, 1);
	
	public float x, y, z;
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void normalize() {
		float mag = Mathf.sqrt(x*x + y*y + z*z);
		x /= mag;
		y /= mag;
		z /= mag;
	}
}
