#version 460 core

out vec4 outColor;

uniform vec3 color;
uniform vec3 shadowColor;
uniform vec2 buttonPos;
uniform vec2 buttonDim;
uniform float shadowOffsetPx;
uniform bool pressed;

const float r = 10;

float roundedRectSDF(vec2 centerPos, vec2 dim, float r) {
	return length(max(abs(centerPos) - dim + r, 0.0)) - r;
}

void main(void) {

	vec2 shadowOffset = vec2(shadowOffsetPx, -shadowOffsetPx);
	vec2 baseOffset = vec2(0.0, 0.0);
	if(pressed) baseOffset = shadowOffset;
	float distance = roundedRectSDF(gl_FragCoord.xy - buttonPos + baseOffset, buttonDim, r);
	float alpha = 1.0 - smoothstep(0.0, 1.0, distance);
	
	float offsetDistance = roundedRectSDF(gl_FragCoord.xy - buttonPos + shadowOffset, buttonDim, r);
	float offsetAlpha = 1.0 - smoothstep(0.0, 1.0, offsetDistance);
	if(pressed) offsetAlpha = 0;
	
	float blendedAlpha = alpha + (1.0 - alpha) * offsetAlpha;
	vec3 blendedColor = mix(shadowColor, color, alpha / blendedAlpha);
	if(blendedAlpha == 0) discard;
	
	outColor = vec4(blendedColor, blendedAlpha);
}