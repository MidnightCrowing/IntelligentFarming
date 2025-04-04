package com.midnightcrowing.model.item

import com.midnightcrowing.gui.bases.Widget

object ItemRegistry {
    private val items = mutableMapOf<String, (Widget) -> Item>()

    fun register(id: String, creator: (Widget) -> Item) {
        items[id] = creator
    }

    fun createItem(id: String, parent: Widget): Item? {
        println("创建物品: $id")
        return items[id]?.invoke(parent)
    }
}
