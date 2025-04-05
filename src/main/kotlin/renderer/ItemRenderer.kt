package com.midnightcrowing.renderer

import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.Item
import org.lwjgl.nanovg.NanoVG

class ItemRenderer(nvg: Long, item: Item) {
    private val textureRenderer: TextureRenderer = TextureRenderer(item.textureEnum.texture)
    private val numTextRenderer: TextRenderer = TextRenderer(nvg).apply {
        fontSize = 32.0
        textAlign = NanoVG.NVG_ALIGN_RIGHT or NanoVG.NVG_ALIGN_MIDDLE

        shadowOffsetX = 3.0
        shadowOffsetY = 3.0
    }
    val tooltipRenderer: TooltipRenderer = TooltipRenderer(nvg, item.name)

    private var bounds: ScreenBounds = ScreenBounds.EMPTY
    private val itemPadding: Double = 3.0

    fun place(bounds: ScreenBounds) = this.place(bounds.x1, bounds.y1, bounds.x2, bounds.y2)

    fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        this.bounds = ScreenBounds(
            x1 + itemPadding, y1 + itemPadding,
            x2 - itemPadding, y2 - itemPadding
        )
        numTextRenderer.x = x2 + 3
        numTextRenderer.y = y2 - 10
    }

    fun render(num: Int = 1) {
        textureRenderer.render(bounds)
        if (num != 1) {
            numTextRenderer.render(num.toString())
        }
        if (num <= 0) {
            System.err.println("$this 警告: num值: $num 不在预期范围内")
            numTextRenderer.textColor = doubleArrayOf(1.0, 0.0, 0.0, 1.0)
        }
    }

    /**
     * 渲染物品名称提示框
     *
     * @param mouseX 鼠标X坐标
     * @param mouseY 鼠标Y坐标
     * @param position 提示框位置 （`center`, `above`, `after-top`, `after`, `after-bottom`, `below`, `before-top`, `before`, `before-bottom`）
     */
    fun renderTooltip(
        mouseX: Double,
        mouseY: Double,
        position: String = "after-top",
    ) {
        tooltipRenderer.position = position
        tooltipRenderer.render(mouseX, mouseY)
    }

    fun cleanup() = textureRenderer.cleanup()
}