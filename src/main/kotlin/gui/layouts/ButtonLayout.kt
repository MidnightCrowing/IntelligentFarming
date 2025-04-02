package com.midnightcrowing.gui.layouts

import com.midnightcrowing.gui.bases.Button
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.utils.LayoutScaler

class ButtonLayout(
    parent: Widget,
    z: Int? = null,
    var width: Double = 0.375,     // 整体宽度(相对于窗口宽度)
    var offsetY: Double = 0.0,    // 整体Y轴偏移量(向下偏移)
    var btnHeight: Double = 0.06,
    var btnGapX: Double = 0.02,
    var btnGapY: Double = 0.035,
) : Widget(parent, z) {
    private var buttons = mutableMapOf<Int, MutableList<Button>>()

    /**
     * 添加按钮
     * @param row 行号
     * @param button 按钮
     */
    fun addButton(row: Int, button: Button) {
        buttons.getOrPut(row) { mutableListOf() }.add(button)
    }

    fun place(windowWidth: Int, windowHeight: Int) {
        val btnWidth = windowWidth * width
        val btnHeight = windowHeight * btnHeight
        val btnStartX = (windowWidth - btnWidth) / 2
        val btnStartY = windowHeight * (1 + offsetY) / 2
        val btnGapX = windowWidth * btnGapX
        val btnGapY = windowHeight * btnGapY

        // 计算每个按钮的位置
        buttons.forEach { (row, buttonArray) ->
            val rowY = btnStartY + (row - 1) * (btnHeight + btnGapY)
            val btnsWidth = (btnWidth - (buttonArray.size - 1) * btnGapX) / buttonArray.size
            buttonArray.forEachIndexed { index, button ->
                val colX = btnStartX + index * (btnsWidth + btnGapX)
                button.place(colX, rowY, colX + btnsWidth, rowY + btnHeight)
            }
        }

        // 调整按钮字体大小
        val fontSize: Double = LayoutScaler.scaleValue(windowWidth)
        buttons.values.flatten().forEach { it.fontSize = fontSize }
    }

    override fun render() = buttons.values.flatten().forEach { it.render() }

    override fun doCleanup() = buttons.values.flatten().forEach { it.cleanup() }
}