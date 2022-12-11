package com.aradipatrik.learn.opengl.gen_chunk

import com.aradipatrik.learn.opengl.*
import com.aradipatrik.learn.opengl.models.Meshes
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL33

fun main() {
    val window = initWindowWithOpenGlContext()

    val vertices = Meshes.Box.vertices
    val elements = Meshes.Box.elements
    val shaderProgram = createProgram(
        vertexShaderSourceCode = readResourceAsText("/gen_chunk/vertex.vsh"),
        fragmentShaderSourceCode = readResourceAsText("/gen_chunk/fragment.fsh")
    )

    val vao = GL33.glGenVertexArrays()
    val vbo = GL33.glGenBuffers()
    val ebo = GL33.glGenBuffers()

    GL33.glBindVertexArray(vao)
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo)
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW)
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, ebo)
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, elements, GL33.GL_STATIC_DRAW)
    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0)
    GL33.glEnableVertexAttribArray(0)

    val modelData = FloatArray(16)
    val model = Matrix4f()

    val projectionData = FloatArray(16)
    Matrix4f()
        .perspective(45.0f, DEFAULT_WINDOW_WIDTH.toFloat() / DEFAULT_WINDOW_HEIGHT, 0.1f, 100f)
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
    wireframeEnabled(true)
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
            GL33.glDrawElements(GL33.GL_TRIANGLES, 6 * 6, GL33.GL_UNSIGNED_INT, 0L)
        }
    }
}