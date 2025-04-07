package com.midnightcrowing.renderer

import org.lwjgl.nanovg.NanoVG
import org.lwjgl.system.MemoryStack


/**
 * 渲染文字提示框
 *
 * @param nvg NanoVG上下文
 * @param text 文本内容
 * @param position 提示框位置 （`center`, `above`, `after-top`, `after`, `after-bottom`, `below`, `before-top`, `before`, `before-bottom`）
 * @param fontSize 字体大小
 * @param padding 提示框内边距
 * @param borderWidth 边框宽度
 * @param opacity 提示框透明度
 * @param bgColor 提示框背景颜色
 * @param borderColor 边框颜色
 */
class TooltipRenderer(
    private val nvg: Long,
    var text: String, // 作为标题使用
    var contentLines: List<Pair<String, DoubleArray>> = listOf(), // 多行内容
    var titleColor: DoubleArray = doubleArrayOf(1.0, 1.0, 1.0, 1.0), // 标题颜色
    var position: String = "after-top",
    var fontSize: Double = 25.0,
    var lineSpacing: Double = 8.0,
    var padding: Int = 6,
    var borderWidth: Double = 4.0,
    var opacity: Double = 0.95,
    var bgColor: FloatArray = floatArrayOf(22 / 255f, 8 / 255f, 22 / 255f, opacity.toFloat()),
    var borderColor: FloatArray = floatArrayOf(37 / 255f, 4 / 255f, 92 / 255f, opacity.toFloat()),
) {
    // 用于存储边框参数的数据类
    private data class ItemNameBorderSegment(
        val x1: Double,
        val y1: Double,
        val x2: Double,
        val y2: Double,
        val color: FloatArray,
        val width: Double,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ItemNameBorderSegment

            if (x1 != other.x1) return false
            if (y1 != other.y1) return false
            if (x2 != other.x2) return false
            if (y2 != other.y2) return false
            if (width != other.width) return false
            if (!color.contentEquals(other.color)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x1.hashCode()
            result = 31 * result + y1.hashCode()
            result = 31 * result + x2.hashCode()
            result = 31 * result + y2.hashCode()
            result = 31 * result + width.hashCode()
            result = 31 * result + color.contentHashCode()
            return result
        }
    }

    // 物品名称渲染器
    private val nameTextRenderer = TextRenderer(nvg).apply {
        textAlign = NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_MIDDLE
    }
    private val rectangleRenderer = RectangleRenderer()
    private val lineRenderers = LineRenderer() // 边框线

    fun render(mouseX: Double, mouseY: Double) {
        MemoryStack.stackPush().use { stack ->
            val allLines = buildList {
                if (text.isNotEmpty()) add(text to titleColor)
                addAll(contentLines)
            }

            NanoVG.nvgFontSize(nvg, fontSize.toFloat())
            val textBounds = stack.mallocFloat(4)
            var maxWidth = 0f
            var textHeight = 0f
            var totalHeight = 0.0

            for ((line, _) in allLines) {
                NanoVG.nvgTextBounds(nvg, 0f, 0f, line, textBounds)
                val lineWidth = textBounds[2] - textBounds[0]
                val lineHeight = textBounds[3] - textBounds[1]
                maxWidth = maxOf(maxWidth, lineWidth)
                textHeight = maxOf(textHeight, lineHeight)
                totalHeight += lineHeight + lineSpacing
            }
            totalHeight -= lineSpacing // 去掉最后一行的空隙

            val textWidth = maxWidth

            // 3. 计算起始坐标
            val (startX, startY) = when (position) {
                "center" -> mouseX to mouseY
                "above" -> mouseX - textWidth / 2 to mouseY - 40
                "after-top" -> mouseX + 30 to mouseY - 25
                "after" -> mouseX + 30 to mouseY
                "after-bottom" -> mouseX + 30 to mouseY + 35
                "below" -> mouseX - textWidth / 2 to mouseY + 50
                "before-top" -> mouseX - textWidth - 30 to mouseY - 25
                "before" -> mouseX - textWidth - 30 to mouseY
                "before-bottom" -> mouseX - textWidth - 30 to mouseY + 35
                else -> {
                    System.err.println("$position 位置不在预期范围内")
                    mouseX to mouseY
                }
            }

            // 4. 计算背景框坐标
            val bgLeft = startX - padding
            val bgRight = startX + textWidth + padding
            val bgTop = startY - (textHeight / 2) - padding
            val bgBottom = startY + totalHeight - (textHeight / 2) + padding

            // 5. 渲染背景
            rectangleRenderer.apply {
                x1 = bgLeft
                y1 = bgTop
                x2 = bgRight
                y2 = bgBottom
                color = bgColor
            }.render()

            // 6. 定义所有边框配置
            val borders = listOf(
                // 顶部双边框
                ItemNameBorderSegment(
                    bgLeft - borderWidth, bgTop - borderWidth / 2,
                    bgRight + borderWidth, bgTop - borderWidth / 2,
                    borderColor, borderWidth
                ),
                ItemNameBorderSegment(
                    bgLeft - borderWidth, bgTop - borderWidth * 3 / 2,
                    bgRight + borderWidth, bgTop - borderWidth * 3 / 2,
                    bgColor, borderWidth
                ),
                // 底部双边框
                ItemNameBorderSegment(
                    bgLeft - borderWidth, bgBottom + borderWidth / 2,
                    bgRight + borderWidth, bgBottom + borderWidth / 2,
                    borderColor, borderWidth
                ),
                ItemNameBorderSegment(
                    bgLeft - borderWidth, bgBottom + borderWidth * 3 / 2,
                    bgRight + borderWidth, bgBottom + borderWidth * 3 / 2,
                    bgColor, borderWidth
                ),
                // 左侧双边框
                ItemNameBorderSegment(
                    bgLeft - borderWidth / 2, bgTop - borderWidth,
                    bgLeft - borderWidth / 2, bgBottom + borderWidth,
                    borderColor, borderWidth
                ),
                ItemNameBorderSegment(
                    bgLeft - borderWidth * 3 / 2, bgTop - borderWidth,
                    bgLeft - borderWidth * 3 / 2, bgBottom + borderWidth,
                    bgColor, borderWidth
                ),
                // 右侧双边框
                ItemNameBorderSegment(
                    bgRight + borderWidth / 2, bgTop - borderWidth,
                    bgRight + borderWidth / 2, bgBottom + borderWidth,
                    borderColor, borderWidth
                ),
                ItemNameBorderSegment(
                    bgRight + borderWidth * 3 / 2, bgTop - borderWidth,
                    bgRight + borderWidth * 3 / 2, bgBottom + borderWidth,
                    bgColor, borderWidth
                )
            )

            // 7. 批量渲染边框
            borders.forEachIndexed { index, config ->
                lineRenderers.apply {
                    x1 = config.x1
                    y1 = config.y1
                    x2 = config.x2
                    y2 = config.y2
                    width = config.width
                    color = config.color
                    render()
                }
            }

            // 渲染每一行文本
            var drawY = startY
            for ((line, color) in allLines) {
                nameTextRenderer.apply {
                    this.x = startX
                    this.y = drawY
                    this.fontSize = this@TooltipRenderer.fontSize
                    this.text = line
                    this.textColor = color
                }.render()

                drawY += fontSize + lineSpacing
            }
        }
    }
}