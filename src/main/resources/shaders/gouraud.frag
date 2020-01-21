#version 420

in vec3 normals;
in vec4 colors;
in float diffuse, specular;

out vec4 fragColor;

void main() {
    fragColor = vec4((diffuse + specular) * colors.rgb, colors.a);
}
