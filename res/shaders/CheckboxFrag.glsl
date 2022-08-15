#version 460 core

out vec4 outColor;

in vec2 passPosition;

const float sharpness = 500;
const float r = 0.004;

float roundedRectSDF(vec2 centerPos, vec2 dim, float r) {
	return length(max(sharpness * (abs(centerPos) - dim + r), 0.0)) - sharpness * r;
}

void main(void) {
	outColor = vec4(0.0);
}