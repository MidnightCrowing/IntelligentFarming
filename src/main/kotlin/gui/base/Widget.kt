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
    var widgetBounds: ScreenBounds = ScreenBounds.EMPTY
        private set
    var visible: Boolean = true
        private set
    val parentVisible: Boolean get() = parent?.visible != false && parent?.parentVisible != false
    val isVisible: Boolean get() = visible && parentVisible

    constructor(window: Window, z: Int? = null) {
        this.window = window
        this.parent = null
        this.z = z ?: 0

        registerListeners()  // 注册监听器
    }

    constructor(parent: Widget, z: Int? = null) {
        this.window = parent.window
        this.parent = parent
        this.z = z ?: (parent.z + 1)

        registerListeners()  // 注册监听器
    }

    /**
     * 注册事件监听器
     */
    private fun registerListeners() {
        if (window == null) {
            return
        }
        window.eventManager.registerWidget(WindowResizeEvent::class, this)
        window.eventManager.registerWidget(MouseClickEvent::class, this)
        window.eventManager.registerWidget(MouseRightClickEvent::class, this)
        window.eventManager.registerWidget(MouseEnterEvent::class, this)
        window.eventManager.registerWidget(MouseLeaveEvent::class, this)
        window.eventManager.registerWidget(MouseMoveEvent::class, this)
        window.eventManager.registerWidget(MousePressedEvent::class, this)
        window.eventManager.registerWidget(MouseReleasedEvent::class, this)
        window.eventManager.registerWidget(KeyPressedEvent::class, this)
    }

    /**
     * 取消注册事件监听器
     */
    private fun unregisterListener() {
        if (window == null) {
            return
        }
        window.eventManager.unregisterWidget(WindowResizeEvent::class, this)
        window.eventManager.unregisterWidget(MouseClickEvent::class, this)
        window.eventManager.unregisterWidget(MouseRightClickEvent::class, this)
        window.eventManager.unregisterWidget(MouseEnterEvent::class, this)
        window.eventManager.unregisterWidget(MouseLeaveEvent::class, this)
        window.eventManager.unregisterWidget(MouseMoveEvent::class, this)
        window.eventManager.unregisterWidget(MousePressedEvent::class, this)
        window.eventManager.unregisterWidget(MouseReleasedEvent::class, this)
        window.eventManager.unregisterWidget(KeyPressedEvent::class, this)
    }

    /**
     * 判断给定坐标是否在组件范围内
     */
    fun containsPoint(x: Double, y: Double): Boolean {
        return x in widgetBounds.x1..widgetBounds.x2 && y in widgetBounds.y1..widgetBounds.y2
    }

    fun setHidden(value: Boolean) {
        visible = !value
    }

    fun setVisible(value: Boolean) {
        visible = value
    }

    fun toggleVisible() {
        visible = !visible
    }

    open fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        widgetBounds = ScreenBounds(x1, y1, x2, y2)
    }

    open fun place(bounds: ScreenBounds) {
        widgetBounds = bounds
    }

    /**
     * 渲染组件
     */
    open fun render() {
        if (isVisible) {
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
     * 鼠标右键点击事件
     */
    open fun onRightClick(e: MouseRightClickEvent) {}

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

    open fun onKeyPress(e: KeyPressedEvent) {}
}
