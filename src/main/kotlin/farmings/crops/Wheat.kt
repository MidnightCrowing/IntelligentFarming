package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.render.Texture
import com.midnightcrowing.render.createImageTexture
import com.midnightcrowing.resource.ResourcesEnum

class Wheat(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growInitTexture: Texture = createImageTexture(ResourcesEnum.WHEAT_GROW_0.inputStream)
    override val growFullTexture: Texture = createImageTexture(ResourcesEnum.WHEAT_GROW_7.inputStream)

    override fun copy(): Wheat {
        val newWheat = Wheat(farmArea)
        newWheat.place(this.widgetBounds)
        newWheat.renderer.texture = newWheat.growInitTexture
        return newWheat
    }
}