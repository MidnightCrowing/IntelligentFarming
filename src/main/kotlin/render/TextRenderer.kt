package com.midnightcrowing.render

import com.midnightcrowing.resource.ColorEnum
import com.midnightcrowing.resource.FontEnum
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.system.MemoryStack

class TextRenderer(
    private val vg: Long,
    var x: Float = 0f,
    var y: Float = 0f,
    var text: String = "",
    var fontName: String = FontEnum.DEFAULT.value,
    var fontSize: Float = 16f,
    var textAlign: Int = NVG_ALIGN_CENTER or NVG_ALIGN_MIDDLE,
    var textColor: FloatArray = ColorEnum.WHITE.value,
    var textOpacity: Float = 1.0f,
) {

    /**
     * 绘制文本
     * @param text 要绘制的文本
     */
    fun drawText(text: String = this.text) {
        MemoryStack.stackPush().use { stack ->
            val colorBuffer = NVGColor.calloc(stack)
            colorBuffer.r(textColor[0]).g(textColor[1]).b(textColor[2]).a(textColor[3] * textOpacity)

            nvgFontSize(vg, fontSize)
            nvgFontFace(vg, fontName)
            nvgFillColor(vg, colorBuffer)
            nvgTextAlign(vg, textAlign)
            nvgText(vg, x, y, text)
        }
    }
}
