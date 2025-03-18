package com.midnightcrowing.gui

import com.midnightcrowing.events.Event
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum

class Inventory : Widget {
    constructor(window: Window) : super(window)

    constructor(parent: Widget) : super(parent)

    companion object {
        private const val BASE_WIDTH = 352
        private const val BASE_HEIGHT = 198
        const val OFFSET_Y = 40 // 向下偏移的距离
        private const val SCALE_BASE = 700f

        private val SCALED: Float by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH: Float by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT: Float by lazy { BASE_HEIGHT * SCALED }
    }

    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.INVENTORY.inputStream)

    override fun onWindowResize(e: Event.WindowResizeEvent) {
        val x1: Float = (e.width - SCALED_WIDTH) / 2
        val x2: Float = x1 + SCALED_WIDTH
        val y1: Float = (e.height - SCALED_HEIGHT) / 2 + OFFSET_Y
        val y2: Float = y1 + SCALED_HEIGHT

        place(x1, y1, x2, y2)
    }
}