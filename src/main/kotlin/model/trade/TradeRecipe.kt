package com.midnightcrowing.model.trade

import com.midnightcrowing.model.item.ItemStack

data class TradeRecipe(
    val buy: ItemStack,                     // 第一个交易物品（通常是货币，比如 emerald）
    val buyB: ItemStack = ItemStack.EMPTY,  // 第二个交易物品（可选，比如 book）
    val sell: ItemStack,                    // 卖出的物品（比如 enchanted_book）
) {
    /**
     * 校验是否满足交易条件（支持任意顺序）
     */
    fun canTrade(slotItems: List<ItemStack>): Boolean {
        val remainingSlots = slotItems.toMutableList()

        // 遍历配方，验证是否满足交易条件
        for (recipeItem in listOf(buy, buyB)) {
            val matchingSlot = remainingSlots.find { it.id == recipeItem.id && it.count >= recipeItem.count }
                ?: return false // 任意一个未满足条件则交易失败
            remainingSlots.remove(matchingSlot) // 成功匹配则移除
        }
        return true
    }
}