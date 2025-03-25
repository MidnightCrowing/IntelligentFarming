package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.CottonItem
import com.midnightcrowing.farmings.FarmItems.CottonSeedItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.Texture
import com.midnightcrowing.resource.ResourcesEnum

class Cotton(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to Texture.createImageTexture(ResourcesEnum.COTTON_GROW_0.inputStream),
        1 to Texture.createImageTexture(ResourcesEnum.COTTON_GROW_0.inputStream),
        2 to Texture.createImageTexture(ResourcesEnum.COTTON_GROW_0.inputStream),
        3 to Texture.createImageTexture(ResourcesEnum.COTTON_GROW_0.inputStream),
        4 to Texture.createImageTexture(ResourcesEnum.COTTON_GROW_4.inputStream),
        5 to Texture.createImageTexture(ResourcesEnum.COTTON_GROW_4.inputStream),
        6 to Texture.createImageTexture(ResourcesEnum.COTTON_GROW_4.inputStream),
        7 to Texture.createImageTexture(ResourcesEnum.COTTON_GROW_7.inputStream)
    )

    override fun getFarmItem(parent: Widget): FarmItems =
        if (isFullyGrown) CottonItem(parent) else CottonSeedItem(parent)

    override fun toString(): String = "棉花"

    override fun copy(): Cotton {
        val newCotton = Cotton(farmArea)
        newCotton.place(this.widgetBounds)
        newCotton.renderer.texture = newCotton.growInitTexture
        return newCotton
    }
}