package org.intelligentfarming.gui

import org.intelligentfarming.render.RenderableBase
import org.intelligentfarming.render.Renderer

abstract class GuiBase(window: Window) : RenderableBase(window) {
    abstract val renderer: Renderer

    abstract val left: Float
    abstract val right: Float
    abstract val top: Float
    abstract val bottom: Float

    override fun render() {
        renderer.render(left, top, right, bottom)
    }

    override fun cleanup() {
        renderer.cleanup()
    }
}