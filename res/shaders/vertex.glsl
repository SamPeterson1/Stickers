  #version 460 core
 
 in vec3 position;
 in vec2 texCoord;
 in vec3 normal;
 
 out vec2 passTexCoord;
 out vec3 passNormal;
 
 uniform mat4 transformationMatrix;
 uniform mat4 projectionMatrix;
 
 void main(void) {
 
 	gl_Position = projectionMatrix * transformationMatrix * vec4(position, 1.0);
 	passTexCoord = texCoord;
 	passNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
 	
 }