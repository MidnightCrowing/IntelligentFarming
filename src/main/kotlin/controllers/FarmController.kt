package com.midnightcrowing.controllers

import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.scenes.MainMenuScreen
import com.midnightcrowing.gui.scenes.farmScene.FarmScene
import java.awt.Desktop
import java.net.URI

class FarmController(private val farmScreen: FarmScene) {
    private val window: Window = farmScreen.window

    val cropInfo: CropInfoDisplayControllers = CropInfoDisplayControllers()
    val farmArea: FarmAreaController = FarmAreaController(this)
    val inventory: InventoryController = InventoryController(this)
    val hotBar: HotBarController = HotBarController(this)

    fun update() {
        farmArea.update()
    }

    private fun openUrl(url: String) {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(URI(url))
            }
        } else {
            println("Desktop is not supported on this system.")
        }
    }

    // 回到游戏
    fun backToGame() {
        farmScreen.escMenus.setHidden(true)
    }

    fun openOptions() {
        println("打开设置界面")
    }

    // 提供反馈
    fun provideFeedback() = openUrl("https://github.com/MidnightCrowing/IntelligentFarming/issues")

    // 退回到标题屏幕
    fun backToTitle() {
        window.screen = MainMenuScreen(window)
    }
}