package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.WHEAT
import com.midnightcrowing.model.item.Items.WHEAT_SEED
import com.midnightcrowing.resource.TextureResourcesEnum

class Wheat(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.BLOCK_WHEAT_GROW_0.texture,
        1 to TextureResourcesEnum.BLOCK_WHEAT_GROW_1.texture,
        2 to TextureResourcesEnum.BLOCK_WHEAT_GROW_2.texture,
        3 to TextureResourcesEnum.BLOCK_WHEAT_GROW_3.texture,
        4 to TextureResourcesEnum.BLOCK_WHEAT_GROW_4.texture,
        5 to TextureResourcesEnum.BLOCK_WHEAT_GROW_5.texture,
        6 to TextureResourcesEnum.BLOCK_WHEAT_GROW_6.texture,
        7 to TextureResourcesEnum.BLOCK_WHEAT_GROW_7.texture
    )

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(WHEAT.id, 1) else ItemStack(WHEAT_SEED.id, 1)

    override fun getDrops(l: Int): Array<ItemStack> {
        return if (isFullyGrown) {
            arrayOf(
                ItemStack(WHEAT_SEED.id, 1 + generateDropCount(n = 3, l = l, p = 4.0 / 7)),
                ItemStack(WHEAT.id, 1),
            )
        } else {
            arrayOf(ItemStack(WHEAT_SEED.id, 1))
        }
    }

    override fun toString(): String = "小麦"
}