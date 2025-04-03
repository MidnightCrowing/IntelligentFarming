package com.midnightcrowing.model.item

data class ItemStack(
    val id: String,
    var count: Int,
) {
    init {
        require(count in 0..64) { "物品数量必须在 0 到 64 之间" }
    }

    companion object {
        val EMPTY get() = ItemStack("", 0)
    }

    fun isEmpty(): Boolean = this == EMPTY
}
