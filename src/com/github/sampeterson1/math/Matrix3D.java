package com.github.sampeterson1.math;

import java.nio.FloatBuffer;

import com.github.sampeterson1.renderEngine.rendering.CameraSettings;
import com.github.sampeterson1.renderEngine.window.Window;

public class Matrix3D {

	public float m00, m01, m02, m03,
				 m10, m11, m12, m13,
				 m20, m21, m22, m23,
				 m30, m31, m32, m33;
	
	public Matrix3D() {
		setIdentity();
	}
	
	public static Matrix3D createProjectionMatrix(CameraSettings settings) {
		Matrix3D mat = new Matrix3D();
		
		float zFar = settings.getZFar();
		float zNear = settings.getZNear();
		float zm = zFar - zNear;
		float zp = zFar + zNear;
		float a = 1;
		float fov = Mathf.DEG_TO_RAD * settings.getFov();
		
		mat.m00 = 1 / Mathf.tan(fov / 2) / a;
		mat.m11 = 1 / Mathf.tan(fov / 2);
		mat.m22 = -zp / zm;
		mat.m23 = -1;
		mat.m32 = -2 * zFar * zNear / zm;
		mat.m33 = 0;
		
		return mat;
	}
	
	public void store(FloatBuffer buffer) {
		buffer.put(new float[] { 
			m00, m01, m02, m03, 
			m10, m11, m12, m13,
			m20, m21, m22, m23, 
			m30, m31, m32, m33
		});
	}
	
	public void setIdentity() {
		m00 = 1;
		m11 = 1;
		m22 = 1;
		m33 = 1;
	}
	
	public void multiply(Matrix3D other) {
		float _m00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20 + m03 * other.m30;
		float _m01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21 + m03 * other.m31;
		float _m02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22 + m03 * other.m32;
		float _m03 = m00 * other.m03 + m01 * other.m13 + m02 * other.m23 + m03 * other.m33;
		
		float _m10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20 + m13 * other.m30;
		float _m11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21 + m13 * other.m31;
		float _m12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22 + m13 * other.m32;
		float _m13 = m10 * other.m03 + m11 * other.m13 + m12 * other.m23 + m13 * other.m33;
		
		float _m20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20 + m23 * other.m30;
		float _m21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21 + m23 * other.m31;
		float _m22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22 + m23 * other.m32;
		float _m23 = m20 * other.m03 + m21 * other.m13 + m22 * other.m23 + m23 * other.m33;
		
		float _m30 = m30 * other.m00 + m31 * other.m10 + m32 * other.m20 + m33 * other.m30;
		float _m31 = m30 * other.m01 + m31 * other.m11 + m32 * other.m21 + m33 * other.m31;
		float _m32 = m30 * other.m02 + m31 * other.m12 + m32 * other.m22 + m33 * other.m32;
		float _m33 = m30 * other.m03 + m31 * other.m13 + m32 * other.m23 + m33 * other.m33;
		
		m00 = _m00; m01 = _m01; m02 = _m02; m03 = _m03;
		m10 = _m10; m11 = _m11; m12 = _m12; m13 = _m13;
		m20 = _m20; m21 = _m21; m22 = _m22; m23 = _m23;
		m30 = _m30; m31 = _m31; m32 = _m32; m33 = _m33;
	}
	
	public void rotateXYZ(float rx, float ry, float rz) {
		rotateX(rx);
		rotateY(ry);
		rotateZ(rz);
	}
	
	public void rotateX(float rx) {
		Matrix3D rotation = new Matrix3D();
		
		float cos = Mathf.cos(rx);
		float sin = Mathf.sin(rx);
		
		rotation.m11 = cos;
		rotation.m12 = -sin;
		rotation.m21 = sin;
		rotation.m22 = cos;
		
		multiply(rotation);
	}
	
	public void rotateY(float ry) {
		Matrix3D rotation = new Matrix3D();
		
		float cos = Mathf.cos(ry);
		float sin = Mathf.sin(ry);
		
		rotation.m00 = cos;
		rotation.m02 = sin;
		rotation.m20 = -sin;
		rotation.m22 = cos;
		
		multiply(rotation);
	}
	
	public void rotateZ(float rz) {
		Matrix3D rotation = new Matrix3D();
		
		float cos = Mathf.cos(rz);
		float sin = Mathf.sin(rz);
		
		rotation.m00 = cos;
		rotation.m01 = -sin;
		rotation.m10 = sin;
		rotation.m11 = cos;
		
		multiply(rotation);
	}
	
	public void translateX(float x) {
		translate(x, 0, 0);
	}
	
	public void translateY(float y) {
		translate(0, y, 0);
	}
	
	public void translateZ(float z) {
		translate(0, 0, z);
	}
	
	public void translate(float x, float y, float z) {
		Matrix3D translation = new Matrix3D();
		
		translation.m30 = x;
		translation.m31 = y;
		translation.m32 = z;
		
		multiply(translation);
	}
	
	public void scale(float s) {
		scale(s, s, s);
	}
	
	public void scale(float sx, float sy, float sz) {
		Matrix3D scale = new Matrix3D();
		
		scale.m00 = sx;
		scale.m11 = sy;
		scale.m22 = sz;
		
		multiply(scale);
	}
	
	public void rotateAroundAxis(Vector3f axis, float theta) {
		Matrix3D rotation = new Matrix3D();
		
		float cos = Mathf.cos(theta);
		float sin = Mathf.sin(theta);
		
		axis.normalize();
		float x = axis.x;
		float y = axis.y;
		float z = axis.z;

		rotation.m00 = cos + x * x * (1 - cos);
		rotation.m01 = x * y * (1 - cos) - z * sin;
		rotation.m02 = x * z * (1 - cos) + y * sin;
		rotation.m10 = y * x * (1 - cos) + z * sin;
		rotation.m11 = cos + y * y * (1 - cos);
		rotation.m12 = y * z * (1 - cos) - x * sin;
		rotation.m20 = z * x * (1 - cos) - y * sin;
		rotation.m21 = z * y * (1 - cos) + x * sin;
		rotation.m22 = cos + z * z * (1 - cos);

		multiply(rotation);
	}
}
