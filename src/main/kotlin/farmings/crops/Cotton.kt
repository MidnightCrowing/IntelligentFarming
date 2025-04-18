package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.COTTON
import com.midnightcrowing.model.item.Items.COTTON_SEED

class Cotton(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures = growthStagesFromList(
        namespace = "cotton",
        path = "cotton/cottons",
        stages = listOf(0, 0, 0, 0, 4, 4, 4, 7)
    )

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(COTTON.id, 1) else ItemStack(COTTON_SEED.id, 1)

    override fun getDrops(l: Int): Array<ItemStack> {
        return if (isFullyGrown) {
            arrayOf(
                ItemStack(COTTON_SEED.id, 1 + generateDropCount(n = 4, l = l, p = 4.0 / 7)),
                ItemStack(COTTON.id, 1),
            )
        } else {
            arrayOf(ItemStack(COTTON_SEED.id, 1))
        }
    }

    override fun toString(): String = "棉花"
}