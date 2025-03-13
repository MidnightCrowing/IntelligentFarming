package com.midnightcrowing.gui.components.inventory

import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import com.midnightcrowing.render.Renderer
import com.midnightcrowing.render.createRenderer
import com.midnightcrowing.resource.ResourcesEnum

class Inventory(window: Window) : Widget(window) {
    override val renderer: Renderer = createRenderer(ResourcesEnum.INVENTORY.inputStream)

    private companion object {
        const val BASE_WIDTH = 352
        const val BASE_HEIGHT = 198
        const val OFFSET_Y = 40 // 向下偏移的距离
        const val SCALE_BASE = 700f
    }

    private val Int.scaled: Float get() = this * (SCALE_BASE / BASE_WIDTH)

    private val scaledWidth by lazy { BASE_WIDTH.scaled }
    private val scaledHeight by lazy { BASE_HEIGHT.scaled }

    override val screenLeft: Float get() = (window.width - scaledWidth) / 2
    override val screenRight: Float get() = screenLeft + scaledWidth
    override val screenTop: Float get() = (window.height - scaledHeight) / 2 + OFFSET_Y
    override val screenBottom: Float get() = screenTop + scaledHeight
}