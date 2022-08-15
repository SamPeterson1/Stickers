#version 460 core

out vec4 outColor;

in vec2 passPosition;

uniform vec3 color;
uniform vec3 shadowColor;
uniform vec2 buttonDim;
uniform float shadowOffset;
uniform bool pressed;

const float r = 0.01;
const float sharpness = 500;

float roundedRectSDF(vec2 centerPos, vec2 dim, float r) {
	return length(max(sharpness*(abs(centerPos) - dim + r), 0.0)) - sharpness*r;
}

void main(void) {

	vec2 shadowOffset = vec2(shadowOffset, -shadowOffset);
	float distance = roundedRectSDF(passPosition - buttonDim, buttonDim, r);
	float alpha = 1.0 - smoothstep(0.0, 1.0, distance);
	
	float offsetDistance = roundedRectSDF(passPosition - buttonDim + shadowOffset, buttonDim, r);
	float offsetAlpha = 1.0 - smoothstep(0.0, 1.0, offsetDistance);
	if(pressed) offsetAlpha = 0;
	
	float blendedAlpha = alpha + (1.0 - alpha) * offsetAlpha;
	vec3 blendedColor = mix(shadowColor, color, alpha / blendedAlpha);
	if(blendedAlpha == 0) discard;
	
	outColor = vec4(blendedColor, blendedAlpha);
}