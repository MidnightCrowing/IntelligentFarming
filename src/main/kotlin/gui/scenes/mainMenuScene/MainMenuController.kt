package com.midnightcrowing.gui.scenes.mainMenuScene

import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.scenes.farmScene.FarmScene
import com.midnightcrowing.model.item.Items

class MainMenuController(private val mainMenuScene: MainMenuScreen) {
    private val window: Window = mainMenuScene.window

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
        mainMenuScene.options.setHidden(false)
    }

    fun exitGame() {
        window.exit()
    }
}