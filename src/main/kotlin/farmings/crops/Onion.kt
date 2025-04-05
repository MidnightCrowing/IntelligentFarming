package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.ONION
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

    override fun getItemStack(): ItemStack = ItemStack(ONION.id, 1)

    override fun getDrops(): Array<ItemStack> = arrayOf(
        ItemStack(ONION.id, if (isFullyGrown) 1 + generateDropCount(n = 3, p = 8.0 / 15) else 1)
    )

    override fun toString(): String = "洋葱"

    override fun copy(): Onion {
        val newOnion = Onion(farmArea)
        newOnion.place(this.widgetBounds)
        newOnion.nowTextures = newOnion.nowTextures
        return newOnion
    }
}