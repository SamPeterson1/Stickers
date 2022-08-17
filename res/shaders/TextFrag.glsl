#version 460 core

in vec2 passTexCoord;
in vec2 screenPos;

out vec4 outColor;

uniform sampler2D atlas;

uniform float thickness;
uniform float blending;
uniform float offsetThickness;
uniform vec2 minPosition;
uniform vec2 maxPosition;
uniform vec2 offset;
uniform vec3 color;
uniform vec3 offsetColor;

void main(void) {

	if(screenPos.x < minPosition.x || screenPos.y < minPosition.y 
	|| screenPos.x > maxPosition.x || screenPos.y > maxPosition.y) discard;
	
	float distance = 1.0 - texture(atlas, passTexCoord).a;
	float alpha = 1.0 - smoothstep(thickness, thickness + blending, distance);
	
	float offsetDistance = 1.0 - texture(atlas, passTexCoord + offset).a;
	float offsetAlpha = 1.0 - smoothstep(thickness + offsetThickness, thickness + offsetThickness + blending, offsetDistance);
	
	float blendedAlpha = alpha + (1.0 - alpha) * offsetAlpha;
	vec3 blendedColor = mix(offsetColor, color, alpha / blendedAlpha);
	
	outColor = vec4(blendedColor, blendedAlpha);
}