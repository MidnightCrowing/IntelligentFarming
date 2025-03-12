package com.midnightcrowing.gui.components.base

import com.midnightcrowing.gui.Window


abstract class AbstractWidget(val window: Window) {
    abstract fun render()

    abstract fun cleanup()
}