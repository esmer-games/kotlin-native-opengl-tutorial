package sample

import glew.*
import glfw.*
import kotlinx.cinterop.*
import platform.opengl32.GL_COLOR_BUFFER_BIT
import platform.opengl32.GL_TRUE
import platform.opengl32.glClear

val g_vertex_buffer_data = floatArrayOf(
    -1.0f, -1.0f, 0.0f,
    1.0f, -1.0f, 0.0f,
    0.0f,  1.0f, 0.0f
)

val fragmentShader = """
#version 330 core
out vec3 color;
void main(){
  color = vec3(1,0,0);
}
"""

val vertexShader = """
#version 330 core
layout(location = 0) in vec3 vertexPosition_modelspace;
void main(){
  gl_Position.xyz = vertexPosition_modelspace;
  gl_Position.w = 1.0;
}
"""

@ExperimentalUnsignedTypes
fun main() {
    glewExperimental = 1u
    if (glfwInit() == 0) {
        error("Failed to initialize GLFW")
    }
    glfwWindowHint(GLFW_SAMPLES, 4)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    val window = glfwCreateWindow(1024, 768, "Tutorial", null, null)
        ?: error("Failed to open GLFW window.")
    glfwMakeContextCurrent(window)
    if (glewInit() != GLEW_OK.toUInt()) {
        error("Failed to initialize GLEW")
    }
    // Create the triangle:
    val vao = memScoped {
        alloc<GLuintVar> {
            glGenVertexArrays!!(1, this.ptr)
        }.value
    }
    glBindVertexArray!!(vao)
    val vbo = memScoped {
        alloc<GLuintVar> {
            glGenBuffers!!(1, this.ptr)
        }.value
    }
    glBindBuffer!!(GL_ARRAY_BUFFER.toUInt(), vbo)
    g_vertex_buffer_data.usePinned {
        glBufferData!!(GL_ARRAY_BUFFER.toUInt(), g_vertex_buffer_data.size.toLong() * 4, it.addressOf(0), GL_STATIC_DRAW.convert())
    }
    // Create the shader
    val shaderId = memScoped {
        // Vertex shader
        val vertexShaderId = glCreateShader!!(GL_VERTEX_SHADER.toUInt())
        glShaderSource!!(vertexShaderId, 1, arrayOf(vertexShader).toCStringArray(this), null)
        glCompileShader!!(vertexShaderId)
        // Fragment shader
        val fragmentShaderId = glCreateShader!!(GL_FRAGMENT_SHADER.toUInt())
        glShaderSource!!(fragmentShaderId, 1, arrayOf(fragmentShader).toCStringArray(this), null)
        glCompileShader!!(fragmentShaderId)
        // The shader
        val shaderId = glCreateProgram!!()
        glAttachShader!!(shaderId, vertexShaderId)
        glAttachShader!!(shaderId, fragmentShaderId)
        glLinkProgram!!(shaderId)
        glDetachShader!!(shaderId, vertexShaderId)
        glDeleteShader!!(vertexShaderId)
        glDetachShader!!(shaderId, fragmentShaderId)
        glDeleteShader!!(fragmentShaderId)
        shaderId
    }
    // Ensure we can capture the escape key being pressed below
    glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE)
    do {
        glClear((GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT).toUInt())
        glUseProgram!!(shaderId)
        // 1st attribute buffer : vertices
        glEnableVertexAttribArray!!(0u)
        glBindBuffer!!(GL_ARRAY_BUFFER.toUInt(), vbo);
        glVertexAttribPointer!!(
            0u,                  // attribute 0. No particular reason for 0, but must match the layout in the shader.
            3,                  // size
            GL_FLOAT.toUInt(),           // type
            GL_FALSE.toUByte(),           // normalized?
            0,                  // stride
            null            // array buffer offset
        );
        // Draw the triangle !
        glDrawArrays(GL_TRIANGLES, 0, 3); // Starting from vertex 0; 3 vertices total -> 1 triangle
        glDisableVertexAttribArray!!(0u);
        // Swap buffers
        glfwSwapBuffers(window)
        glfwPollEvents()

    } // Check if the ESC key was pressed or the window was closed
    while (glfwGetKey(window, GLFW_KEY_ESCAPE ) != GLFW_PRESS && glfwWindowShouldClose(window) == 0)
}
