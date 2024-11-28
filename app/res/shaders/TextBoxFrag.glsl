#version 460 core

out vec4 outColor;

in vec2 passPosition;
in vec2 texCoord;

uniform vec3 borderColor;
uniform vec3 fillColor;
uniform vec2 dimensions;

uniform sampler2D cursor;

const float borderSize = 0.004;
const float borderOverlap = 0.001;
const float sharpness = 500;
const float r_outerBorder = 0.01;
const float r_inner = 0.007;

float roundedRectSDF(vec2 centerPos, vec2 dim, float r) {
	return length(max(sharpness * (abs(centerPos) - dim + r), 0.0)) - sharpness * r;
}

void main(void) {
	vec2 toCenter = passPosition - dimensions;
	vec2 outerSize = dimensions;
	float outerBorderDistance = roundedRectSDF(toCenter, outerSize, r_outerBorder);
	float innerBorderDistance = roundedRectSDF(toCenter, outerSize - borderSize, r_inner);
	float borderAlpha = 1.0 - smoothstep(0.0, 1.0, outerBorderDistance) - (1.0 - smoothstep(0.0, 1.0, innerBorderDistance));
	
	vec2 innerSize = outerSize - borderSize + borderOverlap;
	float innerDistance = roundedRectSDF(toCenter, innerSize, r_inner); 
	float innerAlpha = 1.0 - smoothstep(0.0, 1.0, innerDistance);
	
	float cursorAlpha = texture(cursor, texCoord).a;
	if(cursorAlpha > 0) {
		float blendedAlpha = cursorAlpha + (1.0 - cursorAlpha) * innerAlpha;		
		vec3 blendedColor = mix(fillColor, vec3(0.0), cursorAlpha / blendedAlpha);
		outColor = vec4(blendedColor, blendedAlpha);
	} else {
		float blendedAlpha = innerAlpha + (1.0 - innerAlpha) * borderAlpha;
		if(blendedAlpha == 0) discard;
		
		vec3 blendedColor = mix(borderColor, fillColor, innerAlpha / blendedAlpha);
		outColor = vec4(blendedColor, blendedAlpha);
	}
}