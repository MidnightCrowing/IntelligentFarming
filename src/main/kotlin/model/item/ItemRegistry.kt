package com.midnightcrowing.model.item

import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.base.Widget

object ItemRegistry {
    private val items = mutableMapOf<String, (Widget) -> FarmItems>()

    fun register(id: String, creator: (Widget) -> FarmItems) {
        items[id] = creator
    }

    fun createItem(id: String, parent: Widget): FarmItems? {
        println("创建物品: $id")
        return items[id]?.invoke(parent)
    }
}
