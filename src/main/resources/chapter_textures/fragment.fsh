#version 330 core

out vec4 fragmentColor;
in vec3 vertexColor;
in vec2 vertexTexCoord;

uniform sampler2D tex1;
uniform sampler2D tex2;

void main() {
    fragmentColor = mix(texture(tex1, vertexTexCoord), texture(tex2, vertexTexCoord), 0.2);
}