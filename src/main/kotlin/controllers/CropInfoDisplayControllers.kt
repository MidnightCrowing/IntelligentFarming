package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.publics.CropInfoDisplay
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack

class CropInfoDisplayControllers() {
    lateinit var cropInfoDisplay: CropInfoDisplay

    internal var crop: FarmCropBase? = null
    internal var item: ItemStack = ItemStack.EMPTY
    internal var itemWidget: FarmItems? = null

    // 使用 LinkedHashMap 缓存物品，最多缓存 10 个物品
    internal val itemCache: LinkedHashMap<String, FarmItems?> =
        object : LinkedHashMap<String, FarmItems?>(10, 0.75f, true) {
            override fun removeEldestEntry(eldest: Map.Entry<String, FarmItems?>) = size > 10
        }

    // 初始化 cropInfoDisplay
    fun init(cropInfoDisplay: CropInfoDisplay) {
        this.cropInfoDisplay = cropInfoDisplay
    }

    // 更新作物信息
    fun update(newCrop: FarmCropBase?) {
        if (crop != newCrop) {
            crop = newCrop
            cropInfoDisplay.titleText.text = newCrop?.toString() ?: ""
        }
    }

    // 更新物品
    fun updateItem(newItem: ItemStack?) {
        val updatedItem = newItem ?: ItemStack.EMPTY

        // 如果物品没有变化，直接返回
        if (item.id == updatedItem.id) return

        item = updatedItem

        // 从缓存中获取物品，如果没有则创建新的物品
        itemWidget = itemCache.getOrPut(item.id.toString()) {
            ItemRegistry.createItem(item.id, cropInfoDisplay)
        }

        // 放置物品到显示区域
        itemWidget?.place(cropInfoDisplay.itemBounds)
    }
}