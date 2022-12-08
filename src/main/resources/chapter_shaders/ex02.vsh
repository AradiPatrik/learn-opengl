#version 330 core

layout (location = 0) in vec3 position;
uniform float offset;
out vec4 vertexColor;

void main() {
    gl_Position = vec4(position.x + offset, position.y, position.z, 1.0f);
    vertexColor = gl_Position;
}