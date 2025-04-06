package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.POTATO
import com.midnightcrowing.resource.TextureResourcesEnum

class Potato(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.BLOCK_POTATO_GROW_0.texture,
        1 to TextureResourcesEnum.BLOCK_POTATO_GROW_0.texture,
        2 to TextureResourcesEnum.BLOCK_POTATO_GROW_2.texture,
        3 to TextureResourcesEnum.BLOCK_POTATO_GROW_2.texture,
        4 to TextureResourcesEnum.BLOCK_POTATO_GROW_4.texture,
        5 to TextureResourcesEnum.BLOCK_POTATO_GROW_4.texture,
        6 to TextureResourcesEnum.BLOCK_POTATO_GROW_4.texture,
        7 to TextureResourcesEnum.BLOCK_POTATO_GROW_7.texture
    )

    override fun getItemStack(): ItemStack = ItemStack(POTATO.id, 1)

    override fun getDrops(l: Int): Array<ItemStack> = arrayOf(
        ItemStack(POTATO.id, if (isFullyGrown) 1 + generateDropCount(n = 3, l = l, p = 8.0 / 15) else 1)
    )

    override fun toString(): String = "马铃薯"
}