package com.midnightcrowing.renderer

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.opengl.GL46.*
import org.lwjgl.system.MemoryStack

class TextRenderer(private val nvg: Long) {
    companion object {
        fun createTextRendererForGUI(nvg: Long): TextRenderer {
            return TextRenderer(nvg).apply {
                fontSize = 24.0
                textColor = doubleArrayOf(63.0 / 255, 63.0 / 255, 63.0 / 255, 1.0)
                shadow = false
            }
        }
    }

    var x: Double = 0.0
    var y: Double = 0.0
    var text: String = ""
    var fontName: String = "unifont"
    var fontSize: Double = 16.0
    var fontBlur: Double = 0.0
    var textAlign: Int = NVG_ALIGN_CENTER or NVG_ALIGN_MIDDLE
    var textColor: DoubleArray = doubleArrayOf(1.0, 1.0, 1.0, 1.0)
    var textOpacity: Double = 1.0
    var textSpacing: Double = 1.0
    var rotation: Double = 0.0
    var scale: Double = 1.0

    var shadow: Boolean = true
    var shadowColor: DoubleArray = doubleArrayOf(62.0 / 255, 62.0 / 255, 62.0 / 255)
    var shadowOffsetX: Double = 2.0
    var shadowOffsetY: Double = 2.0

    private fun setColor(stack: MemoryStack, red: Float, green: Float, blue: Float, alpha: Float): NVGColor {
        return NVGColor.calloc(stack).apply {
            r(red)
            g(green)
            b(blue)
            a(alpha)
        }
    }

    private fun renderTextWithOffset(stack: MemoryStack, text: String) {
        val a = (textColor[3] * textOpacity).toFloat()

        if (shadow) {
            setColor(stack, shadowColor[0].toFloat(), shadowColor[1].toFloat(), shadowColor[2].toFloat(), a).also {
                nvgFillColor(nvg, it)
                nvgText(nvg, (x + shadowOffsetX).toFloat(), (y + shadowOffsetY).toFloat(), text)
            }
        }

        setColor(stack, textColor[0].toFloat(), textColor[1].toFloat(), textColor[2].toFloat(), a).also {
            nvgFillColor(nvg, it)
            nvgText(nvg, x.toFloat(), y.toFloat(), text)
        }
    }

    /**
     * 绘制文本
     * @param text 要绘制的文本
     */
    fun render(text: String = this.text) {
        MemoryStack.stackPush().use { stack ->
            nvgSave(nvg) // 保存当前状态
            nvgTranslate(nvg, x.toFloat(), y.toFloat())       // 平移到指定位置
            nvgRotate(nvg, Math.toRadians(rotation).toFloat()) // 旋转文本
            nvgScale(nvg, scale.toFloat(), scale.toFloat())   // 缩放文本
            nvgTranslate(nvg, -x.toFloat(), -y.toFloat())     // 平移回原位置

            nvgFontBlur(nvg, fontBlur.toFloat())                // 设置字体模糊
            nvgTextLetterSpacing(nvg, textSpacing.toFloat()) // 设置字母间距
            nvgFontSize(nvg, fontSize.toFloat())                // 设置字体大小
            nvgFontFace(nvg, fontName)                         // 设置字体
            nvgTextAlign(nvg, textAlign)                              // 设置文本对齐

            renderTextWithOffset(stack, text)                               // 绘制带阴影的文本

            nvgRestore(nvg) // 恢复之前保存的状态
        }

        nvgEndFrame(nvg)
        glEnable(GL_BLEND)  // 确保透明度正常
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }
}
