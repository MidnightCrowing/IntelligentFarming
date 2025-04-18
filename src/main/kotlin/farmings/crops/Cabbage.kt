package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.CABBAGE
import com.midnightcrowing.model.item.Items.CABBAGE_SEED

class Cabbage(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures = growthStagesFromList(
        namespace = "farmersdelight",
        path = "cabbage/cabbages"
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