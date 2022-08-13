  #version 460 core
 
 in vec3 position;
 in vec3 texCoord;
 
 uniform mat4 transformationMatrix;
 
 void main(void) {
 	gl_Position = transformationMatrix * vec4(position, 1.0);
 }