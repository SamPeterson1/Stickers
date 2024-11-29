#version 460 core

out vec4 outColor;

in vec2 passPosition;

uniform float size;
uniform vec3 fillColor;
uniform vec3 borderColor;
uniform bool hasFill;


const float innerOffset = 0.0034;
const float borderSize = 0.003;
const float sharpness = 500;
const float r_outerBorder = 0.01;
const float r_innerBorder = 0.007;
const float r_inner = 0.005;

float roundedRectSDF(vec2 centerPos, vec2 dim, float r) {
	return length(max(sharpness * (abs(centerPos) - dim + r), 0.0)) - sharpness * r;
}

void main(void) {
	vec2 toCenter = passPosition - vec2(size, size) / 2;
	vec2 outerSize = vec2(size, size) / 2;
	float outerBorderDistance = roundedRectSDF(toCenter, outerSize, r_outerBorder);
	float innerBorderDistance = roundedRectSDF(toCenter, outerSize - borderSize, r_innerBorder);
	float borderAlpha = 1.0 - smoothstep(0.0, 1.0, outerBorderDistance) - (1.0 - smoothstep(0.0, 1.0, innerBorderDistance));
	
	vec2 innerSize = outerSize - 2 * innerOffset;
	float innerDistance = roundedRectSDF(toCenter, innerSize, r_inner); 
	float innerAlpha = 1.0 - smoothstep(0.0, 1.0, innerDistance);
	if(!hasFill) innerAlpha = 0;
	
	float blendedAlpha = innerAlpha + (1.0 - innerAlpha) * borderAlpha;
	if(blendedAlpha == 0) discard;
	
	vec3 blendedColor = mix(borderColor, fillColor, innerAlpha / blendedAlpha);
	outColor = vec4(blendedColor, blendedAlpha);
}