package com.midnightcrowing.gui.components.base

import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.gui.Window
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.NanoVGContext.vg
import com.midnightcrowing.render.TextRenderer
import com.midnightcrowing.render.Texture
import com.midnightcrowing.resource.ColorEnum
import com.midnightcrowing.resource.ResourcesEnum

/**
 * 枚举类，定义按钮的不同状态对应的纹理。
 */
enum class ButtonTextures {
    DEFAULT, HOVER, DISABLED
}

/**
 * 按钮组件，实现基本的 UI 交互。
 */
class Button(
    window: Window,
    var text: String = "",
    var fontSize: Float = 16f,
    var textColor: FloatArray = ColorEnum.WHITE.value,
) : Widget(window) {

    // 纹理映射，加载对应状态的按钮纹理
    private val textures: Map<ButtonTextures, Texture?> = ButtonTextures.entries.associateWith { type ->
        ResourcesEnum.valueOf("BUTTON_${type.name}").inputStream?.let { Texture(it).apply { load() } }
    }

    // 渲染器，默认使用 DEFAULT 纹理
    override val renderer: ImageRenderer = textures[ButtonTextures.DEFAULT]?.let { ImageRenderer(it) }
        ?: throw IllegalArgumentException("Default texture cannot be null")

    // 文字渲染器
    private val textRenderer = TextRenderer(vg)

    // 记录当前按钮的屏幕边界
    override var screenBounds = ScreenBounds(0f, 0f, 0f, 0f)

    /**
     * 渲染按钮及其文字。
     */
    fun render(screenBounds: ScreenBounds) {
        this@Button.screenBounds = screenBounds
        super.render()
        drawText()
    }

    /**
     * 绘制按钮文本，使文本居中显示。
     */
    private fun drawText() {
        val centerX = (screenLeft + screenRight) / 2
        val centerY = (screenTop + screenBottom) / 2
        textRenderer.drawText(text, centerX, centerY, fontSize = fontSize, color = textColor)
    }

    /**
     * 处理鼠标进入事件，切换到“悬停”状态。
     */
    override fun onMouseEnter() {
        setTexture(ButtonTextures.HOVER)
    }

    /**
     * 处理鼠标离开事件，恢复为默认状态。
     */
    override fun onMouseLeave() {
        setTexture(ButtonTextures.DEFAULT)
    }

    var onClickCallback: ((e: MouseClickEvent) -> Unit)? = null

    override fun onClick(e: MouseClickEvent) {
        onClickCallback?.invoke(e) // 调用回调函数（如果有）
    }

    /**
     * 设置当前按钮的纹理。
     */
    private fun setTexture(state: ButtonTextures) {
        textures[state]?.let { renderer.setTexture(it) }
    }
}