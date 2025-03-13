package com.midnightcrowing.render

import com.midnightcrowing.resource.ColorEnum
import com.midnightcrowing.resource.FontEnum
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.system.MemoryStack

class TextRenderer(private val vg: Long) {
    /**
     * 绘制文本
     * @param text 要绘制的文本
     * @param x X 坐标
     * @param y Y 坐标
     * @param fontSize 字体大小
     * @param fontName 字体名称
     * @param color 颜色（RGBA 格式）
     */
    fun drawText(
        text: String,
        x: Float,
        y: Float,
        fontSize: Float,
        color: FloatArray = ColorEnum.WHITE.value,
        fontName: String = FontEnum.DEFAULT.value,
    ) {
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
