package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.POTATO

class Potato(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures = growthStagesFromList(
        path = "potato/potatoes",
        stages = listOf(0, 0, 2, 2, 4, 4, 4, 7)
    )

    override fun getItemStack(): ItemStack = ItemStack(POTATO.id, 1)

    override fun getDrops(l: Int): Array<ItemStack> = arrayOf(
        ItemStack(POTATO.id, if (isFullyGrown) 1 + generateDropCount(n = 3, l = l, p = 8.0 / 15) else 1)
    )

    override fun toString(): String = "马铃薯"
}