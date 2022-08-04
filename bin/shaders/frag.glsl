#version 460 core

in vec2 passTexCoord;
in vec3 passNormal;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform vec3 lightDirection;

void main(void) {

	vec3 unitNormal = normalize(passNormal);
	vec3 unitLightDirection = normalize(lightDirection);

	float diffuse = dot(unitNormal, unitLightDirection);
	diffuse = max(diffuse, 0.2);
	
	outColor = diffuse * texture(textureSampler, passTexCoord);
	
}