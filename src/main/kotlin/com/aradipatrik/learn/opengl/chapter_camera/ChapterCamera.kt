package com.aradipatrik.learn.opengl.chapter_camera

import com.aradipatrik.learn.opengl.*
import com.aradipatrik.learn.opengl.models.Box
import com.aradipatrik.learn.opengl.utils.Vectors.yUnit
import com.aradipatrik.learn.opengl.utils.Vectors.zUnit
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL33

fun main() {
    val window = initWindowWithOpenGlContext()

    val vertices = Box.vertices
    val shaderProgram = createProgram(
        vertexShaderSourceCode = readResourceAsText("/chapter_camera/vertex.vsh"),
        fragmentShaderSourceCode = readResourceAsText("/chapter_camera/fragment.fsh")
    )

    val vao = GL33.glGenVertexArrays()
    val vbo = GL33.glGenBuffers()

    GL33.glBindVertexArray(vao)
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo)
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW)
    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 0)
    GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
    GL33.glEnableVertexAttribArray(0)
    GL33.glEnableVertexAttribArray(1)

    GL33.glUseProgram(shaderProgram)
    setUniform(shaderProgram, "tex0", 0)
    setUniform(shaderProgram, "tex1", 1)
    val tex0 = createTexture("/textures/container.jpeg", type = GL33.GL_RGB)
    val tex1 = createTexture("/textures/awesomeface.png", type = GL33.GL_RGBA)

    GL33.glActiveTexture(GL33.GL_TEXTURE0)
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, tex0)
    GL33.glActiveTexture(GL33.GL_TEXTURE1)
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, tex1)

    val modelData = FloatArray(16)
    val model = Matrix4f()

    val projectionData = FloatArray(16)
    Matrix4f()
        .perspective(45.0f, DEFAULT_WINDOW_WIDTH.toFloat() / DEFAULT_WINDOW_HEIGHT, 0.001f, 100f)
        .get(projectionData)

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

    GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(shaderProgram, "projection"), false, projectionData)

    val flyingCamera = FlyingCamera()
    val viewPosition = GL33.glGetUniformLocation(shaderProgram, "view")
    val modelPosition = GL33.glGetUniformLocation(shaderProgram, "model")

    hideCursor(window)
    mouseMoveCallback(window, flyingCamera::processNewMousePosition)
    loop(window) { deltaTime ->
        clear()

        flyingCamera.tick(window, deltaTime)
        GL33.glUniformMatrix4fv(viewPosition, false, flyingCamera.lookAt())

        modelPositions.forEach {
            model.translation(it)
                .get(modelData)
            GL33.glUniformMatrix4fv(modelPosition, false, modelData)

            GL33.glUseProgram(shaderProgram)
            GL33.glBindVertexArray(vao)
            GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 36)
        }
    }
}