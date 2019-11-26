#version 410

uniform mat4 view;
uniform mat4 transform;
uniform mat4 camera;

uniform vec3 viewPos;
uniform vec3 sunPos;

layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 norm;
layout (location = 2) in vec4 col;

out vec3 normals;
out vec4 colors;
out float diffuse, specular;

const float ambient = 0.1;
const float specularStrength = 1.0;

void main() {
    diffuse = max(ambient, dot(norm, -sunPos));

    vec3 lightDir = normalize(sunPos - pos);
    vec3 viewDir = normalize(viewPos - pos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    specular = specularStrength * spec;

    normals = mat3(transpose(inverse(transform))) * norm;
    colors = col;
    gl_Position = view * camera * transform * vec4(pos, 1.0);
}
