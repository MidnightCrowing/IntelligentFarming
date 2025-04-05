package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.gui.publics.CropInfoDisplay
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.renderer.ItemRenderer

class CropInfoDisplayController(farmController: FarmController) {
    lateinit var cropInfoDisplay: CropInfoDisplay

    var crop: FarmCropBase? = null
    var item: ItemStack = ItemStack.EMPTY
    var itemRender: ItemRenderer? = null

    // 使用 LinkedHashMap 缓存物品，最多缓存 10 个物品
    val itemCache: LinkedHashMap<String, ItemRenderer?> =
        object : LinkedHashMap<String, ItemRenderer?>(10, 0.75f, true) {
            override fun removeEldestEntry(eldest: Map.Entry<String, ItemRenderer?>) = size > 10
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
        itemRender = itemCache.getOrPut(item.id.toString()) {
            ItemRegistry.createItemRender(item.id, cropInfoDisplay)
        }

        // 放置物品到显示区域
        itemRender?.place(cropInfoDisplay.itemBounds)
    }
}