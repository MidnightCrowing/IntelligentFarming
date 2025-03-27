package com.midnightcrowing.scenes

import com.midnightcrowing.controllers.MainMenuController
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.base.Button
import com.midnightcrowing.gui.base.Screen
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.model.NdcBounds
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.render.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.utils.LayoutScaler.scaleValue

class MainMenuScreen(window: Window) : Screen(window) {
    override val bgRenderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.MAIN_MENU_BACKGROUND.texture)
    private val startButton = Button(window).apply { text = "开始游戏"; fontSize = 20.0; textSpacing = 2.0 }
    private val optionButton = Button(window).apply { text = "选项..."; fontSize = 20.0; textSpacing = 2.0 }
    private val exitButton = Button(window).apply { text = "退出游戏"; fontSize = 20.0; textSpacing = 2.0 }

    private val controller = MainMenuController(window)

    init {
        startButton.onClickCallback = { controller.startGame() }
        exitButton.onClickCallback = { controller.exitGame() }

        registerItems()
    }

    private companion object {
        const val BTN_WIDTH = 0.75
        const val BTN_HEIGHT = 0.12
        const val BTN_GAP_X = 0.04
        const val BTN_GAP_Y = 0.07
        const val BTN_OFFSET_Y = 0.24 // 按钮的 Y 轴偏移量
    }

    private fun registerItems() {
        ItemRegistry.register(FarmItems.CabbageItem.id) { parent -> FarmItems.CabbageItem(parent) }
        ItemRegistry.register(FarmItems.CabbageSeedItem.id) { parent -> FarmItems.CabbageSeedItem(parent) }
        ItemRegistry.register(FarmItems.CarrotItem.id) { parent -> FarmItems.CarrotItem(parent) }
        ItemRegistry.register(FarmItems.CornItem.id) { parent -> FarmItems.CornItem(parent) }
        ItemRegistry.register(FarmItems.CornSeedItem.id) { parent -> FarmItems.CornSeedItem(parent) }
        ItemRegistry.register(FarmItems.CottonItem.id) { parent -> FarmItems.CottonItem(parent) }
        ItemRegistry.register(FarmItems.CottonSeedItem.id) { parent -> FarmItems.CottonSeedItem(parent) }
        ItemRegistry.register(FarmItems.OnionItem.id) { parent -> FarmItems.OnionItem(parent) }
        ItemRegistry.register(FarmItems.PotatoItem.id) { parent -> FarmItems.PotatoItem(parent) }
        ItemRegistry.register(FarmItems.TomatoItem.id) { parent -> FarmItems.TomatoItem(parent) }
        ItemRegistry.register(FarmItems.TomatoSeedItem.id) { parent -> FarmItems.TomatoSeedItem(parent) }
        ItemRegistry.register(FarmItems.WheatItem.id) { parent -> FarmItems.WheatItem(parent) }
        ItemRegistry.register(FarmItems.WheatSeedItem.id) { parent -> FarmItems.WheatSeedItem(parent) }
    }

    private fun calculateButtonBounds(
        verticalPos: Double,
        isBigButton: Boolean = false,
        isLeftButton: Boolean = false,
    ): ScreenBounds {
        val left = if (isBigButton || isLeftButton) -BTN_WIDTH / 2 else BTN_GAP_X / 2
        val right = if (isBigButton || !isLeftButton) BTN_WIDTH / 2 else -BTN_GAP_X / 2

        val (top, bottom) = if (isBigButton) {
            BTN_HEIGHT - verticalPos to -verticalPos
        } else {
            verticalPos to verticalPos - BTN_HEIGHT
        }

        return NdcBounds(left, top, right, bottom).toScreenBounds(window)
    }

    override fun place(width: Int, height: Int) {
        startButton.place(
            calculateButtonBounds(
                verticalPos = BTN_OFFSET_Y,
                isBigButton = true
            )
        )
        val bottomRowY = -BTN_OFFSET_Y - BTN_GAP_Y
        optionButton.place(calculateButtonBounds(bottomRowY, isLeftButton = true))
        exitButton.place(calculateButtonBounds(bottomRowY))

        val fontSize: Double = scaleValue(window.width)
        startButton.fontSize = fontSize
        optionButton.fontSize = fontSize
        exitButton.fontSize = fontSize
    }

    override fun render() {
        super.render()
        startButton.render()
        optionButton.render()
        exitButton.render()
    }

    override fun cleanup() {
        bgRenderer.cleanup()
        startButton.cleanup()
        optionButton.cleanup()
        exitButton.cleanup()
    }
}