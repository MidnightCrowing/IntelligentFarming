package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.gui.publics.CropInfoDisplay
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.renderer.ItemRenderer

class CropInfoDisplayController(farmController: FarmController) {
    lateinit var cropInfoDisplay: CropInfoDisplay

    var crop: FarmCropBase? = null
    var item: ItemStack = ItemStack.EMPTY
    var itemRender: ItemRenderer? = null

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
        itemRender = cropInfoDisplay.itemRenderCache.getItemCache(item.id)

        // 放置物品到显示区域
        itemRender?.place(cropInfoDisplay.itemBounds)
    }
}