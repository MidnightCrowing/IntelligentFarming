package com.midnightcrowing.farmings

import com.midnightcrowing.gui.Window
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum

class Carrot(window: Window) : FarmBase(window) {
    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.CARROT_GROW_7.inputStream)
}