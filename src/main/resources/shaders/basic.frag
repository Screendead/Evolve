#version 410

layout (location = 0) in vec3 normals;
layout (location = 1) out vec4 colors;

out vec4 fragColor;

const float ambient = 0.1;

void main() {
//	float diffuse = max(ambient,
//		(dot(normals, vec3(0, -1, 0)) * 2 +
//		abs(dot(normals, vec3(1, 0, 0))) +
//		abs(dot(normals, vec3(0, 0, 1))) + 1) / 5);
	float diffuse = 1.0;

	fragColor = vec4(colors.rgb * diffuse, colors.a);
}