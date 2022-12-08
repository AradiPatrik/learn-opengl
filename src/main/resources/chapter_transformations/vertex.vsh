#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;
layout (location = 2) in vec2 texCoord;

uniform mat4 transform;

out vec2 vertexTexCoord;
out vec3 vertexColor;

void main() {
    gl_Position = transform * vec4(position, 1.0);
    vertexTexCoord = texCoord;
    vertexColor = color;
}