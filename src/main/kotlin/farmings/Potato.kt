package com.midnightcrowing.farmings

import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum

class Potato(parent: Widget) : FarmBase(parent) {
    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.POTATO_GROW_7.inputStream)
}