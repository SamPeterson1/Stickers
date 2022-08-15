  #version 460 core
 
in vec2 position;
in vec2 texCoord;

out vec2 passTexCoord;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
 
 void main(void) {
 	gl_Position = projectionMatrix * transformationMatrix * vec4(position, 0.0, 1.0);
	passTexCoord = texCoord;
 }