#version 410

uniform mat4 view;
uniform mat4 transform;
uniform mat4 camera;

layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 norm;
layout (location = 2) in vec4 col;

out vec3 normals;
out vec4 colors;
out vec3 fragPos;

void main() {
	normals = mat3(transpose(inverse(transform))) * norm;
	colors = col;
	fragPos = pos;
	gl_Position = view * camera * transform * vec4(pos, 1.0);
}
