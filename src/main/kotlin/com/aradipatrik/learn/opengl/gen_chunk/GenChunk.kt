package com.aradipatrik.learn.opengl.gen_chunk

import com.aradipatrik.learn.opengl.*
import com.aradipatrik.learn.opengl.models.Meshes
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL33
import kotlin.math.roundToInt
import kotlin.random.Random

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

    var modelPositions = createModelPositions(0.0f)

    val (viewPosition, modelUniformPosition) = getViewAndModelUniformPositions(shaderProgram)

    val flyingCamera = initCamera(window)

    var offset = 0.0f

    val modelData = FloatArray(16)
    val model = Matrix4f()
    loop(window) { deltaTime ->
        clear()

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            offset += deltaTime
            modelPositions = createModelPositions(offset)
        }

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

private fun createModelPositions(offset: Float): List<Vector3f> {
    val lattice = createNoiseLattice()
    return buildList {
        repeat(24) { y ->
            repeat(24) { x ->
                val height = (lattice.noiseValueOf(
                    Vector2f(
                        x.toFloat() / 18 + offset,
                        y.toFloat() / 18 + offset
                    )
                ) * 12).roundToInt()
                repeat(height) { currentHeight ->
                    add(
                        Vector3f(
                            x.toFloat(),
                            currentHeight.toFloat(),
                            y.toFloat(),
                        )
                    )
                }

            }
        }
    }
}

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