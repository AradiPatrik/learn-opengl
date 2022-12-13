package com.aradipatrik.learn.opengl.gen_chunk

import com.aradipatrik.learn.opengl.*
import com.aradipatrik.learn.opengl.models.Meshes
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL33

fun main() {
    val window = initWindowWithOpenGlContext()

    val vertices = Meshes.Box.vertices
    val shaderProgram = createProgram(
        vertexShaderSourceCode = readResourceAsText("/gen_chunk/vertex.vsh"),
        fragmentShaderSourceCode = readResourceAsText("/gen_chunk/fragment.fsh")
    )

    createAndBindGrassTexture(shaderProgram)
    val vao = createAndBindBoxMesh(vertices)

    setPerspectiveProjection(shaderProgram)

    val modelPositions = createModelPositions()

    val (viewPosition, modelUniformPosition) = getViewAndModelUniformPositions(shaderProgram)

    val flyingCamera = initCamera(window)

    val modelData = FloatArray(16)
    val model = Matrix4f()
    loop(window) { deltaTime ->
        clear()

        updateCamera(flyingCamera, window, deltaTime, viewPosition)

        modelPositions.forEach { position ->
            drawBox(model, position, modelData, modelUniformPosition, shaderProgram, vao)
        }
    }
}

private fun getViewAndModelUniformPositions(shaderProgram: Int): Pair<Int, Int> {
    GL33.glUseProgram(shaderProgram)
    val viewPosition = GL33.glGetUniformLocation(shaderProgram, "view")
    val modelUniformPosition = GL33.glGetUniformLocation(shaderProgram, "model")
    return Pair(viewPosition, modelUniformPosition)
}

private fun initCamera(window: Long): FlyingCamera {
    val flyingCamera = FlyingCamera()
    mouseMoveCallback(window, flyingCamera::processNewMousePosition)
    return flyingCamera
}

private fun setPerspectiveProjection(shaderProgram: Int) {
    val projectionData = FloatArray(16)
    Matrix4f()
        .perspective(45.0f, DEFAULT_WINDOW_WIDTH.toFloat() / DEFAULT_WINDOW_HEIGHT, 0.1f, 100f)
        .get(projectionData)
    GL33.glUniformMatrix4fv(GL33.glGetUniformLocation(shaderProgram, "projection"), false, projectionData)
}

private fun createModelPositions() = listOf(
    Vector3f(0.0f, 0.0f, 3.0f),
    Vector3f(2.0f, 5.0f, -15.0f),
    Vector3f(-1.5f, -2.2f, -2.5f),
    Vector3f(-3.8f, -2.0f, -12.3f),
    Vector3f(2.4f, -0.4f, -3.5f),
    Vector3f(-1.7f, 3.0f, -7.5f),
    Vector3f(1.3f, -2.0f, -2.5f),
    Vector3f(1.5f, 2.0f, -2.5f),
    Vector3f(1.5f, 0.2f, -1.5f),
    Vector3f(-1.3f, 1.0f, -1.5f)
)

private fun updateCamera(
    flyingCamera: FlyingCamera,
    window: Long,
    deltaTime: Float,
    viewPosition: Int
) {
    flyingCamera.tick(window, deltaTime)
    GL33.glUniformMatrix4fv(viewPosition, false, flyingCamera.lookAt())
}

private fun drawBox(
    model: Matrix4f,
    position: Vector3f,
    modelData: FloatArray,
    modelPosition: Int,
    shaderProgram: Int,
    vao: Int
) {
    model.translation(position)
        .get(modelData)

    GL33.glUniformMatrix4fv(modelPosition, false, modelData)

    GL33.glUseProgram(shaderProgram)
    GL33.glBindVertexArray(vao)
    GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, 36)
}

private fun createAndBindBoxMesh(vertices: FloatArray): Int {
    val vao = GL33.glGenVertexArrays()
    val vbo = GL33.glGenBuffers()

    GL33.glBindVertexArray(vao)
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vbo)
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW)
    GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 0)
    GL33.glVertexAttribPointer(1, 2, GL33.GL_FLOAT, false, 5 * Float.SIZE_BYTES, 3L * Float.SIZE_BYTES)
    GL33.glEnableVertexAttribArray(0)
    GL33.glEnableVertexAttribArray(1)
    return vao
}

private fun createAndBindGrassTexture(shaderProgram: Int) {
    GL33.glUseProgram(shaderProgram)
    val texture = createTexture("/textures/grass.png", type = GL33.GL_RGBA, filter = GL33.GL_NEAREST)
    setUniform(shaderProgram, "texture0", 0)
    GL33.glActiveTexture(GL33.GL_TEXTURE0)
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture)
}