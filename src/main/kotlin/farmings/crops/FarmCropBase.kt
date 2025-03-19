package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.Texture

abstract class FarmCropBase(val farmArea: FarmArea) : Widget(farmArea) {
    abstract val growInitTexture: Texture
    abstract val growFullTexture: Texture

    fun setShadow() {
        renderer.texture = growInitTexture
        renderer.alpha = 0.6f
    }

    fun setPlanting() {
//        renderer.texture = growFullTexture
        renderer.alpha = 1f
    }

    abstract fun copy(): FarmCropBase
}