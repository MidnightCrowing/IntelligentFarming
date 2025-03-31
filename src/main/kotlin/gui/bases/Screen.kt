package com.midnightcrowing.gui.bases

import com.midnightcrowing.renderer.TextureRenderer

open class Screen(window: Window) : Widget(window) {
    open val bgRenderer: TextureRenderer = TextureRenderer()

    open fun place(width: Int, height: Int) {}

    open fun update() {}

    override fun render() {
        bgRenderer.render(0.0, 0.0, window.width.toDouble(), window.height.toDouble())
        doRender()
    }
}