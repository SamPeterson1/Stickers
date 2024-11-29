 #version 460 core
 
in vec2 position;
in vec2 texCoord;

out vec2 passPosition;

uniform mat4 transformationMatrix;

void main(void) {
	passPosition = position;
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
}