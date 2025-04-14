package com.midnightcrowing.gui.bases.button

import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.Texture
import com.midnightcrowing.renderer.NineSliceRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.utils.LayoutScaler.scaleValue


/**
 * 按钮组件，实现基本的 UI 交互。
 */
open class Button(parent: Widget) : AbstractButton(parent) {
    enum class ButtonTextures {
        // 按钮的不同状态
        DEFAULT, HOVER, DISABLED
    }

    private var lastIsVisible: Boolean = true
    override val isVisible: Boolean
        get() {
            val value = super.isVisible
            if (value != lastIsVisible) {
                lastIsVisible = value
                onVisibilityChanged(value)
            }
            return value
        }

    // 纹理映射，加载对应状态的按钮纹理
    private val textures: Map<ButtonTextures, Texture> = mapOf(
        ButtonTextures.DEFAULT to TextureResourcesEnum.GUI_BUTTON_DEFAULT.texture,
        ButtonTextures.HOVER to TextureResourcesEnum.GUI_BUTTON_HOVER.texture,
        ButtonTextures.DISABLED to TextureResourcesEnum.GUI_BUTTON_DISABLED.texture
    )

    // 渲染器，默认使用 DEFAULT 纹理
    val nineSliceRenderer: NineSliceRenderer? = textures[ButtonTextures.DEFAULT]?.let {
        NineSliceRenderer(it, textureBorder = 4f, vertexBorder = 10f)
    }

    var isHover: Boolean = false  // 是否悬停
    var isSelect: Boolean = false // 是否被选中

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
        nineSliceRenderer?.let { it.vertexBorder = scaleValue(parentWidth, 10.0, 15.0).toFloat() }
    }

    /**
     * 渲染按钮及其文字。
     */
    override fun doRender() {
        nineSliceRenderer?.render(widgetBounds)
        super.doRender()
    }

    /**
     * 处理鼠标进入事件，切换到“悬停”状态。
     */
    override fun onMouseEnter() {
        isHover = true
    }

    /**
     * 处理鼠标离开事件，恢复为默认状态。
     */
    override fun onMouseLeave() {
        isHover = false
    }

    private fun onVisibilityChanged(visible: Boolean) {
        if (visible) {
            val (x, y) = getCursorPos()
            if (containsPoint(x, y)) onMouseEnter()
        } else {
            onMouseLeave()
        }
    }

    override fun update() {
        setTexture(if (isHover || isSelect) ButtonTextures.HOVER else ButtonTextures.DEFAULT)
    }

    var onClickCallback: ((e: MouseClickEvent) -> Unit)? = null

    override fun onClick(e: MouseClickEvent) {
        super.onClick(e)
        onClickCallback?.invoke(e) // 调用回调函数（如果有）
    }

    /**
     * 设置当前按钮的纹理。
     */
    private fun setTexture(state: ButtonTextures) {
        textures[state]?.let { nineSliceRenderer?.texture = it }
    }
}