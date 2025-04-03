package com.midnightcrowing.controllers

import com.midnightcrowing.gui.scenes.farmScene.Trade
import com.midnightcrowing.gui.scenes.farmScene.TradeButton
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.trade.TradeRecipe

typealias TradeList = List<TradeRecipe>

class TradeController(farmController: FarmController) {
    private lateinit var trade: Trade
    val invController: InventoryController by lazy { farmController.inventory }

    val tradeList: TradeList = listOf(
        TradeRecipe(
            buy = ItemStack("minecraft:wheat", 16),
            sell = ItemStack("minecraft:emerald", 2)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:carrot", 32),
            sell = ItemStack("minecraft:emerald", 3)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:potato", 32),
            sell = ItemStack("minecraft:emerald", 3)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:cabbage", 20),
            sell = ItemStack("minecraft:emerald", 2)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:corn", 21),
            sell = ItemStack("minecraft:emerald", 4)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:tomato", 19),
            sell = ItemStack("minecraft:emerald", 3)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:onion", 14),
            sell = ItemStack("minecraft:emerald", 2)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:cotton", 5),
            sell = ItemStack("minecraft:emerald", 1)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:golden_carrot", 1),
            buyB = ItemStack("minecraft:carrot", 6),
            sell = ItemStack("minecraft:emerald", 64)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:emerald", 1),
            sell = ItemStack("minecraft:wheat_seed", 1)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:emerald", 11),
            sell = ItemStack("minecraft:carrot", 1)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:emerald", 11),
            sell = ItemStack("minecraft:potato", 1)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:emerald", 16),
            sell = ItemStack("minecraft:cabbage_seed", 1)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:emerald", 16),
            sell = ItemStack("minecraft:corn_seed", 1)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:emerald", 18),
            sell = ItemStack("minecraft:tomato_seed", 1)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:emerald", 20),
            sell = ItemStack("minecraft:onion", 1)
        ),
        TradeRecipe(
            buy = ItemStack("minecraft:emerald", 22),
            sell = ItemStack("minecraft:cotton_seed", 1)
        ),
    )

    private fun TradeList.findFirstValidTrade(slotItems: List<ItemStack>): TradeRecipe? {
        return this.firstOrNull { it.canTrade(slotItems) }
    }

    fun init(trade: Trade) {
        this.trade = trade
    }

    /**
     * 更新交易槽位状态
     */
    fun updateTrade() {
        val slotItems = listOf(trade.tradeSlot0Item, trade.tradeSlot1Item)
        val selectedButton = trade.tradeButtonGroup.select as? TradeButton
        val tradeRecipe = selectedButton?.tradeRecipe ?: tradeList.findFirstValidTrade(slotItems)

        // 校验任意顺序的交易条件
        trade.tradeSlot2Item = if (tradeRecipe != null && tradeRecipe.canTrade(slotItems)) {
            tradeRecipe.sell.copy()
        } else {
            ItemStack.EMPTY
        }
    }

    /**
     * 执行交易操作
     * @param validationCallback 验证交易是否能成功的回调
     */
    fun executeTrade(validationCallback: (ItemStack) -> Boolean) {
        val slotItems = listOf(trade.tradeSlot0Item, trade.tradeSlot1Item)
        val selectedButton = trade.tradeButtonGroup.select as? TradeButton
        val tradeRecipe = selectedButton?.tradeRecipe ?: tradeList.findFirstValidTrade(slotItems) ?: return

        // 校验交易条件
        if (!tradeRecipe.canTrade(slotItems)) return

        val sellItem = tradeRecipe.sell

        // 回调验证（如背包空间）
        if (!validationCallback(sellItem)) return

        // 执行交易
        slotItems.forEach { slot ->
            tradeRecipe.buy.takeIf { it.id == slot.id && it.count <= slot.count }?.let {
                slot.count -= it.count
            }
            tradeRecipe.buyB.takeIf { it.id == slot.id && it.count <= slot.count }?.let {
                slot.count -= it.count
            }
        }

        // 更新空槽
        trade.tradeSlot0Item = trade.tradeSlot0Item.takeIf { it.count > 0 } ?: ItemStack.EMPTY
        trade.tradeSlot1Item = trade.tradeSlot1Item.takeIf { it.count > 0 } ?: ItemStack.EMPTY

        updateTrade() // 更新交易状态
    }
}