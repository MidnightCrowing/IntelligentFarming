package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.TOMATO
import com.midnightcrowing.model.item.Items.TOMATO_SEED
import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.resource.ResourceType
import com.midnightcrowing.utils.GameTick

class Tomato(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, ResourceLocation> = mapOf(
        0 to ResourceLocation(ResourceType.TE_BLOCK, "farmersdelight", "tomato/budding_tomatoes0.png"),
        1 to ResourceLocation(ResourceType.TE_BLOCK, "farmersdelight", "tomato/budding_tomatoes1.png"),
        2 to ResourceLocation(ResourceType.TE_BLOCK, "farmersdelight", "tomato/budding_tomatoes2.png"),
        3 to ResourceLocation(ResourceType.TE_BLOCK, "farmersdelight", "tomato/budding_tomatoes3.png"),
        4 to ResourceLocation(ResourceType.TE_BLOCK, "farmersdelight", "tomato/budding_tomatoes4.png"),
        5 to ResourceLocation(ResourceType.TE_BLOCK, "farmersdelight", "tomato/tomatoes0.png"),
        6 to ResourceLocation(ResourceType.TE_BLOCK, "farmersdelight", "tomato/tomatoes1.png"),
        7 to ResourceLocation(ResourceType.TE_BLOCK, "farmersdelight", "tomato/tomatoes2.png"),
        8 to ResourceLocation(ResourceType.TE_BLOCK, "farmersdelight", "tomato/tomatoes3.png")
    )

    override fun onFarmRightClick(l: Int): Array<ItemStack>? {
        if (isFullyGrown) {
            val drops = this.getDrops(l)
            growthDuration = triangularRandom(90000.0, 800000.0, 120000.0)
            plantedTick = (GameTick.tick - growthDuration * 0.56).toLong()
            return drops
        }
        return null
    }

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(TOMATO.id, 1) else ItemStack(TOMATO_SEED.id, 1)

    override fun getDrops(l: Int): Array<ItemStack> = if (isFullyGrown) {
        arrayOf(
            ItemStack(TOMATO_SEED.id, 1),
            ItemStack(TOMATO.id, 1 + generateDropCount(n = 2, l = l, p = 8.0 / 15)),
        )
    } else {
        arrayOf(ItemStack(TOMATO_SEED.id, 1))
    }

    override fun toString(): String = "番茄"
}