package com.midnightcrowing.gui.components.base

import com.midnightcrowing.events.listeners.ClickEvent
import com.midnightcrowing.gui.Window
import com.midnightcrowing.render.Renderer
import com.midnightcrowing.render.Texture


abstract class Widget(window: Window) : AbstractWidget(window) {
    abstract val renderer: Renderer

    open val left: Float = 0f
    open val right: Float = 0f
    open val top: Float = 0f
    open val bottom: Float = 0f

    init {
        // 注册监听器
        window.eventManager.registerWidget(ClickEvent::class.java, this)
    }

    fun getRenderer(filePath: String): Renderer {
        return Renderer(Texture(filePath).apply { load() })
    }

    override fun render() = renderer.render(left, top, right, bottom)

    override fun cleanup() = renderer.cleanup()

    open fun onClick(e: ClickEvent) {}
}
