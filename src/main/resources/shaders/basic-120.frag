#version 120

uniform vec3 viewPos;
uniform vec3 sunPos;
uniform vec3 lampPos;

varying vec3 normals;
varying vec4 colors;
varying vec3 fragPos;

const float ambient = 0.0;
const float specularStrength = 2.0;

void main() {
//    float diffuse = max(ambient, dot(normals, -sunPos));
//
//    vec3 lightDir = normalize(lampPos - fragPos);
//    vec3 viewDir = normalize(viewPos - fragPos);
//    vec3 reflectDir = reflect(-lightDir, normals);
//    float specular = pow(max(dot(viewDir, reflectDir), 0.0), 32) * specularStrength;
//
////    if (colors.rgb == vec3(0.1, 0.4, 1.0)) specular = pow(max(dot(viewDir, reflectDir), 0.0), 256) * specularStrength;
//    specular = pow(max(dot(viewDir, reflectDir), 0.0), 4) * specularStrength * 0.25;
//
//    gl_FragColor = vec4((diffuse + specular * vec3(1, 0.9, 0.9)) * colors.rgb, colors.a);

    gl_FragColor = vec4((0 * viewPos + 0 * sunPos + 0 * lampPos) + 0 * colors.xyz, 0 * colors.a) + vec4(normals.xyz, 1);
}