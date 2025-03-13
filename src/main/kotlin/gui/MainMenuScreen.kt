package com.midnightcrowing.gui

import com.midnightcrowing.gui.components.base.AbstractWidget
import com.midnightcrowing.gui.components.base.Button
import com.midnightcrowing.gui.components.hotbar.HotBar
import com.midnightcrowing.gui.components.inventory.Inventory
import com.midnightcrowing.render.Renderer
import com.midnightcrowing.render.createRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.utils.CoordinateConversionUtils
import com.midnightcrowing.utils.ScreenBounds

class MainMenuScreen(window: Window) : AbstractWidget(window) {
    private val bgRenderer: Renderer = createRenderer(ResourcesEnum.MAIN_MENU_BACKGROUND.inputStream)
    private val hotBar: HotBar = HotBar(window)
    private val inventory: Inventory = Inventory(window)
    private val startButton: Button = Button(window, "开始游戏", fontSize = 20f)
    private val optionButton: Button = Button(window, "选项...", fontSize = 20f)
    private val exitButton: Button = Button(window, "退出游戏", fontSize = 20f)

    private val wheatRenderer: Renderer = createRenderer(ResourcesEnum.WHEAT.inputStream)
    private val cabbageRenderer: Renderer = createRenderer(ResourcesEnum.CABBAGE.inputStream)
    private val carrotRenderer: Renderer = createRenderer(ResourcesEnum.CARROT.inputStream)
    private val potatoRenderer: Renderer = createRenderer(ResourcesEnum.POTATO.inputStream)
    private val tomatoRenderer: Renderer = createRenderer(ResourcesEnum.TOMATO.inputStream)

    init {
        startButton.onClickCallback = { event ->
            println("按钮被点击了！点击位置：(${event.x}, ${event.y})")
        }
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

        return CoordinateConversionUtils.convertNdcToScreenBounds(window, left, top, right, bottom)
    }

    override fun render() {
        bgRenderer.render(-1f, 1f, 1f, -1f)
        hotBar.render()
//        inventory.render()

        startButton.render(
            calculateButtonBounds(
                verticalPos = BTN_OFFSET_Y,
                isStartButton = true
            )
        )
        val bottomRowY = -BTN_OFFSET_Y - BTN_GAP_Y
        optionButton.render(calculateButtonBounds(bottomRowY, isLeftButton = true))
        exitButton.render(calculateButtonBounds(bottomRowY))

        wheatRenderer.render(CoordinateConversionUtils.convertScreenToNdcBounds(window, hotBar.getGridBounds(5)))
        cabbageRenderer.render(CoordinateConversionUtils.convertScreenToNdcBounds(window, hotBar.getGridBounds(6)))
        carrotRenderer.render(CoordinateConversionUtils.convertScreenToNdcBounds(window, hotBar.getGridBounds(7)))
        potatoRenderer.render(CoordinateConversionUtils.convertScreenToNdcBounds(window, hotBar.getGridBounds(8)))
        tomatoRenderer.render(CoordinateConversionUtils.convertScreenToNdcBounds(window, hotBar.getGridBounds(9)))
    }

    override fun cleanup() {
        bgRenderer.cleanup()
        hotBar.cleanup()
        inventory.cleanup()
        startButton.cleanup()
        optionButton.cleanup()
        exitButton.cleanup()

        wheatRenderer.cleanup()
        cabbageRenderer.cleanup()
        carrotRenderer.cleanup()
        potatoRenderer.cleanup()
        tomatoRenderer.cleanup()
    }
}