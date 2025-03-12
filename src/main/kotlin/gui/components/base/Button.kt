package com.midnightcrowing.gui.components.base

import com.midnightcrowing.gui.Window
import com.midnightcrowing.render.Renderer
import com.midnightcrowing.render.Texture
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.utils.ScreenBounds

enum class ButtonTextures {
    DEFAULT,
    HOVER,
    PRESSED
}

class Button(window: Window) : Widget(window) {
    private val textures = mapOf(
        ButtonTextures.DEFAULT to ResourcesEnum.BUTTON_DEFAULT.path?.let { Texture(it).apply { load() } },
        ButtonTextures.HOVER to ResourcesEnum.BUTTON_HOVER.path?.let { Texture(it).apply { load() } },
        ButtonTextures.PRESSED to ResourcesEnum.BUTTON_PRESSED.path?.let { Texture(it).apply { load() } }
    )

    override val renderer: Renderer
    var text: String = ""

    private var _screenBounds: ScreenBounds = ScreenBounds(0f, 0f, 0f, 0f)

    override val screenLeft: Float get() = _screenBounds.left
    override val screenRight: Float get() = _screenBounds.right
    override val screenTop: Float get() = _screenBounds.top
    override val screenBottom: Float get() = _screenBounds.bottom

    init {
        textures[ButtonTextures.DEFAULT]?.let {
            renderer = Renderer(it)
        } ?: throw IllegalArgumentException("Default texture cannot be null")
    }

    fun render(screenBounds: ScreenBounds) {
        _screenBounds = screenBounds
        super.render()
    }

    override fun onMouseEnter() {
        textures[ButtonTextures.HOVER]?.let { renderer.setTexture(it) }
    }

    override fun onMouseLeave() {
        textures[ButtonTextures.DEFAULT]?.let { renderer.setTexture(it) }
    }
}
