  #version 460 core
 
 in vec3 position;
 in vec3 normal;
 in int colorGroupID;
 in mat4 transformationMatrix;
 in ivec4 colorIDs;
 
 out vec3 passNormal;
 out vec3 vertColor;
 
 uniform mat4 projectionMatrix;
 uniform mat4 viewMatrix;
 uniform vec3[6] colorPalette;
 
 void main(void) {
 
 	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
 	passNormal = (viewMatrix * transformationMatrix * vec4(normal, 0.0)).xyz;
 	//vertColor = vec3(colorGroupID);
 	vertColor = colorPalette[colorIDs[colorGroupID]];
 }