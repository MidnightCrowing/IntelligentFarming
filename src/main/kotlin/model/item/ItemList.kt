package com.midnightcrowing.model.item


class ItemList(size: Int) {
    private val items: MutableList<ItemStack> = MutableList(size) { ItemStack.EMPTY }

    operator fun get(index: Int): ItemStack = items[index]

    operator fun set(index: Int, stack: ItemStack) {
        require(index in items.indices) { "Index out of bounds: $index" }
        items[index] = stack
    }

    fun size(): Int = items.size

    fun isEmpty(index: Int): Boolean = items[index].isEmpty()

    fun slice(ints: IntRange): List<ItemStack> {
        require(ints.first >= 0 && ints.last < items.size) { "Index out of bounds: $ints" }
        return items.slice(ints)
    }

    fun <R> map(transform: (ItemStack) -> R): List<R> {
        return items.map(transform)
    }
}