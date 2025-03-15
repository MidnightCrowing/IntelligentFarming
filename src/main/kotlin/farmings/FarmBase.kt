package com.midnightcrowing.farmings

import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import com.midnightcrowing.model.ScreenBounds

open class FarmBase(window: Window) : Widget(window) {
    fun render(bounds: FarmBounds) {
        if (!bounds.isAvailable) {
            return
        }

        screenBounds = ScreenBounds(bounds.screenLeft, bounds.screenTop, bounds.screenRight, bounds.screenBottom)
        super.render()
    }
}