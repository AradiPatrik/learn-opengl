package com.aradipatrik.learn.opengl

import org.lwjgl.opengl.GL33

private val vertices = floatArrayOf(
    0.5f,  0.5f, 0.0f,  // top right
    0.5f, -0.5f, 0.0f,  // bottom right
    -0.5f, -0.5f, 0.0f,  // bottom left
    -0.5f,  0.5f, 0.0f   // top left
)

private val elements = intArrayOf(
    0, 1, 3,   // first triangle
    1, 2, 3    // second triangle
)

private val vertexShaderSource = readResourceAsText("/vertex_shader.glsl")
private val fragmentShaderSource = readResourceAsText("/fragment_shader.glsl")

fun main() {
    val window = initWindowWithOpenGlContext()
    val shaderProgram = createProgram(vertexShaderSource, fragmentShaderSource)
    val vertexArrayObject = createVertexArrayObject(vertices, elements)

    loop(window) {
        clear()

        GL33.glUseProgram(shaderProgram)
        GL33.glBindVertexArray(vertexArrayObject)
        GL33.glDrawElements(GL33.GL_TRIANGLES, 6, GL33.GL_UNSIGNED_INT, 0)
    }
}