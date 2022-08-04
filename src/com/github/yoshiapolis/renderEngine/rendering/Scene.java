package com.github.yoshiapolis.renderEngine.rendering;

import java.util.ArrayList;
import java.util.List;

import com.github.yoshiapolis.math.Vector3f;

public class Scene {

	private static Vector3f lightDirection;
	private static CameraSettings cameraSettings;
	private static List<Entity> entities = new ArrayList<Entity>();

	public static void setLightDirection(Vector3f direction) {
		lightDirection = direction;
	}
	
	public static void setCameraSettings(CameraSettings settings) {
		cameraSettings = settings;
	}
	
	public static void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	public static void removeEntity(Entity entity) {
		entities.remove(entity);
	}
	
	public static List<Entity> getEntities() {
		return entities;
	}
	
	public static CameraSettings getSettings() {
		return cameraSettings;
	}
	
	public static Vector3f getLightDirection() {
		return lightDirection;
	}
}
