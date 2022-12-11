#version 330 core

in vec2 vertexTexCoord;

out vec4 fragColor;

uniform sampler2D texture1;
uniform sampler2D texture2;

void main() {
    fragColor = mix(texture(texture1, vertexTexCoord), texture(texture2, vertexTexCoord), 0.2);
}