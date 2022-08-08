#version 460 core

in vec3 passVertColor;
in vec3 passNormal;

out vec4 outColor;

uniform vec3 lightDirection;

void main(void) {
	vec3 unitNormal = normalize(passNormal);
	vec3 unitLightDirection = normalize(lightDirection);

	float diffuse = dot(unitNormal, unitLightDirection);
	diffuse = max(diffuse, 0.3);
	
	outColor = diffuse * vec4(passVertColor, 1.0);
}