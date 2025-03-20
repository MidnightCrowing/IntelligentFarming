package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.render.Texture
import com.midnightcrowing.render.createImageTexture
import com.midnightcrowing.resource.ResourcesEnum

class Wheat(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to createImageTexture(ResourcesEnum.WHEAT_GROW_0.inputStream),
        1 to createImageTexture(ResourcesEnum.WHEAT_GROW_1.inputStream),
        2 to createImageTexture(ResourcesEnum.WHEAT_GROW_2.inputStream),
        3 to createImageTexture(ResourcesEnum.WHEAT_GROW_3.inputStream),
        4 to createImageTexture(ResourcesEnum.WHEAT_GROW_4.inputStream),
        5 to createImageTexture(ResourcesEnum.WHEAT_GROW_5.inputStream),
        6 to createImageTexture(ResourcesEnum.WHEAT_GROW_6.inputStream),
        7 to createImageTexture(ResourcesEnum.WHEAT_GROW_7.inputStream)
    )

    override fun copy(): Wheat {
        val newWheat = Wheat(farmArea)
        newWheat.place(this.widgetBounds)
        newWheat.renderer.texture = newWheat.growInitTexture
        return newWheat
    }
}