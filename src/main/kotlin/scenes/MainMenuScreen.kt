package com.midnightcrowing.scenes

import com.midnightcrowing.controllers.MainMenuController
import com.midnightcrowing.gui.base.Button
import com.midnightcrowing.gui.base.Screen
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.model.NdcBounds
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.utils.FontSizeUtil.calculateFontSize

class MainMenuScreen(window: Window) : Screen(window) {
    override val bgRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.MAIN_MENU_BACKGROUND.inputStream)
    private val startButton = Button(window).apply { text = "开始游戏"; fontSize = 20f }
    private val optionButton = Button(window).apply { text = "选项..."; fontSize = 20f }
    private val exitButton = Button(window).apply { text = "退出游戏"; fontSize = 20f }

    private val controller = MainMenuController(window)

    init {
        startButton.onClickCallback = { controller.startGame() }
        exitButton.onClickCallback = { controller.exitGame() }
    }

    private companion object {
        const val BTN_WIDTH = 0.86f
        const val BTN_HEIGHT = 0.12f
        const val BTN_GAP_X = 0.04f
        const val BTN_GAP_Y = 0.07f
        const val BTN_OFFSET_Y = 0.24f // 按钮的 Y 轴偏移量
    }

    private fun calculateButtonBounds(
        verticalPos: Float,
        isStartButton: Boolean = false,
        isLeftButton: Boolean = false,
    ): ScreenBounds {
        val left = if (isStartButton || isLeftButton) -BTN_WIDTH / 2 else BTN_GAP_X / 2
        val right = if (isStartButton || !isLeftButton) BTN_WIDTH / 2 else -BTN_GAP_X / 2

        val (top, bottom) = if (isStartButton) {
            BTN_HEIGHT - verticalPos to -verticalPos
        } else {
            verticalPos to verticalPos - BTN_HEIGHT
        }

        return NdcBounds(left, top, right, bottom).toScreenBounds(window)
    }

    override fun place() {
        startButton.place(
            calculateButtonBounds(
                verticalPos = BTN_OFFSET_Y,
                isStartButton = true
            )
        )
        val bottomRowY = -BTN_OFFSET_Y - BTN_GAP_Y
        optionButton.place(calculateButtonBounds(bottomRowY, isLeftButton = true))
        exitButton.place(calculateButtonBounds(bottomRowY))

        val fontSize: Float = calculateFontSize(window.width)
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