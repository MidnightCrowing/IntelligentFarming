package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.CABBAGE
import com.midnightcrowing.model.item.Items.CABBAGE_SEED
import com.midnightcrowing.resource.TextureResourcesEnum

class Cabbage(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.BLOCK_CABBAGE_GROW_0.texture,
        1 to TextureResourcesEnum.BLOCK_CABBAGE_GROW_1.texture,
        2 to TextureResourcesEnum.BLOCK_CABBAGE_GROW_2.texture,
        3 to TextureResourcesEnum.BLOCK_CABBAGE_GROW_3.texture,
        4 to TextureResourcesEnum.BLOCK_CABBAGE_GROW_4.texture,
        5 to TextureResourcesEnum.BLOCK_CABBAGE_GROW_5.texture,
        6 to TextureResourcesEnum.BLOCK_CABBAGE_GROW_6.texture,
        7 to TextureResourcesEnum.BLOCK_CABBAGE_GROW_7.texture
    )

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(CABBAGE.id, 1) else ItemStack(CABBAGE_SEED.id, 1)

    override fun getDrops(l: Int): Array<ItemStack> {
        return if (isFullyGrown) {
            arrayOf(
                ItemStack(CABBAGE_SEED.id, 1 + generateDropCount(n = 3, l = l, p = 8.0 / 15)),
                ItemStack(CABBAGE.id, 1),
            )
        } else {
            arrayOf(ItemStack(CABBAGE_SEED.id, 1))
        }
    }

    override fun toString(): String = "卷心菜"
}