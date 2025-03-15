package com.midnightcrowing.gui.components.base

import com.midnightcrowing.gui.Window

open class Screen(window: Window) : AbstractWidget(window) {
    override fun render() {}

    override fun cleanup() {}
}