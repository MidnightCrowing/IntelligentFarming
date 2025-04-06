package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.publics.HotBar
import com.midnightcrowing.model.item.Item
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack

class HotBarController(farmController: FarmController) {
    private lateinit var hotBar: HotBar
    private val invController: InventoryController = farmController.inventory
    private val areaController: FarmAreaController = farmController.farmArea
    private val farmArea: FarmArea by lazy { areaController.farmArea }

    val itemsList: List<ItemStack> get() = invController.hotBarItems

    // 当前选中网格 ID
    var selectedGridId: Int = 0
        set(value) {
            require(value in 0..8) { "selectedGridId 必须在 0 到 8 之间" }

            if (value != field) {
                // 更新选择框位置
                hotBar.itemCheckBox.moveTo(hotBar.getGridBoundsWithCheckbox(value))
            }

            val item: Item? = ItemRegistry.getItem(itemsList[value].id)
            hotBar.setItemLabelText(item?.name)
            areaController.handheldItem = item
            areaController.activeSeedCrop = item?.getBlock(farmArea)

            field = value
        }

    fun init(hotBar: HotBar) {
        this.hotBar = hotBar
        this.selectedGridId = 0 // 默认选中项的ID
    }

    /**
     * 获取当前选中的物品数据
     */
    fun getSelectedItemStack(): ItemStack {
        return itemsList[selectedGridId]
    }

    /**
     * 获取当前选中的物品类
     */
    fun getSelectedItem(): Item? {
        return ItemRegistry.getItem(itemsList[selectedGridId].id)
    }

    /**
     * 处理种植作物的事件
     */
    fun onPlantCrop() {
        val item = getSelectedItemStack()
        if (!item.isEmpty()) {
            item.count -= 1
            if (item.count <= 0) {
                invController.setItem(selectedGridId, ItemStack.EMPTY)
                this.update()
            } else {
                invController.setItem(selectedGridId, item)
            }
        }
    }

    /**
     * 处理使用骨粉的事件
     */
    fun onUseBoneMeal() = onPlantCrop()

    fun update() {
        this.selectedGridId = selectedGridId
    }
}