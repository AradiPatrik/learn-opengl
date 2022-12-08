package com.aradipatrik.learn.opengl.chapter_textures

import com.aradipatrik.learn.opengl.*
import org.lwjgl.opengl.GL33

fun main() {
    val window = initWindowWithOpenGlContext()

    val program = createProgram(
        vertexShaderSourceCode = readResourceAsText("/chapter_textures/vertex.vsh"),
        fragmentShaderSourceCode = readResourceAsText("/chapter_textures/fragment.fsh")
    )
    GL33.glUseProgram(program)
    setUniform(program, "tex1", 0)
    setUniform(program, "tex2", 1)


    val vertices = floatArrayOf(
        // positions      // colors        // texture coords
        0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
        0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // bottom right
        -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom left
        -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, // top left
    )
    val elements = intArrayOf(
        0, 1, 3, // first triangle
        1, 2, 3,  // second triangle
    )

    val vao = GL33.glGenVertexArrays()
    val vbo = GL33.glGenBuffers()
    val ebo = GL33.glGenBuffers()

    GL33.glBindVertexArray(vao)
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo)
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW)
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, ebo)
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, elements, GL33.GL_STATIC_DRAW)

    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 8 * Float.SIZE_BYTES, 0)
    GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 8 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
    GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 8 * Float.SIZE_BYTES, 6L * Float.SIZE_BYTES)
    GL33.glEnableVertexAttribArray(0)
    GL33.glEnableVertexAttribArray(1)
    GL33.glEnableVertexAttribArray(2)

    val texture1 = createTexture("/textures/container.jpeg", type = GL33.GL_RGB)
    val texture2 = createTexture("/textures/awesomeface.png", type = GL33.GL_RGBA)

    loop(window) {
        clear()

        GL33.glUseProgram(program)
        GL33.glBindVertexArray(vao)
        GL33.glActiveTexture(GL33.GL_TEXTURE0)
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture1)
        GL33.glActiveTexture(GL33.GL_TEXTURE1)
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture2)
        GL33.glDrawElements(GL33.GL_TRIANGLES, 6, GL33.GL_UNSIGNED_INT, 0)
    }

}