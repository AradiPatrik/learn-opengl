#version 330 core

in vec2 vTexCoord;

uniform sampler2D tex0;
uniform sampler2D tex1;

out vec4 fragColor;

void main() {
    fragColor = mix(texture(tex0, vTexCoord), texture(tex1, vTexCoord), 0.2f);
}