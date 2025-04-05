package com.midnightcrowing.controllers

import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.scenes.farmScene.FarmScene
import com.midnightcrowing.model.item.Items


class MainMenuController(private val window: Window) {
    companion object {
        private var initialized = false

        private fun initialize() {
            if (!initialized) {
                // 游戏初始化方法放在这里
                Items.registerAll()
                initialized = true
            }
        }
    }

    init {
        initialize()
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