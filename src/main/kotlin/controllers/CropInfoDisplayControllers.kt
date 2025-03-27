package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.CropInfoDisplay

class CropInfoDisplayControllers() {
    lateinit var cropInfoDisplay: CropInfoDisplay

    internal var crop: FarmCropBase? = null
    internal var item: FarmItems? = null

    fun init(cropInfoDisplay: CropInfoDisplay) {
        this.cropInfoDisplay = cropInfoDisplay
    }

    fun setFarmCrop(newCrop: FarmCropBase?) {
        if (crop != newCrop) {
            crop = newCrop
            cropInfoDisplay.titleText.text = newCrop?.toString() ?: ""
        }
    }

    fun updateItem(newItem: FarmItems?) {
        if (item?.let { it::class } != newItem?.let { it::class }) {  // 仅在 item 变化时才替换
            item?.cleanup()
            item = newItem
            item?.place(cropInfoDisplay.itemBounds)
        }
    }
}