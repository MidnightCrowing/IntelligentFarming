package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.WheatItem
import com.midnightcrowing.farmings.FarmItems.WheatSeedItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.Texture
import com.midnightcrowing.resource.ResourcesEnum

class Wheat(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to Texture.createImageTexture(ResourcesEnum.WHEAT_GROW_0.inputStream),
        1 to Texture.createImageTexture(ResourcesEnum.WHEAT_GROW_1.inputStream),
        2 to Texture.createImageTexture(ResourcesEnum.WHEAT_GROW_2.inputStream),
        3 to Texture.createImageTexture(ResourcesEnum.WHEAT_GROW_3.inputStream),
        4 to Texture.createImageTexture(ResourcesEnum.WHEAT_GROW_4.inputStream),
        5 to Texture.createImageTexture(ResourcesEnum.WHEAT_GROW_5.inputStream),
        6 to Texture.createImageTexture(ResourcesEnum.WHEAT_GROW_6.inputStream),
        7 to Texture.createImageTexture(ResourcesEnum.WHEAT_GROW_7.inputStream)
    )

    override fun getFarmItem(parent: Widget): FarmItems =
        if (isFullyGrown) WheatItem(parent) else WheatSeedItem(parent)

    override fun toString(): String = "小麦"

    override fun copy(): Wheat {
        val newWheat = Wheat(farmArea)
        newWheat.place(this.widgetBounds)
        newWheat.renderer.texture = newWheat.growInitTexture
        return newWheat
    }
}