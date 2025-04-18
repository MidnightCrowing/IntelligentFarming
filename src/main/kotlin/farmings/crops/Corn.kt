package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.CORN
import com.midnightcrowing.model.item.Items.CORN_SEED

class Corn(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures = growthStagesFromList(
        namespace = "corn_delight",
        path = "corn/corns"
    )

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(CORN.id, 1) else ItemStack(CORN_SEED.id, 1)

    override fun getDrops(l: Int): Array<ItemStack> {
        return if (isFullyGrown) {
            arrayOf(
                ItemStack(CORN_SEED.id, 1 + generateDropCount(n = 4, l = l, p = 8.0 / 15)),
                ItemStack(CORN.id, 1),
            )
        } else {
            arrayOf(ItemStack(CORN_SEED.id, 1))
        }
    }

    override fun toString(): String = "玉米"
}