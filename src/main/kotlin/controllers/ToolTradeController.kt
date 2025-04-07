package com.midnightcrowing.controllers

import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items
import com.midnightcrowing.model.trade.TradeRecipe

class ToolTradeController(farmController: FarmController) : TradeController(farmController) {
    override val tradeList: TradeList = listOf(
        TradeRecipe(
            ItemStack(Items.EMERALD.id, 8),
            sell = ItemStack(Items.IRON_HOE.id, 1),
            maxUses = 1,
        ),
        TradeRecipe(
            ItemStack(Items.IRON_HOE.id, 1), ItemStack(Items.EMERALD.id, 16),
            sell = ItemStack(Items.GOLDEN_HOE.id, 1),
            maxUses = 1,
        ),
        TradeRecipe(
            ItemStack(Items.GOLDEN_HOE.id, 1), ItemStack(Items.EMERALD.id, 32),
            sell = ItemStack(Items.DIAMOND_HOE.id, 1),
            maxUses = 1,
        ),
        TradeRecipe(
            ItemStack(Items.DIAMOND_HOE.id, 1), ItemStack(Items.EMERALD.id, 64),
            sell = ItemStack(Items.NETHERITE_HOE.id, 1),
            maxUses = 1,
        ),
    )
}