#version 330 core

layout (location = 0) in vec3 position;
out vec4 vertexColor;

void main() {
    gl_Position = vec4(position, 1.0);
    vertexColor = vec4(1.0, 0.2, 0.4, 0.8);
}