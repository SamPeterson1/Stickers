package com.github.sampeterson1.renderEngine.rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.loaders.Loader;
import com.github.sampeterson1.renderEngine.models.MeshData;
import com.github.sampeterson1.renderEngine.models.PieceBatch;
import com.github.sampeterson1.renderEngine.shaders.PieceShader;
import com.github.sampeterson1.renderEngine.shaders.ViewportShader;
import com.github.sampeterson1.renderEngine.window.Window;

public class PuzzleRenderer {

	private static final float PUZZLE_VIEWPORT_SIZE = 0.75f;

	private static final int NUM_ATTRIBS = 8;
	
	private PieceShader pieceShader;
	private ViewportShader viewportShader;
	
	private OrbitalCamera camera;
	private RenderBuffer puzzleViewportMultisample;
	private RenderBuffer puzzleViewport;
	
	public PuzzleRenderer(OrbitalCamera camera) {
		this.pieceShader = new PieceShader();
		this.viewportShader = new ViewportShader();
		this.camera = camera;
		int viewportSizePx = (int) (Window.getHeight() * PUZZLE_VIEWPORT_SIZE);
		this.puzzleViewportMultisample = new RenderBuffer(viewportSizePx, viewportSizePx, true);
		this.puzzleViewport = new RenderBuffer(viewportSizePx, viewportSizePx, false);
	}
	
	private void renderViewport() {
		puzzleViewportMultisample.renderTo(puzzleViewport);
		int viewportSizePx = (int) (Window.getHeight() * PUZZLE_VIEWPORT_SIZE);
		puzzleViewport.render(0, 0, viewportSizePx, viewportSizePx);
	}
	
	private void renderToViewport() {
		puzzleViewportMultisample.bind();
		puzzleViewportMultisample.clear();
		pieceShader.start();
		
		Matrix3D projectionMatrix = Matrix3D.createProjectionMatrix(Scene.getSettings());
		pieceShader.setProjectionMatrix(projectionMatrix);
		pieceShader.setViewMatrix(camera.getViewMatrix());
		pieceShader.setLightDirection(Scene.getLightDirection());
		
		for(PieceBatch pieceBatch : Scene.getPieceBatches()) {
			pieceShader.setColorPalette(pieceBatch.getPalette());
			pieceBatch.prepare();
			MeshData data = pieceBatch.getMesh().getData();
			
			GL30.glBindVertexArray(data.getVaoID());
			for(int i = 0; i < NUM_ATTRIBS; i ++)
				GL20.glEnableVertexAttribArray(i);
			
			GL31.glDrawElementsInstanced(GL11.GL_TRIANGLES, data.getNumIndices(), GL11.GL_UNSIGNED_INT, 0, pieceBatch.getNumInstances());
			
			for(int i = 0; i < NUM_ATTRIBS; i ++)
				GL20.glDisableVertexAttribArray(i);
			GL30.glBindVertexArray(0);
		}
		
		pieceShader.stop();
		puzzleViewportMultisample.unbind();
	}
	
	public void render() {
		renderToViewport();
		renderViewport();
	}
	
	public void dispose() {
		puzzleViewport.cleanUp();
		pieceShader.dispose();
		viewportShader.dispose();
	}
	
}
