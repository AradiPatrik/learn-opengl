#version 330 core

layout (location = 0) in vec3 position;

void main() {
    vec3 invPos = vec3(0.0f, 0.0f, 0.0f) - position;
    gl_Position = vec4(invPos, 1.0f);
}