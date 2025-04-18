package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.farmings.FarmAreaController
import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.publics.compost.CompostController
import com.midnightcrowing.gui.publics.cropInfoDisplay.CropInfoDisplayController
import com.midnightcrowing.gui.publics.hotBar.HotBarController
import com.midnightcrowing.gui.publics.inventory.InventoryController
import com.midnightcrowing.gui.publics.toolTrade.ToolTradeController
import com.midnightcrowing.gui.publics.trade.TradeController
import com.midnightcrowing.gui.scenes.mainMenuScene.MainMenuScreen
import utils.BrowserUtils.openUrl

class FarmController(private val farmScreen: FarmScene) {
    private val window: Window = farmScreen.window

    val cropInfo: CropInfoDisplayController = CropInfoDisplayController(this)
    val farmArea: FarmAreaController = FarmAreaController(this)
    val inventory: InventoryController = InventoryController(this)
    val toolTrade: ToolTradeController = ToolTradeController(this)
    val trade: TradeController = TradeController(this)
    val compost: CompostController = CompostController(this)
    val hotBar: HotBarController = HotBarController(this)

    fun update() {
        farmArea.update()
        compost.update()
    }

    // 回到游戏
    fun backToGame() = farmScreen.escMenus.setHidden(true)

    fun openOptions() = farmScreen.options.setHidden(false)

    fun openAdvancements() {
//        TODO("Not yet implemented")
    }

    fun openStatistics() {
//        TODO("Not yet implemented")
    }

    // 提供反馈
    fun provideFeedback() = openUrl("https://github.com/MidnightCrowing/IntelligentFarming/issues")

    // 退回到标题屏幕
    fun backToTitle() {
        window.screen = MainMenuScreen(window)
    }
}