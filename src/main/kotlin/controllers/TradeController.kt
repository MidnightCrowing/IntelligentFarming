package com.midnightcrowing.controllers

import com.midnightcrowing.gui.scenes.farmScene.Trade
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.trade.TradeRecipe

class TradeController(farmController: FarmController) {
    private lateinit var trade: Trade
    val invController: InventoryController by lazy { farmController.inventory }

    val tradeList: List<TradeRecipe> = listOf(
        TradeRecipe(
            buy = ItemStack("minecraft:wheat", 5),
            sell = ItemStack("minecraft:emerald", 2)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:potato", 5),
            sell = ItemStack("minecraft:emerald", 2)
        )
    )

    fun init(trade: Trade) {
        this.trade = trade
    }


}