package com.midnightcrowing.gui.bases

import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.Texture
import com.midnightcrowing.renderer.NineSliceRenderer
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.utils.LayoutScaler.scaleValue


/**
 * 按钮组件，实现基本的 UI 交互。
 */
class Button(parent: Widget) : Widget(parent) {
    enum class ButtonTextures {
        // 按钮的不同状态
        DEFAULT, HOVER, DISABLED
    }

    // 纹理映射，加载对应状态的按钮纹理
    private val textures: Map<ButtonTextures, Texture> = mapOf(
        ButtonTextures.DEFAULT to TextureResourcesEnum.BUTTON_DEFAULT.texture,
        ButtonTextures.HOVER to TextureResourcesEnum.BUTTON_HOVER.texture,
        ButtonTextures.DISABLED to TextureResourcesEnum.BUTTON_DISABLED.texture
    )

    // 渲染器，默认使用 DEFAULT 纹理
    val nineSliceRenderer: NineSliceRenderer? = textures[ButtonTextures.DEFAULT]?.let {
        NineSliceRenderer(it, textureBorder = 4f, vertexBorder = 10f)
    }

    // 文字渲染器
    private val textRenderer = TextRenderer(window.nvg)

    var text: String = ""
        set(value) {
            field = value
            textRenderer.text = value
        }
    var fontSize: Double = 16.0
        set(value) {
            field = value
            textRenderer.fontSize = value
        }
    var textColor: DoubleArray = doubleArrayOf(1.0, 1.0, 1.0, 1.0)
        set(value) {
            field = value
            textRenderer.textColor = value
        }
    var textSpacing: Double = 1.0
        set(value) {
            field = value
            textRenderer.textSpacing = value
        }

    /**
     * 设置按钮的边界位置。
     * @param x1 左上角 X 坐标
     * @param y1 左上角 Y 坐标
     * @param x2 右下角 X 坐标
     * @param y2 右下角 Y 坐标
     */
    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) = this.place(ScreenBounds(x1, y1, x2, y2))

    /**
     * 设置按钮的边界位置。
     * @param bounds 按钮的边界
     */
    override fun place(bounds: ScreenBounds) {
        super.place(bounds)

        val betweenPoint = widgetBounds.between
        textRenderer.x = betweenPoint.x
        textRenderer.y = betweenPoint.y

        nineSliceRenderer?.let { it.vertexBorder = scaleValue(window.width, 10.0, 15.0).toFloat() }
    }

    /**
     * 渲染按钮及其文字。
     */
    override fun doRender() {
        nineSliceRenderer?.render(widgetBounds)
        textRenderer.render()
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
        textures[state]?.let { nineSliceRenderer?.texture = it }
    }
}