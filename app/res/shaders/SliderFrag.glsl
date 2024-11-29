#version 460 core

out vec4 outColor;

in vec2 passPosition;

uniform vec3 sliderColor;
uniform vec3 handleColor;
uniform vec2 sliderDimensions;
uniform vec2 handleDimensions;
uniform float sliderValue;

const float sharpness = 500;
const float r = 0.004;

float roundedRectSDF(vec2 centerPos, vec2 dim, float r) {
	return length(max(sharpness * (abs(centerPos) - dim + r), 0.0)) - sharpness * r;
}

void main(void) {
	
	vec2 center = vec2(sliderDimensions.x, handleDimensions.y);
	vec2 toCenter = passPosition - center;
	
	float distance = roundedRectSDF(toCenter, sliderDimensions, r);
	float alpha = 1.0 - smoothstep(0.0, 1.0, distance);
	
    float handleOffset = (sliderValue - 0.5) * 2 * sliderDimensions.x;
	float handleDistance = roundedRectSDF(toCenter - vec2(handleOffset, 0.0), handleDimensions, r);
	float handleAlpha = 1.0 - smoothstep(0.0, 1.0, handleDistance);
	
	float blendedAlpha = handleAlpha + (1.0 - handleAlpha) * alpha;
	if(blendedAlpha == 0) discard;
	
	vec3 blendedColor = mix(sliderColor, handleColor, handleAlpha / blendedAlpha);
	
	outColor = vec4(blendedColor, blendedAlpha);
}