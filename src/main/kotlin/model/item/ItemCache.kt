package com.midnightcrowing.model.item

import com.midnightcrowing.gui.bases.Widget

/**
 * 物品缓存类
 *
 * 缓存物品，避免每次渲染时重复创建
 */
class ItemCache(private val parent: Widget) {
    val cache: MutableMap<String, Item?> = mutableMapOf<String, Item?>()

    // 获取物品缓存
    fun getItemCache(id: String): Item? {
        if (id.isEmpty()) return null
        return cache.getOrPut(id) { ItemRegistry.createItem(id, parent) }
    }
}