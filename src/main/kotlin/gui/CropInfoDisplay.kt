package com.midnightcrowing.gui

import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.scenes.FarmScene

class CropInfoDisplay(val screen: FarmScene) : Widget(screen.window, z = 1) {
    companion object {
        // 基础尺寸常量
        private const val BASE_WIDTH = 160
        private const val BASE_HEIGHT = 32
        private const val SCALE_BASE = 350.0

        // 计算缩放比例
        private val SCALED: Double by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT by lazy { BASE_HEIGHT * SCALED }
    }

    override val renderer: ImageRenderer = ImageRenderer.createImageRenderer(ResourcesEnum.TOAST.inputStream)

    init {
        renderer.alpha = 0.8
    }
}