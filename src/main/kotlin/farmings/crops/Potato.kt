package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.render.Texture
import com.midnightcrowing.render.createImageTexture
import com.midnightcrowing.resource.ResourcesEnum

class Potato(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growInitTexture: Texture = createImageTexture(ResourcesEnum.POTATO_GROW_0.inputStream)
    override val growFullTexture: Texture = createImageTexture(ResourcesEnum.POTATO_GROW_7.inputStream)

    override fun copy(): Potato {
        val newPotato = Potato(farmArea)
        newPotato.place(this.widgetBounds)
        newPotato.renderer.texture = newPotato.growInitTexture
        return newPotato
    }
}