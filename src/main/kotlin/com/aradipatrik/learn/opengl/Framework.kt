package com.aradipatrik.learn.opengl

import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL33
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

const val DEFAULT_WINDOW_WIDTH = 1200
const val DEFAULT_WINDOW_HEIGHT = 800
const val VERTEX_ATTRIBUTE_POSITION = 0
const val VERTEX_ATTRIBUTE_COLOR = 1

data class Image(
    val data: ByteBuffer,
    val width: Int,
    val height: Int,
    val numberOfChannels: Int
)

fun clear() {
    GL33.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
    GL33.glClear(GL33.GL_COLOR_BUFFER_BIT)
}

fun createProgram(vertexShaderSourceCode: String, fragmentShaderSourceCode: String): Int {
    val vertexShader = createShader(GL33.GL_VERTEX_SHADER, vertexShaderSourceCode)
    val fragmentShader = createShader(GL33.GL_FRAGMENT_SHADER, fragmentShaderSourceCode)
    val program = createAndLinkProgram(vertexShader, fragmentShader)
    deleteShaders(vertexShader, fragmentShader)
    return program
}

fun createVertexArrayObject(vertices: FloatArray, elements: IntArray? = null): Int {
    val vertexArrayObject = GL33.glGenVertexArrays()
    GL33.glBindVertexArray(vertexArrayObject)
    createVertexBuffer(vertices)
    elements?.let(::createElementBuffer)
    return vertexArrayObject
}

private fun createElementBuffer(elements: IntArray) {
    val elementBuffer = GL33.glGenBuffers()
    GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, elementBuffer)
    GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, elements, GL33.GL_STATIC_DRAW)
}

private fun createVertexBuffer(vertices: FloatArray) {
    val vertexBuffer = GL33.glGenBuffers()
    GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vertexBuffer)
    GL33.glBufferData(GL33.GL_ARRAY_BUFFER, vertices, GL33.GL_STATIC_DRAW)
    GL33.glVertexAttribPointer(VERTEX_ATTRIBUTE_POSITION, 3, GL33.GL_FLOAT, false, 3 * Float.SIZE_BYTES, 0)
    GL33.glEnableVertexAttribArray(VERTEX_ATTRIBUTE_POSITION)
}

fun createAndLinkProgram(vararg shaders: Int): Int {
    val program = GL33.glCreateProgram()
    for (shader in shaders) {
        GL33.glAttachShader(program, shader)
    }
    GL33.glLinkProgram(program)
    checkLink(program)
    return program
}

fun deleteShaders(vararg shaders: Int) {
    for (shader in shaders) {
        GL33.glDeleteShader(shader)
    }
}

fun createShader(shaderType: Int, source: String): Int {
    val shader = GL33.glCreateShader(shaderType)
    GL33.glShaderSource(shader, source)
    GL33.glCompileShader(shader)
    checkShaderCompilation(shader)
    return shader
}

fun checkShaderCompilation(shader: Int) {
    val success = IntArray(1)
    GL33.glGetShaderiv(shader, GL33.GL_COMPILE_STATUS, success)
    if (success.first().isFalse()) {
        error("Compilation of shader failed: " + GL33.glGetShaderInfoLog(shader))
    }
}

fun checkLink(program: Int) {
    val success = IntArray(1)
    GL33.glGetProgramiv(program, GL33.GL_LINK_STATUS, success)
    if (success.first().isFalse()) {
        error("Link failed: " + GL33.glGetProgramInfoLog(program))
    }
}

fun setUniform(program: Int, name: String, value: Int) {
    GL33.glUniform1i(GL33.glGetUniformLocation(program, name), value)
}

fun setUniform(program: Int, name: String, value: Float) {
    GL33.glUniform1f(GL33.glGetUniformLocation(program, name), value)
}

fun processInput(window: Long) {
    if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
        GLFW.glfwSetWindowShouldClose(window, true)
    }
}

fun initWindowWithOpenGlContext(
    windowWidth: Int = DEFAULT_WINDOW_WIDTH,
    windowHeight: Int = DEFAULT_WINDOW_HEIGHT
): Long {
    GLFW.glfwInit()
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
    GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE)
    GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE)

    val window = GLFW.glfwCreateWindow(windowWidth, windowHeight, "LearnOpenGL", 0, 0)
    GLFW.glfwSetFramebufferSizeCallback(window) { _, width, height -> GL33.glViewport(0, 0, width, height) }
    GLFW.glfwMakeContextCurrent(window)
    GL.createCapabilities()

    val width = IntArray(1)
    val height = IntArray(1)
    GLFW.glfwGetFramebufferSize(window, width, height)
    GL33.glViewport(0, 0, width.first(), height.first())
    return window
}

fun readResourceAsText(path: String): String =
    object {}.javaClass.getResource(path)?.readText() ?: error("File not found $path")

fun getResource(path: String) = object {}.javaClass.getResource(path)!!

fun Int.isFalse() = this == 0

fun wireframeEnabled(enabled: Boolean) {
    if (enabled) {
        GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE)
    } else {
        GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_FILL)
    }
}

fun loadImage(path: String): Image {
    val width = IntArray(1)
    val height = IntArray(1)
    val channels = IntArray(1)

    val buffer = STBImage.stbi_load(getResource(path).path, width, height, channels, 0)
    return Image(buffer!!, width.first(), height.first(), channels.first())
}

fun loop(window: Long, render: () -> Unit) {
    while (!GLFW.glfwWindowShouldClose(window)) {
        processInput(window)

        render()

        GLFW.glfwSwapBuffers(window)
        GLFW.glfwPollEvents()
    }

    GLFW.glfwTerminate()
}

fun createTexture(
    path: String,
    type: Int,
    textureWrap: Int = -1,
    filter: Int = -1,
): Int {

    STBImage.stbi_set_flip_vertically_on_load(true)
    val image = loadImage(path)
    val texture = GL33.glGenTextures()
    GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture)
    if (textureWrap != -1) {
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_T, textureWrap)
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_WRAP_S, textureWrap)
    }
    if (filter != -1) {
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, filter)
        GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, filter)
    }
    GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGB, image.width, image.height, 0, type, GL33.GL_UNSIGNED_BYTE, image.data)
    GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D)
    STBImage.stbi_image_free(image.data)
    return texture
}