package com.midnightcrowing.gui.base

import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event.WindowResizeEvent
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer

open class Widget {
    val window: Window?
    val parent: Widget?

    open val renderer: ImageRenderer = ImageRenderer()
    val z: Int
    var widgetBounds: ScreenBounds = ScreenBounds(0f, 0f, 0f, 0f)
        private set
    var visible: Boolean = true
        private set

    constructor(parent: Widget) {
        this.parent = parent
        this.window = parent.window
        this.z = parent.z + 1

        registerListeners()  // 注册监听器
    }

    constructor(window: Window) {
        this.window = window
        this.parent = null
        this.z = 0

        registerListeners()  // 注册监听器
    }

    /**
     * 注册事件监听器
     */
    private fun registerListeners() {
        if (window == null) {
            return
        }
        window.eventManager.registerWidget(WindowResizeEvent::class.java, this)
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
        if (window == null) {
            return
        }
        window.eventManager.unregisterWidget(WindowResizeEvent::class.java, this)
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
        return x in widgetBounds.x1..widgetBounds.x2 && y in widgetBounds.y1..widgetBounds.y2
    }

    open fun place(x1: Float, y1: Float, x2: Float, y2: Float) {
        widgetBounds = ScreenBounds(x1, y1, x2, y2)
    }

    open fun place(bounds: ScreenBounds) {
        widgetBounds = bounds
    }

    /**
     * 渲染组件
     */
    open fun render() {
        if (visible) {
            renderer.render(widgetBounds)
        }
    }

    /**
     * 清理资源
     */
    open fun cleanup() {
        renderer.cleanup()
        unregisterListener()
    }

    open fun onWindowResize(e: WindowResizeEvent) {}

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
