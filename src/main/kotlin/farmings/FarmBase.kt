package com.midnightcrowing.farmings

import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window

open class FarmBase : Widget {
    constructor(window: Window) : super(window)

    constructor(parent: Widget) : super(parent)
}