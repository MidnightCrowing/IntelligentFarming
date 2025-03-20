package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.render.Texture
import com.midnightcrowing.render.createImageTexture
import com.midnightcrowing.resource.ResourcesEnum

class Carrot(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to createImageTexture(ResourcesEnum.CARROT_GROW_0.inputStream),
        1 to createImageTexture(ResourcesEnum.CARROT_GROW_0.inputStream),
        2 to createImageTexture(ResourcesEnum.CARROT_GROW_2.inputStream),
        3 to createImageTexture(ResourcesEnum.CARROT_GROW_2.inputStream),
        4 to createImageTexture(ResourcesEnum.CARROT_GROW_4.inputStream),
        5 to createImageTexture(ResourcesEnum.CARROT_GROW_4.inputStream),
        6 to createImageTexture(ResourcesEnum.CARROT_GROW_4.inputStream),
        7 to createImageTexture(ResourcesEnum.CARROT_GROW_7.inputStream)
    )

    override fun copy(): Carrot {
        val newCarrot = Carrot(farmArea)
        newCarrot.place(this.widgetBounds)
        newCarrot.renderer.texture = newCarrot.growInitTexture
        return newCarrot
    }
}