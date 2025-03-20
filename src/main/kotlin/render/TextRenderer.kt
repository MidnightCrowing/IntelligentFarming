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
    var fontSize: Float = 0f,
    var color: FloatArray = ColorEnum.WHITE.value,
    var fontName: String = FontEnum.DEFAULT.value,
) {
    /**
     * 绘制文本
     * @param text 要绘制的文本
     */
    fun drawText(text: String = this.text) {
        MemoryStack.stackPush().use { stack ->
            val colorBuffer = NVGColor.calloc(stack)
            colorBuffer.r(color[0]).g(color[1]).b(color[2]).a(color[3])

            nvgFontSize(vg, fontSize)
            nvgFontFace(vg, fontName)
            nvgFillColor(vg, colorBuffer)
            nvgTextAlign(vg, NVG_ALIGN_CENTER or NVG_ALIGN_MIDDLE)
            nvgText(vg, x, y, text)
        }
    }
}
