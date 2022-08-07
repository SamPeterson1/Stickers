  #version 460 core
 
 in vec3 position;
 in vec3 vertColor;
 in vec3 normal;
 
 out vec3 passVertColor;
 out vec3 passNormal;
 
 uniform mat4 transformationMatrix;
 uniform mat4 projectionMatrix;
 uniform mat4 viewMatrix;
 
 void main(void) {
 
 	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
 	passVertColor = vertColor;
 	passNormal = (viewMatrix * transformationMatrix * vec4(normal, 0.0)).xyz;
 	
 }