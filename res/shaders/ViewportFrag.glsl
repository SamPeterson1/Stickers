#version 460 core

out vec4 outColor;

in vec2 passTexCoord;

uniform sampler2D renderTexture;

void main(void) {
	outColor = texture(renderTexture, passTexCoord);
}