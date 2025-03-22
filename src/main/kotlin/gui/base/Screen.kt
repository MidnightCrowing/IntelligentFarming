package com.midnightcrowing.gui.base

import com.midnightcrowing.render.ImageRenderer

open class Screen(val window: Window) {
    open val bgRenderer: ImageRenderer = ImageRenderer()

    open fun place() {}

    open fun update() {}

    open fun render() {
        bgRenderer.render(0.0, 0.0, window.width.toDouble(), window.height.toDouble())
    }

    open fun cleanup() {}
}