package com.midnightcrowing.farmings

import com.midnightcrowing.gui.Window
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum

class Potato(window: Window) : FarmBase(window) {
    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.POTATO_GROW_7.inputStream)
}