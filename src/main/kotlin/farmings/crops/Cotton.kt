package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.CottonItem
import com.midnightcrowing.farmings.FarmItems.CottonSeedItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.Texture
import com.midnightcrowing.resource.TextureResourcesEnum

class Cotton(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.COTTON_GROW_0.texture,
        1 to TextureResourcesEnum.COTTON_GROW_0.texture,
        2 to TextureResourcesEnum.COTTON_GROW_0.texture,
        3 to TextureResourcesEnum.COTTON_GROW_0.texture,
        4 to TextureResourcesEnum.COTTON_GROW_4.texture,
        5 to TextureResourcesEnum.COTTON_GROW_4.texture,
        6 to TextureResourcesEnum.COTTON_GROW_4.texture,
        7 to TextureResourcesEnum.COTTON_GROW_7.texture
    )

    override fun getFarmItem(parent: Widget): FarmItems =
        if (isFullyGrown) CottonItem(parent) else CottonSeedItem(parent)

    override fun toString(): String = "棉花"

    override fun copy(): Cotton {
        val newCotton = Cotton(farmArea)
        newCotton.place(this.widgetBounds)
        newCotton.nowTextures = newCotton.nowTextures
        return newCotton
    }
}