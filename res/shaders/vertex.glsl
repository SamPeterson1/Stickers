  #version 460 core
 
 in vec3 position;
 in int colorIndex;
 in vec3 normal;
 in mat4 transformationMatrix;
 
 out vec3 passNormal;
 
 uniform mat4 projectionMatrix;
 uniform mat4 viewMatrix;
 
 void main(void) {
 
 	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
 	passNormal = (viewMatrix * transformationMatrix * vec4(normal, 0.0)).xyz;
 	
 }