package com.midnightcrowing.gui

import com.midnightcrowing.gui.components.base.Button
import com.midnightcrowing.gui.components.base.Screen
import com.midnightcrowing.model.NdcBounds
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.scenes.FarmScene

class MainMenuScreen(window: Window) : Screen(window) {
    private val bgRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.MAIN_MENU_BACKGROUND.inputStream)
    private val startButton: Button = Button(window, "开始游戏", fontSize = 20f)
    private val optionButton: Button = Button(window, "选项...", fontSize = 20f)
    private val exitButton: Button = Button(window, "退出游戏", fontSize = 20f)

    init {
        startButton.onClickCallback = { window.setScreen(FarmScene(window)) }
        exitButton.onClickCallback = { window.exit() }
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

    override fun render() {
        bgRenderer.render(-1f, 1f, 1f, -1f)

        startButton.render(
            calculateButtonBounds(
                verticalPos = BTN_OFFSET_Y,
                isStartButton = true
            )
        )
        val bottomRowY = -BTN_OFFSET_Y - BTN_GAP_Y
        optionButton.render(calculateButtonBounds(bottomRowY, isLeftButton = true))
        exitButton.render(calculateButtonBounds(bottomRowY))

    }

    override fun cleanup() {
        bgRenderer.cleanup()
        startButton.cleanup()
        optionButton.cleanup()
        exitButton.cleanup()
    }
}