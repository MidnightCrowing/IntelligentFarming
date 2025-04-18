package com.midnightcrowing.gui.publics.trade

import com.midnightcrowing.audio.SoundEffectPlayer
import com.midnightcrowing.audio.SoundEvents
import com.midnightcrowing.gui.publics.inventory.InventoryController
import com.midnightcrowing.gui.scenes.farmScene.FarmController
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items
import com.midnightcrowing.model.trade.TradeRecipe

typealias TradeList = List<TradeRecipe>

open class TradeController(farmController: FarmController) {
    private lateinit var trade: Trade
    val invController: InventoryController by lazy { farmController.inventory }

    open val tradeList: TradeList = listOf(
        TradeRecipe(ItemStack(Items.WHEAT.id, 16), sell = ItemStack(Items.EMERALD.id, 2)),
        TradeRecipe(ItemStack(Items.CARROT.id, 32), sell = ItemStack(Items.EMERALD.id, 3)),
        TradeRecipe(ItemStack(Items.POTATO.id, 32), sell = ItemStack(Items.EMERALD.id, 3)),
        TradeRecipe(ItemStack(Items.CABBAGE.id, 20), sell = ItemStack(Items.EMERALD.id, 2)),
        TradeRecipe(ItemStack(Items.CORN.id, 21), sell = ItemStack(Items.EMERALD.id, 4)),
        TradeRecipe(ItemStack(Items.TOMATO.id, 19), sell = ItemStack(Items.EMERALD.id, 3)),
        TradeRecipe(ItemStack(Items.ONION.id, 14), sell = ItemStack(Items.EMERALD.id, 2)),
        TradeRecipe(ItemStack(Items.COTTON.id, 5), sell = ItemStack(Items.EMERALD.id, 1)),
        TradeRecipe(
            ItemStack(Items.GOLDEN_CARROT.id, 1), ItemStack(Items.CARROT.id, 6),
            sell = ItemStack(Items.EMERALD.id, 64)
        ),
        TradeRecipe(ItemStack(Items.EMERALD.id, 1), sell = ItemStack(Items.WHEAT_SEED.id, 1)),
        TradeRecipe(ItemStack(Items.EMERALD.id, 11), sell = ItemStack(Items.CARROT.id, 1)),
        TradeRecipe(ItemStack(Items.EMERALD.id, 11), sell = ItemStack(Items.POTATO.id, 1)),
        TradeRecipe(ItemStack(Items.EMERALD.id, 16), sell = ItemStack(Items.CABBAGE_SEED.id, 1)),
        TradeRecipe(ItemStack(Items.EMERALD.id, 16), sell = ItemStack(Items.CORN_SEED.id, 1)),
        TradeRecipe(ItemStack(Items.EMERALD.id, 18), sell = ItemStack(Items.TOMATO_SEED.id, 1)),
        TradeRecipe(ItemStack(Items.EMERALD.id, 20), sell = ItemStack(Items.ONION.id, 1)),
        TradeRecipe(ItemStack(Items.EMERALD.id, 22), sell = ItemStack(Items.COTTON_SEED.id, 1)),
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
        val inputs = listOf(trade.tradeSlot0Item, trade.tradeSlot1Item)
        val recipe = (trade.tradeButtonGroup.select as? TradeButton)?.tradeRecipe
            ?: tradeList.findFirstValidTrade(inputs)

        trade.tradeSlot2Item = if (recipe?.canTrade(inputs) == true) recipe.sell.copy() else ItemStack.EMPTY
        trade.showUnableArrow = recipe != null && !recipe.isTradeable
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
        if (!tradeRecipe.canTrade(slotItems)) {
            SoundEffectPlayer.play(SoundEvents.ENTITY_VILLAGER_NO)
            return
        }

        val sellItem = tradeRecipe.sell

        // 回调验证（如背包空间）
        if (!validationCallback(sellItem)) return

        // 执行交易
        tradeRecipe.executeTrade(slotItems)

        // 更新空槽
        trade.tradeSlot0Item = trade.tradeSlot0Item.takeIf { it.count > 0 } ?: ItemStack.EMPTY
        trade.tradeSlot1Item = trade.tradeSlot1Item.takeIf { it.count > 0 } ?: ItemStack.EMPTY

        // 更新交易按钮状态
        trade.tradeButtons.find { it.tradeRecipe == tradeRecipe }?.let {
            it.isTradeable = tradeRecipe.isTradeable
        }

        // 播放交易音效
        SoundEffectPlayer.play(SoundEvents.ENTITY_VILLAGER_YES)

        updateTrade() // 更新交易状态
    }
}