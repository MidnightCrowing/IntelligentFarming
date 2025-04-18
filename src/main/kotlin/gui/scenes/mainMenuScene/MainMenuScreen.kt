package com.midnightcrowing.gui.scenes.mainMenuScene

import com.midnightcrowing.audio.AudioResource
import com.midnightcrowing.audio.BackgroundMusicPlayer
import com.midnightcrowing.audio.SoundEvents
import com.midnightcrowing.config.AppConfig
import com.midnightcrowing.gui.bases.Screen
import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.bases.button.Button
import com.midnightcrowing.gui.layouts.ButtonLayout
import com.midnightcrowing.gui.publics.options.Options
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.resource.ResourceType
import com.midnightcrowing.utils.LayoutScaler
import org.lwjgl.nanovg.NanoVG.*

class MainMenuScreen(window: Window) : Screen(window) {
    override val bgRenderer: TextureRenderer = TextureRenderer(
        ResourceLocation(ResourceType.TE_BACKGROUND, "minecraft", "main_menu.jpg")
    )

    private val controller = MainMenuController(this)

    // 游戏标题
    private val titleRenderer = TextureRenderer(
        ResourceLocation(ResourceType.TE_GUI, "minecraft", "title/intellfarm_edition.png")
    )
    private var titleBounds: ScreenBounds = ScreenBounds.EMPTY
    private val splashTextRenderer = TextRenderer(window.nvg).apply {
        text = "Also try Minecraft!"; rotation = -21.0
        textColor = doubleArrayOf(1.0, 1.0, 0.0, 1.0)
        shadowColor = doubleArrayOf(63.0 / 255, 63.0 / 255, 0.0)
    }

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

    // 底部文字
    private val versionRenderer = TextRenderer(window.nvg).apply {
        text = "${AppConfig.APP_NAME} ${AppConfig.VERSION}"
        fontSize = 18.0; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }
    private val licenseRenderer = TextRenderer(window.nvg).apply {
        text = "Java 课设作品 by MidnightCrowing"
        fontSize = 18.0; textAlign = NVG_ALIGN_RIGHT or NVG_ALIGN_MIDDLE
    }

    val options: Options = Options(this)

    init {
        buttonLayout.addButton(1, buttons[0])
        buttonLayout.addButton(2, buttons[1])
        buttonLayout.addButton(2, buttons[2])

        AudioResource.getByEvent(SoundEvents.BACKGROUND_MUSIC)?.random()?.let {
            BackgroundMusicPlayer.play(it)
        }

        options.setHidden(true)
    }

    override fun update() {
        buttonLayout.update()
        options.update()
    }

    override fun place(width: Int, height: Int) {
        super.place(width, height)

        // titleBounds
        titleBounds.y1 = height * 0.15
        titleBounds.y2 = height * 0.37
        titleBounds.x1 = width.toDouble() / 2 - titleBounds.height / 256 * 1024 / 2
        titleBounds.x2 = width.toDouble() / 2 + titleBounds.height / 256 * 1024 / 2

        // splashTextRenderer
        splashTextRenderer.x = titleBounds.x2 - titleBounds.width / 16
        splashTextRenderer.y = titleBounds.y2 - titleBounds.height / 2
        splashTextRenderer.fontSize = LayoutScaler.scaleValue(parentWidth, 27.0, 40.0)

        buttonLayout.place(width, height)

        versionRenderer.x = 5.0
        versionRenderer.y = height - 15.0
        licenseRenderer.x = width - 5.0
        licenseRenderer.y = height - 15.0

        options.place(width, height)
    }

    override fun doRender() {
        titleRenderer.render(titleBounds)
        splashTextRenderer.render()
        buttonLayout.render()
        versionRenderer.render()
        licenseRenderer.render()
        options.render()
    }
}