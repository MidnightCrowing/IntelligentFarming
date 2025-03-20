package com.midnightcrowing.gui.base

import com.midnightcrowing.events.Event.WindowResizeEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.render.NanoVGContext
import com.midnightcrowing.render.TextRenderer
import com.midnightcrowing.utils.FPSCounter
import com.midnightcrowing.utils.GameTick
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryUtil

/**
 * 窗口管理类，负责初始化和管理 GLFW 窗口。
 */
class Window(
    var title: String, var width: Int, var height: Int, var minWidth: Int = 0, var minHeight: Int = 0,
) {
    // 窗口句柄
    var handle: Long = 0L
        private set

    // 全局事件管理器
    val eventManager: EventManager

    private val fpsCounter = FPSCounter()

    var screen: Screen = Screen(this)
        set(value) {
            val oldScreen = field
            field = value
            field.place()
            oldScreen.cleanup()
        }

    init {
        createGLFW()

        // 创建 OpenGL 的能力集，通常用于初始化 OpenGL 的各类扩展功能
        GL.createCapabilities()

        eventManager = EventManager(this) // 需要在initGLFW之前设置

        initGLFW()

        // Enable texture mapping
        glEnable(GL_TEXTURE_2D)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

    }

    private fun createGLFW() {
        // 设置错误回调
        GLFWErrorCallback.createPrint(System.err).set()
        check(glfwInit()) { "无法初始化 GLFW" }

        // 配置 GLFW 并创建窗口
        glfwDefaultWindowHints()
        handle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) throw RuntimeException("创建 GLFW 窗口失败")

        // 设置当前线程的 OpenGL 上下文为指定的窗口句柄
        glfwMakeContextCurrent(handle)
    }

    private fun initGLFW() {
        // 设置交换缓冲区的垂直同步为开启（1表示开启垂直同步，0表示关闭）
        glfwSwapInterval(1)

        // 显示窗口（如果窗口之前被隐藏）
        glfwShowWindow(handle)

        // 设置窗口固定比例
        glfwSetWindowAspectRatio(handle, width, height)

        // 设置最小窗口大小
        glfwSetWindowSizeLimits(handle, minWidth, minHeight, GLFW_DONT_CARE, GLFW_DONT_CARE)
    }

    fun onWindowResize(e: WindowResizeEvent) {
        width = e.width
        height = e.height

        glViewport(0, 0, width, height)

        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(0.0, width.toDouble(), height.toDouble(), 0.0, -1.0, 1.0)
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()
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

    val fpsTextRenderer: TextRenderer = TextRenderer(NanoVGContext.vg).apply {
        x = 5f; y = 15f; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }
    val tickTextRenderer: TextRenderer = TextRenderer(NanoVGContext.vg).apply {
        x = 5f; y = 40f; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }

    fun loop() {
        while (!shouldClose()) {
            // 清除缓冲区
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            // 开始 NanoVG 绘制
            NanoVGContext.beginFrame(this)

            GameTick.update()

            // 更新游戏内容
            screen.update()

            // 渲染内容
            screen.render()

            fpsCounter.update()
            fpsTextRenderer.drawText("FPS: ${fpsCounter.fps}")
            tickTextRenderer.drawText("Tick: ${GameTick.tick}")

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
        glDisable(GL_TEXTURE_2D)
        NanoVGContext.cleanup()
        screen.cleanup()

        // Disable texture mapping
        glDisable(GL_TEXTURE_2D)

        Callbacks.glfwFreeCallbacks(handle)
        glfwDestroyWindow(handle)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }
}