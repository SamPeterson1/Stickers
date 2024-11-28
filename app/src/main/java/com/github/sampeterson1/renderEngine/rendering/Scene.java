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

package com.github.sampeterson1.renderEngine.rendering;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector3f;
import com.github.sampeterson1.renderEngine.models.Entity;
import com.github.sampeterson1.renderEngine.models.PieceBatch;

public class Scene {

	private static Vector3f lightDirection;
	private static CameraSettings cameraSettings;
	private static Matrix3D sceneTransformationMat;
	
	private static List<PieceBatch> pieceBatches = new ArrayList<PieceBatch>();
	private static Map<MeshType, List<Entity>> entities = new EnumMap<MeshType, List<Entity>>(MeshType.class);

	public static void addEntity(Entity entity) {
		MeshType type = entity.getMesh().getType();
		if(entities.containsKey(type)) {
			entities.get(type).add(entity);
		} else {
			List<Entity> list = new ArrayList<Entity>();
			list.add(entity);
			entities.put(type, list);
		}
	}
	
	public static void setSceneTransformationMat(Matrix3D mat) {
		sceneTransformationMat = mat;
	}
	
	public static void setLightDirection(Vector3f direction) {
		lightDirection = direction;
	}
	
	public static void setCameraSettings(CameraSettings settings) {
		cameraSettings = settings;
	}
	
	public static void clearPieceBatches() {
		pieceBatches = new ArrayList<PieceBatch>();
	}
	
	public static void addPieceBatch(PieceBatch pieceBatch) {
		pieceBatches.add(pieceBatch);
	}

	public static List<PieceBatch> getPieceBatches() {
		return pieceBatches;
	}
	
	public static List<Entity> getEntities(MeshType type) {
		return entities.get(type);
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
