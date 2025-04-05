package com.midnightcrowing.model.item

import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.renderer.ItemRenderer

/**
 * 物品渲染缓存类
 *
 * 缓存物品渲染器，避免每次渲染时重复创建
 */
class ItemRenderCache(private val parent: Widget) {
    private val cache: MutableMap<String, ItemRenderer?> = mutableMapOf<String, ItemRenderer?>()

    // 获取物品缓存
    fun getItemCache(id: String): ItemRenderer? {
        if (id.isEmpty()) return null
        return cache.getOrPut(id) { ItemRegistry.createItemRender(id, parent) }
    }

    // 保留缓存中存在的键，并删除其他键
    fun retainAll(keys: Collection<String>) {
        cache.keys.retainAll(keys)
    }
}