package com.midnightcrowing.gui.components.base

import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.gui.Window
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer


open class Widget(window: Window) : AbstractWidget(window) {
    open val renderer: ImageRenderer = ImageRenderer()

    open var screenBounds: ScreenBounds = ScreenBounds(0f, 0f, 0f, 0f)
    open val screenLeft: Float get() = screenBounds.left
    open val screenRight: Float get() = screenBounds.right
    open val screenTop: Float get() = screenBounds.top
    open val screenBottom: Float get() = screenBounds.bottom

    init {
        // 注册监听器
        registerListeners()
    }

    var visible = true

    /**
     * 注册事件监听器
     */
    private fun registerListeners() {
        window.eventManager.registerWidget(MouseClickEvent::class.java, this)
        window.eventManager.registerWidget(MouseEnterEvent::class.java, this)
        window.eventManager.registerWidget(MouseLeaveEvent::class.java, this)
        window.eventManager.registerWidget(MouseMoveEvent::class.java, this)
        window.eventManager.registerWidget(MousePressedEvent::class.java, this)
        window.eventManager.registerWidget(MouseReleasedEvent::class.java, this)
    }

    /**
     * 取消注册事件监听器
     */
    private fun unregisterListener() {
        window.eventManager.unregisterWidget(MouseClickEvent::class.java, this)
        window.eventManager.unregisterWidget(MouseEnterEvent::class.java, this)
        window.eventManager.unregisterWidget(MouseLeaveEvent::class.java, this)
        window.eventManager.unregisterWidget(MouseMoveEvent::class.java, this)
        window.eventManager.unregisterWidget(MousePressedEvent::class.java, this)
        window.eventManager.unregisterWidget(MouseReleasedEvent::class.java, this)
    }

    /**
     * 判断给定坐标是否在组件范围内
     */
    fun containsPoint(x: Float, y: Float): Boolean {
        return x in screenLeft..screenRight && y in screenTop..screenBottom
    }

    /**
     * 渲染组件
     */
    override fun render() {
        if (visible) {
            renderer.render(
                ScreenBounds(screenLeft, screenTop, screenRight, screenBottom).toNdcBounds(window)
            )
        }
    }

    /**
     * 清理资源
     */
    override fun cleanup() {
        renderer.cleanup()
        unregisterListener()
    }

    /**
     * 鼠标按下事件
     */
    open fun onMousePress(e: MousePressedEvent) {}

    /**
     * 鼠标释放事件
     */
    open fun onMouseRelease(e: MouseReleasedEvent) {}

    /**
     * 鼠标点击事件
     */
    open fun onClick(e: MouseClickEvent) {}

    /**
     * 鼠标移入事件
     */
    open fun onMouseEnter() {}

    /**
     * 鼠标移出事件
     */
    open fun onMouseLeave() {}

    /**
     * 鼠标移动事件
     */
    open fun onMouseMove(e: MouseMoveEvent) {}
}
