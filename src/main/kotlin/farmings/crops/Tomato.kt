package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.render.Texture
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.utils.GameTick

class Tomato(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to Texture.createImageTexture(ResourcesEnum.BUDDING_TOMATO_GROW_0.inputStream),
        1 to Texture.createImageTexture(ResourcesEnum.BUDDING_TOMATO_GROW_1.inputStream),
        2 to Texture.createImageTexture(ResourcesEnum.BUDDING_TOMATO_GROW_2.inputStream),
        3 to Texture.createImageTexture(ResourcesEnum.BUDDING_TOMATO_GROW_3.inputStream),
        4 to Texture.createImageTexture(ResourcesEnum.BUDDING_TOMATO_GROW_4.inputStream),
        5 to Texture.createImageTexture(ResourcesEnum.TOMATO_GROW_0.inputStream),
        6 to Texture.createImageTexture(ResourcesEnum.TOMATO_GROW_1.inputStream),
        7 to Texture.createImageTexture(ResourcesEnum.TOMATO_GROW_2.inputStream),
        8 to Texture.createImageTexture(ResourcesEnum.TOMATO_GROW_3.inputStream)
    )

    override fun onFarmRightClick() {
        if (isFullyGrown) {
            growthDuration = triangularRandom(0.0, 80000.0, 12000.0)
            plantedTick = (GameTick.tick - growthDuration * 0.56).toLong()
        }
    }

    override fun copy(): Tomato {
        val newTomato = Tomato(farmArea)
        newTomato.place(this.widgetBounds)
        newTomato.renderer.texture = newTomato.growInitTexture
        return newTomato
    }
}