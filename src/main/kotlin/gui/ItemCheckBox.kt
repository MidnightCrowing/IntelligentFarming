package com.midnightcrowing.gui

import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.scenes.FarmScene

class ItemCheckBox(scene: FarmScene) : Widget(scene.window) {
    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.CHECK_BOX.inputStream)
}