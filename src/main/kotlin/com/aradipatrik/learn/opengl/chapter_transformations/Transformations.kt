package com.aradipatrik.learn.opengl.chapter_transformations

import com.aradipatrik.learn.opengl.*
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33
import kotlin.math.sin

fun main() {
    val window = initWindowWithOpenGlContext()

    val program = createProgram(
        vertexShaderSourceCode = readResourceAsText("/chapter_transformations/vertex.vsh"),
        fragmentShaderSourceCode = readResourceAsText("/chapter_transformations/fragment.fsh")
    )
    GL33.glUseProgram(program)
    setUniform(program, "tex", 0)


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
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, ebo)

    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW)
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, elements, GL33.GL_STATIC_DRAW)

    val stride = 8 * Float.SIZE_BYTES
    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, stride, 0)
    GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, stride, 3L * Float.SIZE_BYTES)
    GL33.glVertexAttribPointer(2, 3, GL33.GL_FLOAT, false, stride, 6L * Float.SIZE_BYTES)
    GL33.glEnableVertexAttribArray(0)
    GL33.glEnableVertexAttribArray(1)
    GL33.glEnableVertexAttribArray(2)

    val texture = createTexture("/textures/awesomeface.png", type = GL33.GL_RGBA)

    val trans = Matrix4f()

    val transformArray = FloatArray(16)

    val transformLoc = GL33.glGetUniformLocation(program, "transform")

    var tick = 0.1f
    loop(window) {
        clear()

        GL33.glUseProgram(program)
        GL33.glActiveTexture(GL33.GL_TEXTURE0)
        GL33.glBindTexture(program, texture)

        tick += 0.1f
        trans.translation(0.5f, -0.5f, 0.0f)
            .rotate(tick, 0f, 0f, 1f)
        GL33.glUniformMatrix4fv(transformLoc, false, trans.get(transformArray))
        GL33.glDrawElements(GL33.GL_TRIANGLES, 6, GL33.GL_UNSIGNED_INT, 0)

        trans.translation(-0.5f, 0.5f, 0.0f)
            .scale(sin(tick))
        GL33.glUniformMatrix4fv(transformLoc, false, trans.get(transformArray))
        GL33.glDrawElements(GL33.GL_TRIANGLES, 6, GL33.GL_UNSIGNED_INT, 0)
    }
}