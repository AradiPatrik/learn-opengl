package com.aradipatrik.learn.opengl.chapter_shaders

import com.aradipatrik.learn.opengl.*
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.opengl.GL33

fun main() {
    ex02()
}

@Suppress("unused")
fun ex01() {
    val window = initWindowWithOpenGlContext()
    val shaderProgram = createProgram(
        readResourceAsText("/chapter_shaders/ex01.vsh"),
        readResourceAsText("/fragment_shader.glsl")
    )
    val vertexArrayObject = createVertexArrayObject(floatArrayOf(
        0.0f, 1.0f, 0.0f,
        -1f, -1f, 0.0f,
        1f, -1f, 1.0f
    ))

    loop(window) {
        clear()

        GL33.glUseProgram(shaderProgram)
        GL33.glBindVertexArray(vertexArrayObject)
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 3)
    }
}

fun ex02() {
    val window = initWindowWithOpenGlContext()
    val shaderProgram = createProgram(
        readResourceAsText("/chapter_shaders/ex02.vsh"),
        readResourceAsText("/chapter_shaders/ex02.fsh")
    )
    val vertexArrayObject = createVertexArrayObject(floatArrayOf(
        0.0f, 1.0f, 0.0f,
        -1f, -1f, 1.0f,
        1f, -1f, 0.0f
    ))

    var offset = 0f
    val offsetLocation = GL33.glGetUniformLocation(shaderProgram, "offset")
    GL33.glUniform1f(offsetLocation, offset)

    loop(window) {
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
            offset += 0.01f
            GL33.glUniform1f(offsetLocation, offset)
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
            offset -= 0.01f
            GL33.glUniform1f(offsetLocation, offset)
        }

        clear()

        GL33.glUseProgram(shaderProgram)
        GL33.glBindVertexArray(vertexArrayObject)
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 6)
    }
}