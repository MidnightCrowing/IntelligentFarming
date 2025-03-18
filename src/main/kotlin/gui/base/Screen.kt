package com.midnightcrowing.gui.base

import com.midnightcrowing.render.ImageRenderer

open class Screen(val window: Window) {
    open val bgRenderer: ImageRenderer = ImageRenderer()

    open fun place() {}

    open fun render() {
        bgRenderer.render(0f, 0f, window.width.toFloat(), window.height.toFloat())
    }

    open fun cleanup() {}
}