package com.midnightcrowing.controllers

import com.midnightcrowing.gui.scenes.farmScene.Trade

class TradeController(farmController: FarmController) {
    private lateinit var trade: Trade
    val invController: InventoryController by lazy { farmController.inventory }

    fun init(trade: Trade) {
        this.trade = trade
    }
}