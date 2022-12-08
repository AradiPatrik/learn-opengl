#version 330 core

in vec3 vertexColor;
in vec2 vertexTexCoord;

uniform sampler2D tex;

out vec4 fragmentColor;

void main() {
    fragmentColor = texture(tex, vertexTexCoord);
}
