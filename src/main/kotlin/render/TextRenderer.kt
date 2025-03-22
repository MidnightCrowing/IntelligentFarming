package com.midnightcrowing.render

import com.midnightcrowing.resource.ColorEnum
import com.midnightcrowing.resource.FontEnum
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.system.MemoryStack

class TextRenderer(
    private val vg: Long,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var text: String = "",
    var fontName: String = FontEnum.DEFAULT.value,
    var fontSize: Double = 16.0,
    var textAlign: Int = NVG_ALIGN_CENTER or NVG_ALIGN_MIDDLE,
    var textColor: DoubleArray = ColorEnum.WHITE.value,
    var textOpacity: Double = 1.0,
) {

    /**
     * 绘制文本
     * @param text 要绘制的文本
     */
    fun drawText(text: String = this.text) {
        MemoryStack.stackPush().use { stack ->
            val colorBuffer = NVGColor.calloc(stack)
            colorBuffer
                .r(textColor[0].toFloat())
                .g(textColor[1].toFloat())
                .b(textColor[2].toFloat())
                .a((textColor[3] * textOpacity).toFloat())

            nvgFontSize(vg, fontSize.toFloat())
            nvgFontFace(vg, fontName)
            nvgFillColor(vg, colorBuffer)
            nvgTextAlign(vg, textAlign)
            nvgText(vg, x.toFloat(), y.toFloat(), text)
        }
    }
}
