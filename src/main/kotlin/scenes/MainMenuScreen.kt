package com.midnightcrowing.scenes

import com.midnightcrowing.controllers.MainMenuController
import com.midnightcrowing.gui.base.Button
import com.midnightcrowing.gui.base.Screen
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.render.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.utils.ButtonLayout

class MainMenuScreen(window: Window) : Screen(window) {
    override val bgRenderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.MAIN_MENU_BACKGROUND.texture)

    private val controller = MainMenuController(window)
    private val buttonLayout = ButtonLayout()
    private val buttons = listOf(
        Button(window).apply { text = "开始游戏"; textSpacing = 2.0; onClickCallback = { controller.startGame() } },
        Button(window).apply { text = "选项..."; textSpacing = 2.0; onClickCallback = { controller.openOptions() } },
        Button(window).apply { text = "退出游戏"; textSpacing = 2.0; onClickCallback = { controller.exitGame() } }
    )

    init {
        buttonLayout.addButton(1, buttons[0])
        buttonLayout.addButton(2, buttons[1])
        buttonLayout.addButton(2, buttons[2])
    }

    override fun place(width: Int, height: Int) = buttonLayout.place(width, height)

    override fun doRender() = buttonLayout.render()

    override fun cleanup() {
        bgRenderer.texture?.cleanup()
        bgRenderer.cleanup()
        buttonLayout.cleanup()
    }
}