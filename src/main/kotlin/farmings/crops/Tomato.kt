package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.TomatoItem
import com.midnightcrowing.farmings.FarmItems.TomatoSeedItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.Texture
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.utils.GameTick

class Tomato(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.BUDDING_TOMATO_GROW_0.texture,
        1 to TextureResourcesEnum.BUDDING_TOMATO_GROW_1.texture,
        2 to TextureResourcesEnum.BUDDING_TOMATO_GROW_2.texture,
        3 to TextureResourcesEnum.BUDDING_TOMATO_GROW_3.texture,
        4 to TextureResourcesEnum.BUDDING_TOMATO_GROW_4.texture,
        5 to TextureResourcesEnum.TOMATO_GROW_0.texture,
        6 to TextureResourcesEnum.TOMATO_GROW_1.texture,
        7 to TextureResourcesEnum.TOMATO_GROW_2.texture,
        8 to TextureResourcesEnum.TOMATO_GROW_3.texture
    )

    override fun onFarmRightClick() {
        if (isFullyGrown) {
            growthDuration = triangularRandom(0.0, 80000.0, 12000.0)
            plantedTick = (GameTick.tick - growthDuration * 0.56).toLong()
        }
    }

    override fun getFarmItem(parent: Widget): FarmItems =
        if (isFullyGrown) TomatoItem(parent) else TomatoSeedItem(parent)

    override fun toString(): String = "番茄"

    override fun copy(): Tomato {
        val newTomato = Tomato(farmArea)
        newTomato.place(this.widgetBounds)
        newTomato.nowTextures = newTomato.nowTextures
        return newTomato
    }
}