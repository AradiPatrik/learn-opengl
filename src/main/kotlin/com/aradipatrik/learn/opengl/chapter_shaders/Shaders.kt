package com.aradipatrik.learn.opengl.chapter_shaders

import com.aradipatrik.learn.opengl.*
import org.lwjgl.opengl.GL33

private val vertices = floatArrayOf(
    // vertices        // colors
    0.5f,  0.5f, 0.0f, 1f, 0f, 0f,  // top right
    0.5f, -0.5f, 0.0f, 0.0f, 1f, 0f, // bottom right
    -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1f,  // bottom left
    -0.5f,  0.5f, 0.0f, 0.5f, 0.5f, 0.5f   // top left
)

private val elements = intArrayOf(
    0, 1, 3,   // first triangle
    1, 2, 3    // second triangle
)

fun main() {
    extraAttribute()
}

@Suppress("unused")
private fun extraAttribute() {
    val window = initWindowWithOpenGlContext()

    val program = createProgram(
        vertexShaderSourceCode = readResourceAsText("/chapter_shaders/vertex_with_color.vsh"),
        fragmentShaderSourceCode = readResourceAsText("/chapter_shaders/fragment_with_input.fsh")
    )

    val vao = GL33.glGenVertexArrays()
    GL33.glBindVertexArray(vao)

    val buffer = GL33.glGenBuffers()
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, buffer)
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW)
    GL33.glVertexAttribPointer(VERTEX_ATTRIBUTE_POSITION, 3, GL33.GL_FLOAT, false, 6 * Float.SIZE_BYTES, 0)
    GL33.glEnableVertexAttribArray(0)
    GL33.glVertexAttribPointer(VERTEX_ATTRIBUTE_COLOR, 3, GL33.GL_FLOAT, false, 6 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
    GL33.glEnableVertexAttribArray(1)

    val elementBuffer = GL33.glGenBuffers()
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, elementBuffer)
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, elements, GL33.GL_STATIC_DRAW)

    loop(window) {
        clear()

        GL33.glUseProgram(program)
        GL33.glBindVertexArray(vao)
        GL33.glDrawElements(GL33.GL_TRIANGLES, 6, GL33.GL_UNSIGNED_INT, 0)
    }
}

@Suppress("unused")
private fun uniform() {
    val window = initWindowWithOpenGlContext()

    val program = createProgram(
        vertexShaderSourceCode = readResourceAsText("/chapter_shaders/vertex_default.vsh"),
        fragmentShaderSourceCode = readResourceAsText("/chapter_shaders/fragment_with_uniform.fsh")
    )

    val vao = createVertexArrayObject(vertices, elements)

    loop(window) {
        clear()

        GL33.glUseProgram(program)
        GL33.glUniform4f(
            GL33.glGetUniformLocation(program, "color"),
            1.0f, 0.3f, 0.4f, 0.5f
        )
        GL33.glBindVertexArray(vao)
        GL33.glDrawElements(GL33.GL_TRIANGLES, 6, GL33.GL_UNSIGNED_INT, 0)
    }
}

@Suppress("unused")
private fun inputOutput() {
    val window = initWindowWithOpenGlContext()

    val program = createProgram(
        vertexShaderSourceCode = readResourceAsText("/chapter_shaders/vertex_with_output.vsh"),
        fragmentShaderSourceCode = readResourceAsText("/chapter_shaders/fragment_with_input.fsh")
    )

    val vao = createVertexArrayObject(vertices, elements)

    loop(window) {
        clear()

        GL33.glUseProgram(program)
        GL33.glBindVertexArray(vao)
        GL33.glDrawElements(GL33.GL_TRIANGLES, 6, GL33.GL_UNSIGNED_INT, 0)
    }
}

