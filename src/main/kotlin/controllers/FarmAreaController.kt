package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.CropInfoDisplay

class FarmAreaController(gameController: GameController) {
    lateinit var farmArea: FarmArea

    val cropInfoController: CropInfoDisplayControllers = gameController.cropInfo
    val cropInfo: CropInfoDisplay by lazy { cropInfoController.cropInfoDisplay }

    fun init(farmArea: FarmArea) {
        this.farmArea = farmArea
    }
}