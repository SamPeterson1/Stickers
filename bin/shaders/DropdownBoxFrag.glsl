#version 460 core

out vec4 outColor;

in vec4 color;
in vec2 passTexCoord;

uniform sampler2D arrowTex;
uniform vec3 arrowColor;

void main(void) {
	float alpha = texture(arrowTex, passTexCoord).a;
	if(alpha > 0.1) {
		outColor = vec4(arrowColor * alpha, 1.0);
	} else {
		if(color.a == 0) discard;
		outColor = vec4(color);
	}
}