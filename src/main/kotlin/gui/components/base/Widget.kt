package com.midnightcrowing.gui.components.base

import com.midnightcrowing.events.listeners.ClickEvent
import com.midnightcrowing.events.listeners.MouseEnterEvent
import com.midnightcrowing.events.listeners.MouseLeaveEvent
import com.midnightcrowing.events.listeners.MouseMoveEvent
import com.midnightcrowing.gui.Window
import com.midnightcrowing.render.Renderer
import com.midnightcrowing.utils.CoordinateConversionUtils.convertScreenToNdcBounds


abstract class Widget(window: Window) : AbstractWidget(window) {
    abstract val renderer: Renderer

    open val screenLeft: Float = 0f
    open val screenRight: Float = 0f
    open val screenTop: Float = 0f
    open val screenBottom: Float = 0f

    init {
        // 注册监听器
        registerListeners()
    }

    /**
     * 注册事件监听器
     */
    private fun registerListeners() {
        window.eventManager.registerWidget(ClickEvent::class.java, this)
        window.eventManager.registerWidget(MouseEnterEvent::class.java, this)
        window.eventManager.registerWidget(MouseLeaveEvent::class.java, this)
        window.eventManager.registerWidget(MouseMoveEvent::class.java, this)
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
        renderer.render(
            convertScreenToNdcBounds(
                window, screenLeft, screenTop, screenRight, screenBottom
            )
        )
    }

    /**
     * 清理资源
     */
    override fun cleanup() = renderer.cleanup()

    /**
     * 鼠标点击事件
     */
    open fun onClick(e: ClickEvent) {}

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
