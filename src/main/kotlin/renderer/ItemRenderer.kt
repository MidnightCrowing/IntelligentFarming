package com.midnightcrowing.renderer

import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.Item
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_RIGHT

class ItemRenderer(nvg: Long, item: Item) {
    /**
     * 将整数转换为罗马数字
     *
     * @return 罗马数字字符串
     */
    private fun Int.toRoman(): String {
        val values = listOf(
            1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
            100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
            10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I"
        )
        var number = this
        val result = StringBuilder()
        for ((value, symbol) in values) {
            while (number >= value) {
                result.append(symbol)
                number -= value
            }
        }
        return result.toString()
    }

    // 渲染器
    private val textureRenderer: TextureRenderer = TextureRenderer(item.location)
    private val numTextRenderer: TextRenderer = TextRenderer(nvg).apply {
        fontSize = 32.0
        textAlign = NVG_ALIGN_RIGHT or NVG_ALIGN_MIDDLE

        shadowOffsetX = 3.0
        shadowOffsetY = 3.0
    }
    val tooltipRenderer: TooltipRenderer = TooltipRenderer(nvg, item.displayName)

    private var bounds: ScreenBounds = ScreenBounds.EMPTY
    private val itemPadding: Double = 3.0

    init {
        // 补充物品名称提示框的内容
        val contentLines = buildTooltipContent(item)
        if (contentLines.isNotEmpty()) {
            tooltipRenderer.titleColor = doubleArrayOf(84 / 255.0, 252 / 255.0, 252 / 255.0, 1.0) // 亮青色
            tooltipRenderer.contentLines = contentLines
        }
    }

    /**
     * 构建物品名称提示框的内容
     *
     * @param item 物品对象
     * @return 提示框内容列表
     */
    private fun buildTooltipContent(item: Item): List<Pair<String, DoubleArray>> {
        val content = mutableListOf<Pair<String, DoubleArray>>()
        val contentColor = doubleArrayOf(168 / 255.0, 168 / 255.0, 168 / 255.0, 1.0)

        if (item.fortune != 0) {
            content.add("时运${item.fortune.toRoman()}" to contentColor)
        }

        return content
    }

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
}