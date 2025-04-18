package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.WHEAT
import com.midnightcrowing.model.item.Items.WHEAT_SEED

class Wheat(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures = growthStagesFromList(path = "wheat/wheat")

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