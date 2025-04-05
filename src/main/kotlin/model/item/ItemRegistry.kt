package com.midnightcrowing.model.item

import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.renderer.ItemRenderer

object ItemRegistry {
    private val items = mutableMapOf<String, Item>()

    fun register(item: Item) {
        if (items.containsKey(item.id)) {
            throw IllegalArgumentException("物品ID ${item.id} 已经存在。")
        }
        items[item.id] = item
    }

    fun getItem(id: String): Item? {
        return items[id]
    }

    fun getItemMaxCount(id: String): Int {
        return items[id]?.maxCount ?: 64
    }

    fun createItemRender(id: String, parent: Widget): ItemRenderer? {
        println("创建物品: $id")
        return items[id]?.let { ItemRenderer(parent.window.nvg, it) }
    }
}
