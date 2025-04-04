package com.midnightcrowing.controllers

import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.scenes.farmScene.FarmScene
import com.midnightcrowing.model.item.Item


class MainMenuController(private val window: Window) {
    init {
        // 游戏初始化方法放在这里
        Item.registerAll()
    }

    fun startGame() {
        window.screen = FarmScene(window)
    }

    fun openOptions() {
        println("打开设置界面")
    }

    fun exitGame() {
        window.exit()
    }
}