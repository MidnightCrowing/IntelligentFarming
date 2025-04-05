package com.midnightcrowing.model.item

data class ItemStack(
    val id: String,
    var count: Int,
) {
    init {
        val maxCount: Int = ItemRegistry.getItemMaxCount(id)
        require(count in 0..maxCount) { "$id 的物品数量必须在 0 到 $maxCount 之间" }
    }

    companion object {
        val EMPTY get() = ItemStack("", 0)
    }

    fun isEmpty(): Boolean = this == EMPTY

    override fun toString() = if (isEmpty()) "${this::class.qualifiedName}.EMPTY" else super.toString()
}
