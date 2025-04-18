package com.midnightcrowing.gui.publics.options

import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.gui.bases.Screen
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.publics.options.OptionMenuEnum.*
import com.midnightcrowing.gui.publics.options.menus.AboutMenu
import com.midnightcrowing.gui.publics.options.menus.AudioMenu
import com.midnightcrowing.gui.publics.options.menus.MainMenu
import com.midnightcrowing.gui.publics.options.menus.VideoMenu
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.RectangleRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.resource.ResourceType
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import kotlin.reflect.KClass

class Options(parent: Screen) : Widget(parent, z = 50) {
    val controller: OptionsController = OptionsController(this)

    private val bgRenderer: TextureRenderer = TextureRenderer(
        ResourceLocation(ResourceType.TE_BACKGROUND, "minecraft", "options.jpg")
    )
    private val bgMaskRenderer: RectangleRenderer = RectangleRenderer(
        color = floatArrayOf(0f, 0f, 0f, 0.53f),
    )
    private var bgBounds: ScreenBounds = ScreenBounds.EMPTY

    private val menus = mutableMapOf<OptionMenuEnum, OptionMenuBase>()

    var activeMenu: OptionMenuBase? = null
        set(value) {
            field?.setHidden(true)
            field = value
            value?.setHidden(false)
            value?.place(widgetBounds)
        }

    init {
        initializeMenus()
    }

    private fun initializeMenus() {
        OptionMenuEnum.entries.forEach { menuEnum ->
            menus[menuEnum] = createMenu(menuEnum).apply { setHidden(true) }
        }
        // 设置默认场景
        controller.switchToMenu(MAIN_OPTIONS)
    }

    private fun createMenu(menuEnum: OptionMenuEnum): OptionMenuBase {
        return when (menuEnum) {
            MAIN_OPTIONS -> MainMenu(this)
            VIDEO_OPTIONS -> VideoMenu(this)
            AUDIO_OPTIONS -> AudioMenu(this)
            ABOUT_OPTIONS -> AboutMenu(this)
        }
    }

    fun switchToMenu(id: OptionMenuEnum) {
        activeMenu = menus[id]
    }

    override fun update() {
        activeMenu?.update()
    }

    override fun containsPoint(x: Double, y: Double, event: KClass<out Event>?): Boolean = true

    override fun onClick(e: MouseClickEvent) {}

    override fun onRightClick(e: MouseRightClickEvent) {}

    override fun onMousePress(e: MousePressedEvent) {}

    override fun onMouseRelease(e: MouseReleasedEvent) {}

    override fun onMouseMove(e: MouseMoveEvent) {}

    override fun onMouseScroll(e: MouseScrollEvent) {}

    override fun onKeyPress(e: KeyPressedEvent): Boolean {
        if (e.key == GLFW_KEY_ESCAPE) {
            controller.backToMenu()
        }
        return false
    }

    fun place(width: Int, height: Int) {
        super.place(0.0, 0.0, width.toDouble(), height.toDouble())

        bgBounds.x2 = width.toDouble()
        bgBounds.y2 = height.toDouble()
        bgMaskRenderer.x2 = width.toDouble()
        bgMaskRenderer.y2 = height.toDouble()

        activeMenu?.place(width, height)
    }

    override fun doRender() {
        bgRenderer.render(bgBounds)
        bgMaskRenderer.render()
        // 渲染当前显示的场景
        activeMenu?.render()
    }
}