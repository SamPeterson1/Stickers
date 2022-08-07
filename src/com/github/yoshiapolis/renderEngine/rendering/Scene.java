package com.github.yoshiapolis.renderEngine.rendering;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.math.Matrix3D;
import com.github.yoshiapolis.math.Vector3f;
import com.github.yoshiapolis.puzzle.display.DisplayPiece;

public class Scene {

	private static Vector3f lightDirection;
	private static CameraSettings cameraSettings;
	private static Matrix3D sceneTransformationMat;
	private static List<DisplayPiece> pieces = new ArrayList<DisplayPiece>();

	public static void setSceneTransformationMat(Matrix3D mat) {
		sceneTransformationMat = mat;
	}
	
	public static void setLightDirection(Vector3f direction) {
		lightDirection = direction;
	}
	
	public static void setCameraSettings(CameraSettings settings) {
		cameraSettings = settings;
	}
	
	public static void addPiece(DisplayPiece piece) {
		pieces.add(piece);
	}
	
	public static void removeEntity(DisplayPiece piece) {
		pieces.remove(piece);
	}
	
	public static List<DisplayPiece> getPieces() {
		return pieces;
	}
	
	public static CameraSettings getSettings() {
		return cameraSettings;
	}
	
	public static Vector3f getLightDirection() {
		return lightDirection;
	}
	
	public static Matrix3D getSceneTransformationMat() {
		return sceneTransformationMat;
	}
}
