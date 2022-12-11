package com.aradipatrik.learn.opengl.chapter_coord_sys

import com.aradipatrik.learn.opengl.*
import com.aradipatrik.learn.opengl.models.Box
import org.joml.Math
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL33

fun main() {
    val window = initWindowWithOpenGlContext()

    val program = createProgram(
        readResourceAsText("/chapter_coord_sys/vertex.vsh"),
        readResourceAsText("/chapter_coord_sys/fragment.fsh")
    )
    GL33.glUseProgram(program)
    setUniform(program, "texture1", 0)
    setUniform(program, "texture2", 1)

    val texture1 = createTexture("/textures/container.jpeg", type = GL33.GL_RGB)
    val texture2 = createTexture("/textures/awesomeface.png", type = GL33.GL_RGBA)
    GL33.glActiveTexture(GL33.GL_TEXTURE0)
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture1)
    GL33.glActiveTexture(GL33.GL_TEXTURE1)
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture2)

    val vertices = Box.vertices

    val modelPositions = listOf(
        Vector3f( 0.0f,  0.0f,  0.0f),
        Vector3f( 2.0f,  5.0f, -15.0f),
        Vector3f(-1.5f, -2.2f, -2.5f),
        Vector3f(-3.8f, -2.0f, -12.3f),
        Vector3f( 2.4f, -0.4f, -3.5f),
        Vector3f(-1.7f,  3.0f, -7.5f),
        Vector3f( 1.3f, -2.0f, -2.5f),
        Vector3f( 1.5f,  2.0f, -2.5f),
        Vector3f( 1.5f,  0.2f, -1.5f),
        Vector3f(-1.3f,  1.0f, -1.5f)
    )

    val vao = GL33.glGenVertexArrays()
    val vbo = GL33.glGenBuffers()
    val ebo = GL33.glGenBuffers()

    GL33.glBindVertexArray(vao)
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo)
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, ebo)

    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW)

    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 0)
    GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)

    GL33.glEnableVertexAttribArray(0)
    GL33.glEnableVertexAttribArray(1)

    val matrixHolder = FloatArray(16)

    val model = Matrix4f()

    val view = Matrix4f()
        .translate(0.0f, 0.0f, -6.0f)

    val projection = Matrix4f()
        .perspective(Math.toRadians(55f), DEFAULT_WINDOW_WIDTH.toFloat() / DEFAULT_WINDOW_HEIGHT.toFloat(), 0.1f, 100f)

    view.get(matrixHolder)
    GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(program, "view"), false, matrixHolder)

    projection.get(matrixHolder)
    GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(program, "projection"), false, matrixHolder)

    var tick = 0.0f
    val axis = Vector3f(0.5f, 0.5f, 0.2f).normalize()

    loop(window) {
        clear()

        tick += 1f

        for (modelPosition in modelPositions) {
            model.translation(modelPosition)
                .rotate(Math.toRadians(tick), axis)
            model.get(matrixHolder)
            GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(program, "model"), false, matrixHolder)
            GL33.glUseProgram(program)
            GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 36)
        }
    }
}