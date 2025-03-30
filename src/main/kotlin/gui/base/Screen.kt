package com.midnightcrowing.gui.base

import com.midnightcrowing.render.TextureRenderer

open class Screen(val window: Window) {
    open val bgRenderer: TextureRenderer = TextureRenderer()

    open fun place(width: Int, height: Int) {}

    open fun update() {}

    open fun render() {
        bgRenderer.render(0.0, 0.0, window.width.toDouble(), window.height.toDouble())
        doRender()
    }

    protected open fun doRender() {}

    open fun cleanup() {}
}