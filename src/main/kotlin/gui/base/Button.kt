package com.midnightcrowing.gui.base

import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.NanoVGContext.vg
import com.midnightcrowing.render.TextRenderer
import com.midnightcrowing.render.Texture
import com.midnightcrowing.resource.ColorEnum
import com.midnightcrowing.resource.ResourcesEnum


/**
 * 按钮组件，实现基本的 UI 交互。
 */
class Button : Widget {
    enum class ButtonTextures {
        // 按钮的不同状态
        DEFAULT, HOVER, DISABLED
    }

    constructor(window: Window) : super(window)
    constructor(parent: Widget) : super(parent)

    // 纹理映射，加载对应状态的按钮纹理
    private val textures: Map<ButtonTextures, Texture?> = ButtonTextures.entries.associateWith { type ->
        ResourcesEnum.valueOf("BUTTON_${type.name}").inputStream?.let { Texture(it).apply { load() } }
    }

    // 渲染器，默认使用 DEFAULT 纹理
    override val renderer: ImageRenderer = textures[ButtonTextures.DEFAULT]?.let { ImageRenderer(it) }
        ?: throw IllegalArgumentException("Default texture cannot be null")

    // 文字渲染器
    private val textRenderer = TextRenderer(vg)

    var text: String = ""
        set(value) {
            field = value
            textRenderer.text = value
        }
    var fontSize: Float = 16f
        set(value) {
            field = value
            textRenderer.fontSize = value
        }
    var textColor: FloatArray = ColorEnum.WHITE.value
        set(value) {
            field = value
            textRenderer.textColor = value
        }

    override fun place(bounds: ScreenBounds) {
        super.place(bounds)
        textRenderer.x = (widgetBounds.x1 + widgetBounds.x2) / 2
        textRenderer.y = (widgetBounds.y1 + widgetBounds.y2) / 2
    }

    /**
     * 渲染按钮及其文字。
     */
    override fun render() {
        super.render()
        textRenderer.drawText()
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
        textures[state]?.let { renderer.texture = it }
    }
}