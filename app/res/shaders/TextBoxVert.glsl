 #version 460 core
 
in vec2 position;

out vec2 passPosition;
out vec2 texCoord;

uniform float cursorPosition;
uniform vec2 dimensions;
uniform mat4 transformationMatrix;
uniform float cursorHeight;

void main(void) {
	
	float aspect = dimensions.x / dimensions.y;
	float offset = -cursorPosition * aspect + 0.5;
	float cursorPadding = (1 - cursorHeight) / 2;
	
	float texX = (position.x < 0) ? offset : (dimensions.x / dimensions.y) + offset;
	float texY = (position.y < 0) ? (-cursorPadding) : (1 + cursorPadding);
	
	texCoord = vec2(texX, texY);
	passPosition = position;
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
}