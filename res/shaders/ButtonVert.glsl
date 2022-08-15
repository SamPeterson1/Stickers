 #version 460 core
 
in vec2 position;
in vec2 texCoord;
 
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;


void main(void) {
	
	//vec4 screenPos = projectionMatrix * transformationMatrix * vec4(position, 0.0, 1.0);
	gl_Position = vec4(position, 0.0, 1.0);
}