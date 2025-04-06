package com.midnightcrowing.model.item

import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.renderer.ItemRenderer

/**
 * 物品渲染缓存类
 *
 * 缓存物品渲染器，避免每次渲染时重复创建。
 * 可选设置最大缓存大小，超过则移除最近最少使用的缓存。
 */
class ItemRenderCache(
    private val parent: Widget,
    private val maxSize: Int = -1, // 默认无限制
) {
    private val cache: MutableMap<String, ItemRenderer?> =
        object : LinkedHashMap<String, ItemRenderer?>(16, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, ItemRenderer?>): Boolean {
                return maxSize > 0 && size > maxSize
            }
        }

    // 获取物品缓存（访问过的会被标记为最近使用）
    fun getItemCache(id: String): ItemRenderer? {
        if (id.isEmpty()) return null
        return cache.getOrPut(id) { ItemRegistry.createItemRender(id, parent) }
    }

    // 保留缓存中存在的键，并删除其他键
    fun retainAll(itemList: ItemList) {
        cache.keys.retainAll(itemList.map { it.id })
    }

    fun retainAll(keys: Collection<String>) {
        cache.keys.retainAll(keys)
    }
}