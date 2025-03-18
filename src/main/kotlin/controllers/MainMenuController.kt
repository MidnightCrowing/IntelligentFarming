package com.midnightcrowing.controllers

import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.scenes.FarmScene


class MainMenuController(private val window: Window) {
    fun startGame() {
        window.screen = FarmScene(window)
    }

    fun openSettings() {
        println("打开设置界面")
        // 切换到 SettingsScreen
    }

    fun exitGame() {
        window.exit()
    }
}