package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.math.Matrix3D;
import com.github.sampeterson1.math.Vector2f;
import com.github.sampeterson1.renderEngine.gui.GUIButton;
import com.github.sampeterson1.renderEngine.gui.GUIMaster;
import com.github.sampeterson1.renderEngine.window.Window;

public class ButtonShader extends Shader {

	private static final String VERTEX_FILE = "ButtonVert.glsl";
	private static final String FRAGMENT_FILE = "ButtonFrag.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_color;
	private int location_shadowColor;
	private int location_buttonPos;
	private int location_buttonDim;
	private int location_shadowOffsetPx;
	private int location_pressed;
	
	public ButtonShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_color = super.getUniformLocation("color");
		location_shadowColor = super.getUniformLocation("shadowColor");
		location_buttonPos = super.getUniformLocation("buttonPos");
		location_buttonDim = super.getUniformLocation("buttonDim");
		location_shadowOffsetPx = super.getUniformLocation("shadowOffsetPx");
		location_pressed = super.getUniformLocation("pressed");
	}
	
	public void loadButton(GUIButton button) {
		Vector2f windowScale = new Vector2f(Window.getWidth(), Window.getHeight());
		
		super.loadMatrix(location_projectionMatrix, GUIMaster.orthoProjection);
		super.loadMatrix(location_transformationMatrix, button.getTransform());
		super.loadVector3f(location_color, button.getColor());
		super.loadVector3f(location_shadowColor, button.shadowColor);
		super.loadVector2f(location_buttonPos, button.getCenterPos().mult(windowScale));
		super.loadVector2f(location_buttonDim, button.getDim().mult(windowScale));
		super.loadFloat(location_shadowOffsetPx, button.shadowOffset * Window.getHeight());
		super.loadBoolean(location_pressed, button.isPressed());
	}
	
}
