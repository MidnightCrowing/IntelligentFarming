package org.intelligentfarming.gui

import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil

/**
 * 窗口管理类，负责初始化和管理 GLFW 窗口。
 */
class Window(title: String, width: Int, height: Int, minWidth: Int = 0, minHeight: Int = 0) {
    var handle: Long = 0
        private set
    var width: Int = width
        private set
    var height: Int = height
        private set

    init {
        // 设置错误回调
        GLFWErrorCallback.createPrint(System.err).set()
        check(GLFW.glfwInit()) { "无法初始化 GLFW" }

        // 配置 GLFW 并创建窗口
        GLFW.glfwDefaultWindowHints()
        handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) throw RuntimeException("创建 GLFW 窗口失败")

        // 设置当前线程的 OpenGL 上下文为指定的窗口句柄
        GLFW.glfwMakeContextCurrent(handle)

        // 设置交换缓冲区的垂直同步为开启（1表示开启垂直同步，0表示关闭）
        GLFW.glfwSwapInterval(1)

        // 显示窗口（如果窗口之前被隐藏）
        GLFW.glfwShowWindow(handle)

        // 创建 OpenGL 的能力集，通常用于初始化 OpenGL 的各类扩展功能
        GL.createCapabilities()

        // 设置窗口大小回调
        GLFW.glfwSetFramebufferSizeCallback(handle) { _, newWidth, newHeight ->
            this.width = newWidth
            this.height = newHeight
            GL11.glViewport(0, 0, newWidth, newHeight)
        }

        // 设置窗口固定比例
        GLFW.glfwSetWindowAspectRatio(handle, width, height)

        // 设置最小窗口大小
        GLFW.glfwSetWindowSizeLimits(handle, minWidth, minHeight, GLFW.GLFW_DONT_CARE, GLFW.GLFW_DONT_CARE)
    }

    fun shouldClose(): Boolean = GLFW.glfwWindowShouldClose(handle)

    /**
     * 交换帧缓冲区
     */
    fun swapBuffers() = GLFW.glfwSwapBuffers(handle)

    /**
     * 处理窗口事件
     */
    fun pollEvents() = GLFW.glfwPollEvents()

    fun cleanup() {
        Callbacks.glfwFreeCallbacks(handle)
        GLFW.glfwDestroyWindow(handle)
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)!!.free()
    }
}