package com.midnightcrowing.gui

import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.components.base.Screen
import com.midnightcrowing.render.NanoVGContext
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil

/**
 * 窗口管理类，负责初始化和管理 GLFW 窗口。
 */
class Window(title: String, width: Int, height: Int, minWidth: Int = 0, minHeight: Int = 0) {
    // 窗口句柄
    var handle: Long = 0
        private set

    // 窗口宽度
    var width: Int = width
        private set

    // 窗口高度
    var height: Int = height
        private set

    // 全局事件管理器
    val eventManager: EventManager

    private var screen: Screen = Screen(this)

    init {
        // 设置错误回调
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "无法初始化 GLFW" }

        // 配置 GLFW 并创建窗口
        glfwDefaultWindowHints()
        handle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) throw RuntimeException("创建 GLFW 窗口失败")

        // 设置当前线程的 OpenGL 上下文为指定的窗口句柄
        glfwMakeContextCurrent(handle)

        // 设置交换缓冲区的垂直同步为开启（1表示开启垂直同步，0表示关闭）
        glfwSwapInterval(1)

        // 显示窗口（如果窗口之前被隐藏）
        glfwShowWindow(handle)

        // 创建 OpenGL 的能力集，通常用于初始化 OpenGL 的各类扩展功能
        GL.createCapabilities()

        // 设置窗口大小回调
        glfwSetFramebufferSizeCallback(handle) { _, newWidth, newHeight ->
            this.width = newWidth
            this.height = newHeight
            GL11.glViewport(0, 0, newWidth, newHeight)
        }

        // 设置窗口固定比例
        glfwSetWindowAspectRatio(handle, width, height)

        // 设置最小窗口大小
        glfwSetWindowSizeLimits(handle, minWidth, minHeight, GLFW_DONT_CARE, GLFW_DONT_CARE)

        // 设置事件管理器
        eventManager = EventManager(this)

        // Enable texture mapping
        GL11.glEnable(GL11.GL_TEXTURE_2D)
    }

    fun shouldClose(): Boolean = glfwWindowShouldClose(handle)

    /**
     * 交换帧缓冲区
     */
    fun swapBuffers() = glfwSwapBuffers(handle)

    /**
     * 处理窗口事件
     */
    fun pollEvents() = glfwPollEvents()

    fun setScreen(newScreen: Screen) {
        val oldScreen = screen
        screen = newScreen
        oldScreen.cleanup()
    }

    fun loop() {
        while (!shouldClose()) {
            // 清除缓冲区
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
            // 开始 NanoVG 绘制
            NanoVGContext.beginFrame(this)
            // 渲染内容
            screen.render()
            // 结束 NanoVG 绘制
            NanoVGContext.endFrame()
            // 交换帧缓冲区
            swapBuffers()
            // 处理窗口事件
            pollEvents()
        }
    }

    /**
     * 关闭窗口
     */
    fun exit() {
        glfwSetWindowShouldClose(handle, true)
    }

    fun cleanup() {
        // Disable texture mapping
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        NanoVGContext.cleanup()
        screen.cleanup()

        // Disable texture mapping
        GL11.glDisable(GL11.GL_TEXTURE_2D)

        Callbacks.glfwFreeCallbacks(handle)
        glfwDestroyWindow(handle)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }
}