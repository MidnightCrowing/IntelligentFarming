package com.midnightcrowing.gui.components.inventory

import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import com.midnightcrowing.render.Renderer
import com.midnightcrowing.resource.ResourcesEnum

class Inventory(window: Window) : Widget(window) {
    override val renderer: Renderer = getRenderer(ResourcesEnum.INVENTORY.path)

    private companion object {
        const val BASE_WIDTH = 352
        const val BASE_HEIGHT = 198
        const val OFFSET_Y = 0.1f // 向下偏移的距离
        const val SCALE_BASE = 352f
    }

    private val Int.scaled: Float get() = this * (SCALE_BASE / BASE_WIDTH)

    private val scaledWidth by lazy { BASE_WIDTH.scaled }
    private val scaledHeight by lazy { BASE_HEIGHT.scaled }

    private val scaleFactorX: Float get() = scaledWidth / scaleX
    private val scaleFactorY: Float get() = scaledHeight / scaleY

    override val left get() = -scaleFactorX
    override val right get() = scaleFactorX
    override val top get() = scaleFactorY - OFFSET_Y
    override val bottom get() = -scaleFactorY - OFFSET_Y
}