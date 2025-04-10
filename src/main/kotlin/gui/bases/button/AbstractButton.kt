package com.midnightcrowing.gui.bases.button

import com.midnightcrowing.audio.SoundEffectPlayer
import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.TextRenderer

open class AbstractButton(parent: Widget) : Widget(parent) {
    // 文字渲染器
    private val textRenderer = TextRenderer(window.nvg)

    var soundEffect: String = "ui.button.click"

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
     * @param bounds 按钮的边界
     */
    override fun place(bounds: ScreenBounds) {
        super.place(bounds)

        val betweenPoint = widgetBounds.between
        textRenderer.x = betweenPoint.x
        textRenderer.y = betweenPoint.y
    }

    /**
     * 渲染按钮及其文字。
     */
    override fun doRender() {
        textRenderer.render()
    }

    override fun onClick(e: MouseClickEvent) {
        SoundEffectPlayer.play(soundEffect)
    }
}