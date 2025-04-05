package com.midnightcrowing.controllers

import com.midnightcrowing.gui.publics.Compost

class CompostController(farmController: FarmController) {
    private lateinit var compost: Compost
    val invController: InventoryController by lazy { farmController.inventory }

    fun init(compost: Compost) {
        this.compost = compost
    }
}