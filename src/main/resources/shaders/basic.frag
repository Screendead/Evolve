#version 410

//uniform vec3 sun;

in vec3 normals;
in vec4 colors;

out vec4 fragColor;

const float ambient = 0.1;

void main() {
	float diffuse = max(ambient,
			dot(normals, -vec3(0, -1, 0)));

	fragColor = vec4(colors.rgb * diffuse, colors.a);

//	fragColor = colors;
//	fragColor = vec4(1, 1, 1, 1);
}
