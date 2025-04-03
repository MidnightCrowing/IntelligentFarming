package com.midnightcrowing.gui.bases

import com.midnightcrowing.config.AppConfig
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.utils.FPSCounter
import com.midnightcrowing.utils.GameTick
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL2.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryUtil
import java.io.File
import java.io.IOException
import javax.swing.JOptionPane

/**
 * 窗口管理类，负责初始化和管理 GLFW 窗口。
 */
class Window(
    var name: String,  // 英文名称
    var title: String, // 中文名称
    var width: Int,
    var height: Int,
    var minWidth: Int = 0,
    var minHeight: Int = 0,
) {
    companion object {
        /**
         * 使用应用配置创建默认窗口
         */
        fun createWindow() = Window(
            AppConfig.APP_NAME,
            AppConfig.APP_NAME_CN,
            AppConfig.WINDOW_WIDTH,
            AppConfig.WINDOW_HEIGHT,
            AppConfig.WINDOW_MIN_WIDTH,
            AppConfig.WINDOW_MIN_HEIGHT
        )
    }

    // 窗口句柄
    var handle: Long = 0L
        private set

    // 全局事件管理器
    val eventManager: EventManager = EventManager(this)

    val nvg: Long by lazy { nvgCreate(NVG_ANTIALIAS or NVG_STENCIL_STROKES) }

    var screen: Screen = Screen(this)
        set(value) {
            val oldScreen = field
            oldScreen.cleanup()
            field = value
            field.place(width, height)
        }

    init {
        createGLFW()
        createOpenGL()
        initGLFW()
        initNanoVG()
        initEventManager()
        createFont()
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

    private fun createOpenGL() {
        // 创建 OpenGL 的能力集，通常用于初始化 OpenGL 的各类扩展功能
        GL.createCapabilities()
    }

    private fun initEventManager() {
        // 初始化事件管理器
        eventManager.initGLFWCallback()
        eventManager.initListener()

        val (windowWidth, windowHeight) = getWindowSize()
        eventManager.triggerWindowResizeEvent(windowWidth, windowHeight)
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

    private fun initNanoVG() {
        if (nvg == 0L) {
            throw RuntimeException("Failed to create NanoVG context")
        }
    }

    private fun createFont() {
        val fontPath = ResourcesEnum.FONT_DEFAULT.inputStream ?: run {
            JOptionPane.showMessageDialog(null, "字体资源加载失败", "错误", JOptionPane.ERROR_MESSAGE)
            return
        }

        val byteArray = fontPath.readBytes()
        val tempDir = System.getProperty("java.io.tmpdir")
        val fontFile = File(tempDir, "${name}_temp_font.otf")

        if (!fontFile.exists()) {
            try {
                fontFile.writeBytes(byteArray)
            } catch (e: IOException) {
                JOptionPane.showMessageDialog(null, "写入临时文件失败: ${e.message}", "错误", JOptionPane.ERROR_MESSAGE)
                return
            }
        }

        // 转换路径为 UTF-8（确保中文路径不出问题）
        val fontPathUtf8 = fontFile.absolutePath.toByteArray(Charsets.UTF_8).toString(Charsets.UTF_8)

        val fontId = nvgCreateFont(nvg, "unifont", fontPathUtf8)
        if (fontId == -1) {
            JOptionPane.showMessageDialog(null, "字体加载失败 path: $fontPathUtf8", "错误", JOptionPane.ERROR_MESSAGE)
        }
    }

    private val fpsCounter = FPSCounter()
    private val fpsTextRenderer: TextRenderer = TextRenderer(nvg).apply {
        x = 5.0; y = 15.0; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }
    private val tickTextRenderer: TextRenderer = TextRenderer(nvg).apply {
        x = 5.0; y = 40.0; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }
    private val mousePosRenderer: TextRenderer = TextRenderer(nvg).apply {
        x = 5.0; y = 65.0; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }

    fun shouldClose(): Boolean = glfwWindowShouldClose(handle)

    fun update() {
        // 更新游戏tick
        GameTick.update()
        // 更新游戏内容
        screen.update()
        // 更新 FPS 计数器
        fpsCounter.update()
    }

    fun renderBegin() {
        // 开始 NanoVG 渲染
        nvgBeginFrame(nvg, width.toFloat(), height.toFloat(), 1f)
        // 清除颜色和深度缓冲区
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun render() {
        // 渲染窗口内容
        screen.render()
        // 渲染 FPS 和 Tick 信息
        fpsTextRenderer.render("FPS: ${fpsCounter.fps}")
        tickTextRenderer.render("Tick: ${GameTick.tick}")
        val (mousePosX, mousePosY) = getCursorPos()
        mousePosRenderer.render("Mouse: X: $mousePosX, Y: $mousePosY")
    }

    fun renderEnd() {}

    /**
     * 交换帧缓冲区
     */
    fun swapBuffers() = glfwSwapBuffers(handle)

    /**
     * 处理窗口事件
     */
    fun pollEvents() = glfwPollEvents()

    fun handleResize(width: Int, height: Int) {
        this.width = width
        this.height = height

        glViewport(0, 0, width, height)

        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(0.0, width.toDouble(), height.toDouble(), 0.0, -1.0, 1.0)
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()
    }

    /**
     * 获取窗口的大小
     *
     * @return 一个包含窗口宽度和高度的 Pair
     */
    fun getWindowSize(): Pair<Int, Int> {
        val width = IntArray(1)
        val height = IntArray(1)
        glfwGetWindowSize(handle, width, height)
        return Pair(width[0], height[0])
    }

    /**
     * 获取鼠标在窗口中的坐标
     *
     * @return 一个包含鼠标 x 和 y 坐标的 Pair
     */
    fun getCursorPos(): Pair<Double, Double> {
        val xpos = DoubleArray(1)
        val ypos = DoubleArray(1)
        glfwGetCursorPos(handle, xpos, ypos)
        return Pair(xpos[0], ypos[0])
    }

    /**
     * 关闭窗口
     */
    fun exit() = glfwSetWindowShouldClose(handle, true)

    fun cleanup() {
        screen.cleanup()
        nvgDelete(nvg)
        glfwCleanup()
    }

    private fun glfwCleanup() {
        Callbacks.glfwFreeCallbacks(handle)
        glfwDestroyWindow(handle)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }
}