#version 460 core

in vec2 passTexCoord;

out vec4 outColor;

uniform sampler2D atlas;

void main(void) {
	outColor = texture(atlas, passTexCoord);
}