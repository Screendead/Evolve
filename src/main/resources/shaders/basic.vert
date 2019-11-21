#version 410

uniform mat4 view;
uniform mat4 transform;
uniform mat4 camera;

layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 norm;
layout (location = 2) in vec4 col;

layout (location = 0) out vec3 normals;
layout (location = 1) out vec4 colors;

void main() {
	normals = norm;
	colors = col;
	gl_Position = view * camera * transform * vec4(pos, 1.0);
}
