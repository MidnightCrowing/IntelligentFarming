package com.midnightcrowing.gui.bases

import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.texture.TextureManager

open class Screen(window: Window) : Widget(window) {
    open val bgRenderer: TextureRenderer = TextureRenderer()

    open fun place(width: Int, height: Int) {
        super.place(0.0, 0.0, width.toDouble(), height.toDouble())
    }

    override fun render() {
        bgRenderer.render(0.0, 0.0, window.width.toDouble(), window.height.toDouble())
        doRender()
    }

    override fun cleanup() {
        super.cleanup()
        bgRenderer.location?.let { TextureManager.cleanup(it) }
    }
}