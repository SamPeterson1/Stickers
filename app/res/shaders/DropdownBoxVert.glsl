 #version 460 core
 
in vec2 position;
in vec2 texCoord;
in int optionID;

out vec4 color;
out vec2 passTexCoord;

uniform bool expanded;
uniform int selectionID;
uniform vec3 selectionColor;
uniform mat4 transformationMatrix;


void main(void) {
	
	if(optionID == selectionID && selectionID >= 0) {
		color = vec4(selectionColor, 1.0);
	} else if(optionID % 2 == 1) {
		color = vec4(1.0);
	} else {
		color = vec4(0.97, 0.97, 0.97, 1.0);
	}
	
	if(expanded) {
		passTexCoord = vec2(texCoord.y, 1.0 - texCoord.x);
	} else {
		if(optionID >= 0) color = vec4(0);
		passTexCoord = texCoord;	
	}
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
}