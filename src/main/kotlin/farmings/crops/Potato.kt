package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.PotatoItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.Texture
import com.midnightcrowing.resource.TextureResourcesEnum

class Potato(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.POTATO_GROW_0.texture,
        1 to TextureResourcesEnum.POTATO_GROW_0.texture,
        2 to TextureResourcesEnum.POTATO_GROW_2.texture,
        3 to TextureResourcesEnum.POTATO_GROW_2.texture,
        4 to TextureResourcesEnum.POTATO_GROW_4.texture,
        5 to TextureResourcesEnum.POTATO_GROW_4.texture,
        6 to TextureResourcesEnum.POTATO_GROW_4.texture,
        7 to TextureResourcesEnum.POTATO_GROW_7.texture
    )

    override fun getFarmItem(parent: Widget): FarmItems = PotatoItem(parent)

    override fun toString(): String = "马铃薯"

    override fun copy(): Potato {
        val newPotato = Potato(farmArea)
        newPotato.place(this.widgetBounds)
        newPotato.nowTextures = newPotato.nowTextures
        return newPotato
    }
}