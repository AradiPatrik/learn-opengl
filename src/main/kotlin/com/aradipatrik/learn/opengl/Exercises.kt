package com.aradipatrik.learn.opengl

import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL33

private val vertexShaderSource = readResourceAsText("/vertex_shader.glsl")
private val fragmentShaderSource = readResourceAsText("/fragment_shader.glsl")
private val secondFragmentShaderSource = readResourceAsText("/fragment_shader_exercise.glsl")
private val vertices = floatArrayOf(
    -0.25f, 0.5f, 0.0f, // first top
    -0.125f, -0.5f, 0.0f, // first bottom right
    -0.375f, -0.5f, 0.0f, // first bottom left
    0.25f, 0.5f, 0.0f, // second top
    0.375f, -0.5f, 0.0f, // second bottom left
    0.125f, -0.5f, 0.0f, // second bottom right
)

private val verticesA = floatArrayOf(
    -0.25f, 0.5f, 0.0f, // first top
    -0.125f, -0.5f, 0.0f, // first bottom right
    -0.375f, -0.5f, 0.0f, // first bottom left
)

private val verticesB = floatArrayOf(
    0.25f, 0.5f, 0.0f, // second top
    0.375f, -0.5f, 0.0f, // second bottom left
    0.125f, -0.5f, 0.0f, // second bottom right
)

fun main() {
    ex02()
}

@Suppress("unused")
private fun ex01() {
    val window = initWindowWithOpenGlContext()
    val shaderProgram = createProgram(vertexShaderSource, fragmentShaderSource)
    val vertexArrayObject = createVertexArrayObject(vertices)

    loop(window) {
        clear()

        GL33.glUseProgram(shaderProgram)
        GL33.glBindVertexArray(vertexArrayObject)
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 6 * 3)
    }
}

@Suppress("unused")
private fun ex02() {
    val window = initWindowWithOpenGlContext()
    val shaderProgram = createProgram(vertexShaderSource, fragmentShaderSource)
    val secondShaderProgram = createProgram(vertexShaderSource, secondFragmentShaderSource)
    val leftTriVao = createVertexArrayObject(verticesA)
    val rightTriVao = createVertexArrayObject(verticesB)

    loop(window) {
        clear()

        GL33.glUseProgram(shaderProgram)
        GL33.glBindVertexArray(leftTriVao)
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 3)

        GL33.glUseProgram(secondShaderProgram)
        GL33.glBindVertexArray(rightTriVao)
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 3)
    }
}