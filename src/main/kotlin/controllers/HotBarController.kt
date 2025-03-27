package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.HotBar
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack

class HotBarController(gameController: GameController) {
    private lateinit var hotBar: HotBar
    private val invController: InventoryController = gameController.inventory
    private val farmController: FarmAreaController = gameController.farmArea
    private val farmArea: FarmArea by lazy { farmController.farmArea }

    val defaultSelectId = 0 // 默认选中项的ID
    val itemsList: List<ItemStack> get() = invController.hotBarItems

    // 当前选中网格 ID
    var selectedGridId: Int = 0
        set(value) {
            require(value in 0..8) { "selectedGridId 必须在 0 到 8 之间" }
            field = value
        }

    fun init(hotBar: HotBar) {
        this.hotBar = hotBar
        changeActiveItem(defaultSelectId)
    }

    fun changeActiveItem(id: Int) {
        selectedGridId = id
        val item = ItemRegistry.createItem(itemsList[id].id, hotBar)
        hotBar.setItemLabelText(item?.toString())
        farmArea.activeSeedCrop = item?.getCrop(farmArea)
    }
}