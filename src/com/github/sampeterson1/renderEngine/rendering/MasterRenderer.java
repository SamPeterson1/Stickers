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

import org.lwjgl.opengl.GL11;

public class MasterRenderer {
	
	private PuzzleRenderer puzzleRenderer;
	private GUIRenderer guiRenderer;
	private TextRenderer textRenderer;
	
	public MasterRenderer(OrbitalCamera camera) {
		this.puzzleRenderer = new PuzzleRenderer(camera);
		this.textRenderer = new TextRenderer();
		this.guiRenderer = new GUIRenderer();
		initSettings();
	}
	
	private void initSettings() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void render() {
		puzzleRenderer.render();
		guiRenderer.render();
		textRenderer.render();
	}
	
	public void dispose() {
		puzzleRenderer.dispose();
		textRenderer.dispose();
		guiRenderer.dispose();
	}
}
