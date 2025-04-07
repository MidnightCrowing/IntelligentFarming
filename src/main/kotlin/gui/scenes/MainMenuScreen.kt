package com.midnightcrowing.gui.scenes

import com.midnightcrowing.controllers.MainMenuController
import com.midnightcrowing.gui.bases.Button
import com.midnightcrowing.gui.bases.Screen
import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.layouts.ButtonLayout
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum

class MainMenuScreen(window: Window) : Screen(window) {
    override val bgRenderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.BG_MAIN_MENU_BACKGROUND.texture)

    private val controller = MainMenuController(window)

    // 游戏标题
    private val titleRenderer = TextureRenderer(TextureResourcesEnum.TITLE_INTELLFARM.texture)
    private var titleBounds: ScreenBounds = ScreenBounds.EMPTY

    // 菜单按钮
    private val buttonLayout = ButtonLayout(this, offsetY = 0.14)
    private val buttons = listOf(
        Button(buttonLayout).apply {
            text = "开始游戏"; textSpacing = 2.0; onClickCallback = { controller.startGame() }
        },
        Button(buttonLayout).apply {
            text = "选项…"; textSpacing = 2.0; onClickCallback = { controller.openOptions() }
        },
        Button(buttonLayout).apply {
            text = "退出游戏"; textSpacing = 2.0; onClickCallback = { controller.exitGame() }
        }
    )

    init {
        buttonLayout.addButton(1, buttons[0])
        buttonLayout.addButton(2, buttons[1])
        buttonLayout.addButton(2, buttons[2])
    }

    override fun place(width: Int, height: Int) {
        super.place(width, height)
        titleBounds.y1 = height * 0.15
        titleBounds.y2 = height * 0.30
        titleBounds.x1 = width.toDouble() / 2 - titleBounds.height / 158 * 1024 / 2
        titleBounds.x2 = width.toDouble() / 2 + titleBounds.height / 158 * 1024 / 2
        buttonLayout.place(width, height)
    }

    override fun update() = buttonLayout.update()

    override fun doRender() {
        titleRenderer.render(titleBounds)
        buttonLayout.render()
    }

    override fun doCleanup() {
        bgRenderer.cleanup()
        titleRenderer.cleanup()
        buttonLayout.cleanup()
    }
}