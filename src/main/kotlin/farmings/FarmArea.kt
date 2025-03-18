package com.midnightcrowing.farmings

import com.midnightcrowing.events.CustomEvent.MouseMoveEvent
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.model.Point


class FarmArea : Widget {
    private val farmlandBoard: List<Int>
    private val rowCount: Int
    private val columnCount: Int

    constructor(window: Window, farmlandBoard: List<Int>) : super(window) {
        this.farmlandBoard = farmlandBoard
        this.rowCount = farmlandBoard.maxOf { Integer.toBinaryString(it).length }
        this.columnCount = farmlandBoard.size
    }

    constructor(parent: Widget, farmlandBoard: List<Int>) : super(parent) {
        this.farmlandBoard = farmlandBoard
        this.rowCount = farmlandBoard.maxOf { Integer.toBinaryString(it).length }
        this.columnCount = farmlandBoard.size
    }

    private companion object {
        const val SPARE_AREA_WIDTH: Int = 20 // 留白区域宽度
    }

    var middlePoint: Point = Point(0f, 0f)
    var leftPoint: Point = Point(0f, 0f)
    var rightPoint: Point = Point(0f, 0f)
    var blockHeight: Float = 0f
    var blockDeep: Float = 0f
    private val blockLeftWidth: Float get() = (middlePoint.x - leftPoint.x) / rowCount
    private val blockLeftHeight: Float get() = (middlePoint.y - leftPoint.y) / rowCount
    private val blockRightWidth: Float get() = (rightPoint.x - middlePoint.x) / columnCount
    private val blockRightHeight: Float get() = (middlePoint.y - rightPoint.y) / columnCount


    private val wheat = Wheat(this)
    private val carrot = Carrot(this)
    private val potato = Potato(this)

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
            screenBottom = middlePoint.y - blockLeftHeight * y - blockRightHeight * x,
            screenTop = middlePoint.y - blockHeight - blockLeftHeight * (y + 1) - blockRightHeight * (x + 1),
            screenLeft = middlePoint.x - blockLeftWidth - blockLeftWidth * y + blockRightWidth * x,
            screenRight = middlePoint.x + blockRightWidth - blockLeftWidth * y + blockRightWidth * x,
            screenBlockHeight = blockHeight,
        )
    }

    fun findMouseInField(mouseX: Float, mouseY: Float): Pair<Int, Int>? {
        // 计算列方向（右斜）基向量Vx和行方向（左斜）基向量Vy
        val vx = Point(
            (middlePoint.x - rightPoint.x) / columnCount,
            (middlePoint.y - rightPoint.y) / columnCount
        )
        val vy = Point(
            (middlePoint.x - leftPoint.x) / rowCount,
            (middlePoint.y - leftPoint.y) / rowCount
        )

        val dx = middlePoint.x - mouseX
        val dy = middlePoint.y - mouseY

        // 计算行列式以确定是否有解
        val determinant = vx.x * vy.y - vx.y * vy.x
        if (determinant == 0f) return null // 基向量平行，无法构成网格

        // 解线性方程组以获取列和行的浮点值
        val col = (vy.y * dx - vy.x * dy) / determinant
        val row = (-vx.y * dx + vx.x * dy) / determinant
        if (col <= 0f || row <= 0f) return null

        // 转换为整数索引
        val x = col.toInt()
        val y = row.toInt()

        return Pair(x, y)
    }

    fun place(blockDeep: Float, blockHeight: Float, leftPoint: Point, middlePoint: Point, rightPoint: Point) {
        this.blockDeep = blockDeep
        this.blockHeight = blockHeight
        this.leftPoint = leftPoint
        this.middlePoint = middlePoint
        this.rightPoint = rightPoint

        super.place(
            x1 = leftPoint.x - SPARE_AREA_WIDTH,
            x2 = rightPoint.x + SPARE_AREA_WIDTH,
            y1 = leftPoint.y - middlePoint.y + rightPoint.y - SPARE_AREA_WIDTH,
            y2 = middlePoint.y + SPARE_AREA_WIDTH
        )
    }

    override fun render() {
        super.render()

        wheat.render()
    }

    override fun onMouseMove(e: MouseMoveEvent) {
        val gridPos = findMouseInField(e.x, e.y)

        val a = getBlockBounds(gridPos?.first ?: -1, gridPos?.second ?: -1)
        wheat.place(a.screenLeft, a.screenTop, a.screenRight, a.screenBottom)
    }

    override fun cleanup() {
        wheat.cleanup()
        carrot.cleanup()
        potato.cleanup()
    }
}