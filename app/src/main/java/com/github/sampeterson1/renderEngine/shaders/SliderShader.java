package com.github.sampeterson1.renderEngine.shaders;

import com.github.sampeterson1.renderEngine.gui.GUIComponent;
import com.github.sampeterson1.renderEngine.gui.GUISlider;

public class SliderShader extends GUIColorShader {

	private static final String VERTEX_FILE = "SliderVert.glsl";
	private static final String FRAGMENT_FILE = "SliderFrag.glsl";

	private static final String[] UNIFORM_NAMES = {
			"transformationMatrix", "sliderDimensions", "handleDimensions",
			"sliderValue", "sliderColor", "handleColor"
	};
	
	public SliderShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected String[] getAllUniformNames() {
		return UNIFORM_NAMES;
	}
	
	@Override
	public void loadGUIComponent(GUIComponent component) {
		GUISlider slider = (GUISlider) component;
		super.loadMatrix("transformationMatrix", slider.getTransform());
		super.loadVector2f("sliderDimensions", slider.getDimensions());
		super.loadVector2f("handleDimensions", GUISlider.SLIDER_HANDLE_DIMENSIONS);
		super.loadFloat("sliderValue", slider.getValue());
		super.loadVector3f("sliderColor", slider.getSliderColor());
		super.loadVector3f("handleColor", slider.getHandleColor());
	}
	
}
