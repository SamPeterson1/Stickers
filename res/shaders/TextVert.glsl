  #version 460 core
 
in vec3 position;
in vec2 texCoord;

out vec2 passTexCoord;

uniform mat4 transformationMatrix;
 
 void main(void) {
 	gl_Position = transformationMatrix * vec4(position, 1.0);
	passTexCoord = texCoord;
 }