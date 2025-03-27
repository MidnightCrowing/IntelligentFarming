package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.OnionItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.Texture
import com.midnightcrowing.resource.TextureResourcesEnum

class Onion(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.ONION_GROW_0.texture,
        1 to TextureResourcesEnum.ONION_GROW_0.texture,
        2 to TextureResourcesEnum.ONION_GROW_2.texture,
        3 to TextureResourcesEnum.ONION_GROW_2.texture,
        4 to TextureResourcesEnum.ONION_GROW_4.texture,
        5 to TextureResourcesEnum.ONION_GROW_4.texture,
        6 to TextureResourcesEnum.ONION_GROW_4.texture,
        7 to TextureResourcesEnum.ONION_GROW_7.texture
    )

    override fun getFarmItem(parent: Widget): FarmItems = OnionItem(parent)

    override fun toString(): String = "洋葱"

    override fun copy(): Onion {
        val newOnion = Onion(farmArea)
        newOnion.place(this.widgetBounds)
        newOnion.nowTextures = newOnion.nowTextures
        return newOnion
    }
}