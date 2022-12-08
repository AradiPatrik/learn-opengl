#version 330 core

out vec4 fragmentColor;
in vec3 vTexColor;
in vec2 vTexCoord;

uniform sampler2D tex1;
uniform sampler2D tex2;
uniform float ratio;

void main() {

    fragmentColor = mix(
        texture(tex1, vTexCoord),
        texture(tex2, vTexCoord),
        ratio
    );
}