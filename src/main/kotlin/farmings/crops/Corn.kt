package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.CornItem
import com.midnightcrowing.farmings.FarmItems.CornSeedItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.Texture
import com.midnightcrowing.resource.TextureResourcesEnum

class Corn(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.CORN_GROW_0.texture,
        1 to TextureResourcesEnum.CORN_GROW_1.texture,
        2 to TextureResourcesEnum.CORN_GROW_2.texture,
        3 to TextureResourcesEnum.CORN_GROW_3.texture,
        4 to TextureResourcesEnum.CORN_GROW_4.texture,
        5 to TextureResourcesEnum.CORN_GROW_5.texture,
        6 to TextureResourcesEnum.CORN_GROW_6.texture,
        7 to TextureResourcesEnum.CORN_GROW_7.texture
    )

    override fun getFarmItem(parent: Widget): FarmItems =
        if (isFullyGrown) CornItem(parent) else CornSeedItem(parent)

    override fun toString(): String = "玉米"

    override fun copy(): Corn {
        val newCorn = Corn(farmArea)
        newCorn.place(this.widgetBounds)
        newCorn.nowTextures = newCorn.nowTextures
        return newCorn
    }
}