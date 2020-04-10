package sample

import glew.*
import glfw.*
import platform.opengl32.GL_COLOR_BUFFER_BIT
import platform.opengl32.GL_TRUE
import platform.opengl32.glClear
import platform.opengl32.glClearColor

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
    // Ensure we can capture the escape key being pressed below
    glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE)
    do{
        // Clear the screen. It's not mentioned before Tutorial 02, but it can cause flickering, so it's there nonetheless.
        glClear( GL_COLOR_BUFFER_BIT )
        // Draw nothing, see you in tutorial 2 !
        // Swap buffers
        glfwSwapBuffers(window)
        glfwPollEvents()

    } // Check if the ESC key was pressed or the window was closed
    while( glfwGetKey(window, GLFW_KEY_ESCAPE ) != GLFW_PRESS && glfwWindowShouldClose(window) == 0 )
}
