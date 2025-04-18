package com.midnightcrowing.gui.bases

import com.midnightcrowing.config.AppConfig
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.publics.Debugger
import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.resource.ResourceType
import com.midnightcrowing.texture.TextureErrorTracker
import com.midnightcrowing.texture.TextureManager
import com.midnightcrowing.utils.GameTick
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.nanovg.NanoVG.nvgBeginFrame
import org.lwjgl.nanovg.NanoVG.nvgCreateFont
import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL46.*
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.io.File
import java.io.IOException
import javax.swing.JOptionPane

/**
 * 窗口管理类，负责初始化和管理 GLFW 窗口。
 */
class Window(
    var name: String,  // 游戏名称（英文）
    var title: String, // 窗口标题
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
            "${AppConfig.APP_NAME_CN}* ${AppConfig.VERSION}",
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

        // 设置窗口图标
        setWindowIcon()

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
        // 设置垂直同步
        setSwapInterval(AppConfig.SWAP_INTERVAL)

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

    private fun setWindowIcon() {
        val iconPath = ResourceLocation("minecraft", "farmland.png").toAssetPath()
        val inputStream = this::class.java.getResourceAsStream(iconPath)

        if (inputStream == null) {
            JOptionPane.showMessageDialog(null, "图标资源加载失败", "错误", JOptionPane.ERROR_MESSAGE)
            return
        }

        MemoryStack.stackPush().use { stack ->
            val width = stack.mallocInt(1)
            val height = stack.mallocInt(1)
            val channels = stack.mallocInt(1)

            // 读取输入流中的图标数据
            val iconData = inputStream.readBytes()
            val buffer = BufferUtils.createByteBuffer(iconData.size).apply {
                put(iconData)
                flip()
            }

            // 从内存中加载图标图像
            val icon = stbi_load_from_memory(buffer, width, height, channels, 4)
            if (icon == null) {
                JOptionPane.showMessageDialog(
                    null,
                    "Failed to load icon image from InputStream\nSTBImage error: ${stbi_failure_reason()}",
                    "错误",
                    JOptionPane.ERROR_MESSAGE
                )
                return
            }

            // 创建 GLFW 图标
            val iconBuffer = GLFWImage.malloc(1)
            iconBuffer.width(width.get())
            iconBuffer.height(height.get())
            iconBuffer.pixels(icon)

            // 设置窗口图标
            glfwSetWindowIcon(handle, iconBuffer)

            // 释放图标图像内存
            stbi_image_free(icon)
        }
    }

    private fun createFont() {
        val fontPath = ResourceLocation(
            ResourceType.FONT,
            "minecraft",
            "unifont-16.0.02.otf"
        ).toAssetPath()
        val fontStream = this::class.java.getResourceAsStream(fontPath) ?: run {
            JOptionPane.showMessageDialog(null, "字体资源加载失败", "错误", JOptionPane.ERROR_MESSAGE)
            return
        }

        val byteArray = fontStream.readBytes()

        // 获取临时目录路径
        val tempDirPath = System.getProperty("java.io.tmpdir")
        val tempDir = File(tempDirPath)
        val containsChinese = tempDir.absolutePath.any { it.code > 127 }

        // 如果包含中文，则 fallback 到程序当前路径下的 .IntelligentFarming 目录
        val fallbackDir = if (containsChinese) {
            val altDir = File(".IntelligentFarming")
            if (!altDir.exists()) altDir.mkdirs()
            altDir
        } else {
            tempDir
        }

        val fontFile = File(fallbackDir, "${name}_temp_font.otf")
        if (!fontFile.exists()) {
            try {
                fontFile.writeBytes(byteArray)
            } catch (e: IOException) {
                JOptionPane.showMessageDialog(null, "写入字体文件失败: ${e.message}", "错误", JOptionPane.ERROR_MESSAGE)
                return
            }
        }

        // 使用绝对路径加载字体（不需要 UTF-8 编码转换）
        val fontId = nvgCreateFont(nvg, "unifont", fontFile.absolutePath)
        if (fontId == -1) {
            JOptionPane.showMessageDialog(
                null,
                "字体加载失败 path: ${fontFile.absolutePath}",
                "错误",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }

    // 调试器, 用于显示 FPS 和 Tick 等信息
    private val debugger = Debugger(this)

    fun shouldClose(): Boolean = glfwWindowShouldClose(handle)

    fun update() {
        // 更新游戏tick
        GameTick.update()
        // 更新游戏内容
        screen.update()
        // 更新调试器
        debugger.update()
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
        // 渲染调试器
        debugger.render()
    }

    fun renderEnd() {
        if (TextureErrorTracker.checkMaxErrors()) {
            System.err.println("FATAL: Too many leaked textures, exiting.")
            throw RuntimeException("Too many Texture leaks.")
        }
    }

    /**
     * 交换帧缓冲区
     */
    fun swapBuffers() = glfwSwapBuffers(handle)

    /**
     * 处理窗口事件
     */
    fun pollEvents() = glfwPollEvents()

    /**
     * 处理窗口大小变化事件
     *
     * @param width 新的窗口宽度
     * @param height 新的窗口高度
     */
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
     * 处理键盘按下事件
     *
     * @param key 按下的键的 GLFW 键码
     */
    fun handleKeyPress(key: Int) {
        if (key == GLFW_KEY_F3) {
            // 切换调试信息的显示状态
            debugger.toggleVisible()
        } else if (key == GLFW_KEY_T) {
            println("widget tree:")
            screen.tree()
        }
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
     * 设置垂直同步
     *
     * @param enabled 是否启用垂直同步
     */
    fun setSwapInterval(enabled: Boolean) {
        // 设置交换缓冲区的垂直同步为开启（1表示开启垂直同步，0表示关闭）
        glfwSwapInterval(if (enabled) 1 else 0)
    }

    /**
     * 关闭窗口
     */
    fun exit() = glfwSetWindowShouldClose(handle, true)

    fun cleanup() {
        screen.cleanup()
        nvgDelete(nvg)
        TextureManager.cleanupAll()
        glfwCleanup()
    }

    private fun glfwCleanup() {
        Callbacks.glfwFreeCallbacks(handle)
        glfwDestroyWindow(handle)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
    }
}