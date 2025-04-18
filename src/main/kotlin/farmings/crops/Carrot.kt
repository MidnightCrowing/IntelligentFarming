package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.CARROT

class Carrot(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures = growthStagesFromList(
        path = "carrot/carrots",
        stages = listOf(0, 0, 2, 2, 4, 4, 4, 7)
    )

    override fun getItemStack(): ItemStack = ItemStack(CARROT.id, 1)

    override fun getDrops(l: Int): Array<ItemStack> = arrayOf(
        ItemStack(CARROT.id, if (isFullyGrown) 1 + generateDropCount(n = 3, l = l, p = 8.0 / 15) else 1),
        ItemStack(CARROT.id, if (isFullyGrown) generateDropCount(n = 1, l = l, p = 1.0 / 64) else 0)
    )

    override fun toString(): String = "胡萝卜"
}