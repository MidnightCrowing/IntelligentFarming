package com.midnightcrowing.controllers

class GameController() {
    val cropInfo: CropInfoDisplayControllers = CropInfoDisplayControllers()
    val farmArea: FarmAreaController = FarmAreaController(this)
    val inventory: InventoryController = InventoryController(this)
    val hotBar: HotBarController = HotBarController(this)

    fun update() {
        farmArea.update()
    }
}