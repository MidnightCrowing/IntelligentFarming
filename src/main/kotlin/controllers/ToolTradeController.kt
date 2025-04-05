package com.midnightcrowing.controllers

import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.trade.TradeRecipe

class ToolTradeController(farmController: FarmController) : TradeController(farmController) {
    override val tradeList: TradeList = listOf(
        TradeRecipe(
            buy = ItemStack("minecraft:emerald", 8),
            sell = ItemStack("minecraft:iron_hoe", 1),
            maxUses = 12,
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:iron_hoe", 1),
            buyB = ItemStack("minecraft:emerald", 16),
            sell = ItemStack("minecraft:golden_hoe", 1),
            maxUses = 11,
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:golden_hoe", 1),
            buyB = ItemStack("minecraft:emerald", 32),
            sell = ItemStack("minecraft:diamond_hoe", 1),
            maxUses = 11,
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:diamond_hoe", 1),
            buyB = ItemStack("minecraft:emerald", 64),
            sell = ItemStack("minecraft:netherite_hoe", 1),
            maxUses = 11,
        ),
    )
}