  #version 460 core
 
in vec2 position;
in vec2 texCoord;

out vec2 passTexCoord;
out vec2 screenPos;

uniform mat4 transformationMatrix;
 
 void main(void) {
 
 	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
 	screenPos = (gl_Position.xy + 1) / 2;
	passTexCoord = texCoord;
 }