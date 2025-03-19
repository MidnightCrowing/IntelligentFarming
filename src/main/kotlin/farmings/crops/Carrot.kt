package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.render.Texture
import com.midnightcrowing.render.createImageTexture
import com.midnightcrowing.resource.ResourcesEnum

class Carrot(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growInitTexture: Texture = createImageTexture(ResourcesEnum.CARROT_GROW_0.inputStream)
    override val growFullTexture: Texture = createImageTexture(ResourcesEnum.CARROT_GROW_7.inputStream)

    override fun copy(): Carrot {
        val newCarrot = Carrot(farmArea)
        newCarrot.place(this.widgetBounds)
        newCarrot.renderer.texture = newCarrot.growInitTexture
        return newCarrot
    }
}