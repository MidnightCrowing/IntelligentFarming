package com.midnightcrowing.model.trade

import com.midnightcrowing.model.item.ItemStack

data class TradeRecipe(
    val buy: ItemStack,   // 第一个交易物品（通常是货币，比如 emerald）
    val buyB: ItemStack? = null,  // 第二个交易物品（可选，比如 book）
    val sell: ItemStack,  // 卖出的物品（比如 enchanted_book）
)