package com.midnightcrowing.render

import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.system.MemoryStack

class TextRenderer(
    private val nvg: Long,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var text: String = "",
    var fontName: String = "default",
    var fontSize: Double = 16.0,
    var textAlign: Int = NVG_ALIGN_CENTER or NVG_ALIGN_MIDDLE,
    var textColor: DoubleArray = doubleArrayOf(1.0, 1.0, 1.0, 1.0),
    var textOpacity: Double = 1.0,
) {

    /**
     * 绘制文本
     * @param text 要绘制的文本
     */
    fun render(text: String = this.text) {
        MemoryStack.stackPush().use { stack ->
            val colorBuffer = NVGColor.calloc(stack)
            colorBuffer
                .r(textColor[0].toFloat())
                .g(textColor[1].toFloat())
                .b(textColor[2].toFloat())
                .a((textColor[3] * textOpacity).toFloat())

            nvgFontSize(nvg, fontSize.toFloat())
            nvgFontFace(nvg, fontName)
            nvgFillColor(nvg, colorBuffer)
            nvgTextAlign(nvg, textAlign)
            nvgText(nvg, x.toFloat(), y.toFloat(), text)
        }
    }
}
