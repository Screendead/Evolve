#version 410

uniform vec3 viewPos;
uniform vec3 sunPos;
uniform vec3 lampPos;

in vec3 normals;
in vec4 colors;
in vec3 fragPos;

out vec4 fragColor;

const float ambient = 0.1;
const float specularStrength = 1.0;

void main() {
    float diffuse = max(ambient, dot(normals, -sunPos));

    vec3 lightDir = normalize(lampPos - fragPos);
    vec3 viewDir = normalize(viewPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, normals);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 16);
    float specular = specularStrength * spec;

    fragColor = vec4((diffuse + specular) * colors.rgb, colors.a);
}
