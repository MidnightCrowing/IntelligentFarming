package com.midnightcrowing.model.trade

import com.midnightcrowing.model.item.ItemStack

data class TradeRecipe(
    val buy: ItemStack,                     // 第一个交易物品（通常是货币，比如 emerald）
    val buyB: ItemStack = ItemStack.EMPTY,  // 第二个交易物品（可选，比如 book）
    val sell: ItemStack,                    // 卖出的物品（比如 enchanted_book）
    val maxUses: Int = -1,                  // 最大交易次数（-1 表示无限）
    val experience: Int = 0,                // 交易获得的经验值
) {
    var tradeUses: Int = 0 // 已交易的次数

    val isTradeable: Boolean get() = maxUses == -1 || tradeUses < maxUses // 是否可以交易

    /**
     * 校验是否满足交易条件（支持任意顺序）
     */
    fun canTrade(slotItems: List<ItemStack>): Boolean {
        if (!isTradeable) {
            return false // 超过最大交易次数
        }

        val remainingSlots = slotItems.toMutableList()

        // 遍历配方，验证是否满足交易条件
        for (recipeItem in listOf(buy, buyB)) {
            val matchingSlot = remainingSlots.find { it.id == recipeItem.id && it.count >= recipeItem.count }
                ?: return false // 任意一个未满足条件则交易失败
            remainingSlots.remove(matchingSlot) // 成功匹配则移除
        }
        return true
    }

    /**
     * 执行交易
     */
    fun executeTrade(slotItems: List<ItemStack>) {
        // 执行交易
        slotItems.forEach { slot ->
            if (slot.id == buy.id) {
                slot.count -= buy.count
            }
            if (slot.id == buyB.id) {
                slot.count -= buyB.count
            }
        }
        tradeUses++ // 增加交易次数
    }
}