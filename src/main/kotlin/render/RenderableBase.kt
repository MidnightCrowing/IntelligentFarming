package org.intelligentfarming.render

import org.intelligentfarming.gui.Window


abstract class RenderableBase(val window: Window) {
    open val scaleX: Float get() = window.width.toFloat() / 2
    open val scaleY: Float get() = window.height.toFloat() / 2

    abstract fun render()

    abstract fun cleanup()
}
