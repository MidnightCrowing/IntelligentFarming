package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems.PotatoItem
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
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

    override fun getItemStack(): ItemStack = ItemStack(PotatoItem.id, 1)

    override fun getDrops(): Array<ItemStack> = arrayOf(
        ItemStack(PotatoItem.id, if (isFullyGrown) 1 + generateDropCount(n = 3, p = 8.0 / 15) else 1)
    )

    override fun toString(): String = "马铃薯"

    override fun copy(): Potato {
        val newPotato = Potato(farmArea)
        newPotato.place(this.widgetBounds)
        newPotato.nowTextures = newPotato.nowTextures
        return newPotato
    }
}