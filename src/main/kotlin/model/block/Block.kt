package com.midnightcrowing.model.block

import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.TextureRenderer

open class Block(val parent: Widget) {
    protected open val renderer: TextureRenderer = TextureRenderer()
    private var visible: Boolean = true
    var bounds: ScreenBounds = ScreenBounds.EMPTY
        private set

    val isVisible: Boolean get() = visible && parent.isVisible

    open fun update() {}

    open fun setHidden(value: Boolean) {
        visible = !value
    }

    open fun place(bounds: ScreenBounds) {
        this.bounds = bounds
    }

    /**
     * 渲染组件
     */
    open fun render() {
        if (!isVisible) return
        renderer.render(bounds)
    }

    /**
     * 清理资源
     */
    fun cleanup() {
        renderer.cleanup()
    }
}