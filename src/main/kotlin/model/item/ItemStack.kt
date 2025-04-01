package com.midnightcrowing.model.item

data class ItemStack(
    val id: String,
    private var _count: Int,
) {
    init {
        require(_count in 0..64) { "物品数量必须在 0 到 64 之间" }
    }

    var count: Int
        get() = _count
        set(value) {
            _count = value.coerceIn(0, 64)
        }

    companion object {
        val EMPTY get() = ItemStack("", 0)
    }

    fun isEmpty(): Boolean = this == EMPTY
}
