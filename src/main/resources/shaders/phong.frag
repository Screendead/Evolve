#version 420

uniform vec3 viewPos;
uniform vec3 sunDir;
uniform vec3 lampPos;

in vec3 normals;
in vec4 colors;
in vec3 fragPos;

out vec4 fragColor;

const float ambient = 0.01;
const float specularStrength = 1.0;

void main() {
    float diffuse = max(ambient, dot(normals, -sunDir));

    vec3 lightDir = normalize(lampPos - fragPos);
    vec3 viewDir = normalize(viewPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, normals);
    float specular = pow(max(dot(viewDir, reflectDir), 0.0), 512) * specularStrength;

    fragColor = vec4((diffuse + specular) * colors.rgb, colors.a);
}
