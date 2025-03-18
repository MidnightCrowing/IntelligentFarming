package com.midnightcrowing.farmings

import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum

class Wheat(parent: Widget) : FarmBase(parent) {
    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.WHEAT_GROW_0.inputStream)

    init {
        renderer.setAlpha(0.8f)
    }
}