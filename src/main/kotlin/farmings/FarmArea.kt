package com.midnightcrowing.farmings

import com.midnightcrowing.events.CustomEvent.MouseMoveEvent
import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds


class FarmArea(
    window: Window,
    private val farmlandBoard: List<Int>,
) : Widget(window) {
    private val rowCount: Int = farmlandBoard.maxOf { Integer.toBinaryString(it).length }
    private val columnCount: Int = farmlandBoard.size

    private var middlePointX: Float = 0f
    private var middlePointY: Float = 0f
    private var leftPointX: Float = 0f
    private var leftPointY: Float = 0f
    private var rightPointX: Float = 0f
    private var rightPointY: Float = 0f
    private var blockHeight: Float = 0f
    private var blockDeep: Float = 0f
    private var blockLeftWidth: Float = 0f
    private var blockLeftHeight: Float = 0f
    private var blockRightWidth: Float = 0f
    private var blockRightHeight: Float = 0f


    private val wheat = Wheat(window)
    private val carrot = Carrot(window)
    private val potato = Potato(window)

    // 判断 (x, y) 是否可用
    fun isAvailable(x: Int, y: Int): Boolean {
        return (farmlandBoard.getOrNull(x) ?: 0) and (1 shl (rowCount - 1 - y)) != 0
    }

    fun getBlockBounds(x: Int, y: Int): FarmBounds {
        if (!isAvailable(x, y)) {
            return FarmBounds(false, 0f, 0f, 0f, 0f, 0f)
        }

        return FarmBounds(
            true,
            screenBottom = middlePointY - blockLeftHeight * y - blockRightHeight * x,
            screenTop = middlePointY - blockHeight - blockLeftHeight * (y + 1) - blockRightHeight * (x + 1),
            screenLeft = middlePointX - blockLeftWidth - blockLeftWidth * y + blockRightWidth * x,
            screenRight = middlePointX + blockRightWidth - blockLeftWidth * y + blockRightWidth * x,
            screenBlockHeight = blockHeight,
        )
    }

    var mouseX: Float = 0f
    var mouseY: Float = 0f
    var renderX: Int = -1
    var renderY: Int = -1

    fun findMouseInField(mouseX: Float, mouseY: Float): Pair<Int, Int>? {
        // 计算列方向（右斜）基向量Vx和行方向（左斜）基向量Vy
        val vx = Point(
            (middlePointX - rightPointX) / columnCount,
            (middlePointY - rightPointY) / columnCount
        )
        val vy = Point(
            (middlePointX - leftPointX) / rowCount,
            (middlePointY - leftPointY) / rowCount
        )

        val dx = middlePointX - mouseX
        val dy = middlePointY - mouseY

        // 计算行列式以确定是否有解
        val determinant = vx.x * vy.y - vx.y * vy.x
        if (determinant == 0f) return null // 基向量平行，无法构成网格

        // 解线性方程组以获取列和行的浮点值
        val col = (vy.y * dx - vy.x * dy) / determinant
        val row = (-vx.y * dx + vx.x * dy) / determinant

        // 转换为整数索引
        val x = col.toInt()
        val y = row.toInt()

        return Pair(x, y)
    }

    fun render(
        blockHeight: Float,
        blockDeep: Float,
        leftPointX: Float,
        leftPointY: Float,
        middlePointX: Float,
        middlePointY: Float,
        rightPointX: Float,
        rightPointY: Float,
    ) {
        screenBounds = ScreenBounds(
            left = leftPointX,
            right = rightPointX,
            top = leftPointY - middlePointY + rightPointY,
            bottom = middlePointY
        )

        this.middlePointX = middlePointX
        this.middlePointY = middlePointY
        this.leftPointX = leftPointX
        this.leftPointY = leftPointY
        this.rightPointX = rightPointX
        this.rightPointY = rightPointY
        this.blockHeight = blockHeight
        this.blockDeep = blockDeep
        blockLeftWidth = (middlePointX - leftPointX) / rowCount
        blockLeftHeight = (middlePointY - leftPointY) / rowCount
        blockRightWidth = (rightPointX - middlePointX) / columnCount
        blockRightHeight = (middlePointY - rightPointY) / columnCount

        super.render()

//        renderPoint(convertScreenToNdc(window, leftPointX, leftPointY))
//        renderPoint(convertScreenToNdc(window, middlePointX, middlePointY))
//        renderPoint(convertScreenToNdc(window, rightPointX, rightPointY))
//        renderPoint(convertScreenToNdc(window, mouseX, mouseY))

        wheat.render(getBlockBounds(renderX, renderY))
    }

    override fun onMouseMove(e: MouseMoveEvent) {
        mouseX = e.x
        mouseY = e.y
        val gridPos = findMouseInField(mouseX, mouseY)
        renderX = gridPos?.first ?: -1
        renderY = gridPos?.second ?: -1
    }

    override fun cleanup() {
        wheat.cleanup()
        carrot.cleanup()
        potato.cleanup()
    }
}