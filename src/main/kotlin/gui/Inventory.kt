package com.midnightcrowing.gui

import com.midnightcrowing.events.CustomEvent.KeyPressedEvent
import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.scenes.FarmScene

class Inventory(val screen: FarmScene) : Widget(screen.window, z = 2) {
    companion object {
        private const val BASE_WIDTH = 352
        private const val BASE_HEIGHT = 198
        private const val SCALE_BASE = 700.0
        const val OFFSET_Y = 40 // 向下偏移的距离

        private val BAG_BAR_POINT_1 = Point(16.0, 34.0)
        private val BAG_BAR_POINT_2 = Point(335.0, 137.0)
        private val QUICK_BAR_POINT_1 = Point(16.0, 150.0)
        private val QUICK_BAR_POINT_2 = Point(335.0, 181.0)
        private const val GRID_GAP = 4
        const val BAG_BAR_COL_NUM = 9 // 列数
        const val BAG_BAR_ROW_NUM = 3 // 行数

        private const val BASE_CHECKBOX_SIZE = 7

        private val SCALED: Double by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH: Double by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT: Double by lazy { BASE_HEIGHT * SCALED }
        val SCALED_BAG_BAR_POINT_1: Point by lazy {
            Point(
                BAG_BAR_POINT_1.x * SCALED,
                BAG_BAR_POINT_1.y * SCALED
            )
        }
        val SCALED_BAG_BAR_POINT_2: Point by lazy {
            Point(
                BAG_BAR_POINT_2.x * SCALED,
                BAG_BAR_POINT_2.y * SCALED
            )
        }
        val SCALED_QUICK_BAR_POINT_1: Point by lazy {
            Point(
                QUICK_BAR_POINT_1.x * SCALED,
                QUICK_BAR_POINT_1.y * SCALED
            )
        }
        val SCALED_QUICK_BAR_POINT_2: Point by lazy {
            Point(
                QUICK_BAR_POINT_2.x * SCALED,
                QUICK_BAR_POINT_2.y * SCALED
            )
        }
        val SCALED_GRID_GAP: Double by lazy { GRID_GAP * SCALED }
        val SCALED_CHECKBOX_SIZE: Double by lazy { BASE_CHECKBOX_SIZE * SCALED }
    }

    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.INVENTORY.inputStream)

    val itemCheckBox: ItemCheckBox = ItemCheckBox(this)

    private fun isClickBagBar(point: Point): Boolean {
        return widgetBounds.x1 + SCALED_BAG_BAR_POINT_1.x < point.x && point.x < widgetBounds.x1 + SCALED_BAG_BAR_POINT_2.x &&
                widgetBounds.y1 + SCALED_BAG_BAR_POINT_1.y < point.y && point.y < widgetBounds.y1 + SCALED_BAG_BAR_POINT_2.y
    }

    private fun isClickQuickBar(point: Point): Boolean {
        return widgetBounds.x1 + SCALED_QUICK_BAR_POINT_1.x < point.x && point.x < widgetBounds.x1 + SCALED_QUICK_BAR_POINT_2.x &&
                widgetBounds.y1 + SCALED_QUICK_BAR_POINT_1.y < point.y && point.y < widgetBounds.y1 + SCALED_QUICK_BAR_POINT_2.y
    }

    private fun getBagBarGridPosition(point: Point): Pair<Int, Int>? {
        if (isClickBagBar(point)) {
            // 计算点击的格子
            val relativeX = point.x - (widgetBounds.x1 + SCALED_BAG_BAR_POINT_1.x)
            val relativeY = point.y - (widgetBounds.y1 + SCALED_BAG_BAR_POINT_1.y)
            val gridWidth = SCALED_BAG_BAR_POINT_2.x - SCALED_BAG_BAR_POINT_1.x
            val gridHeight = SCALED_BAG_BAR_POINT_2.y - SCALED_BAG_BAR_POINT_1.y

            val x = (relativeX / gridWidth * BAG_BAR_COL_NUM).toInt()
            val y = (relativeY / gridHeight * BAG_BAR_ROW_NUM).toInt()
            return Pair(x, y)
        }
        return null
    }

    private fun getQuickBarGridPosition(point: Point): Int? {
        if (isClickQuickBar(point)) {
            // 计算点击的格子
            val relativeX = point.x - (widgetBounds.x1 + SCALED_QUICK_BAR_POINT_1.x)
            val gridWidth = SCALED_QUICK_BAR_POINT_2.x - SCALED_QUICK_BAR_POINT_1.x

            val x = (relativeX / gridWidth * BAG_BAR_COL_NUM).toInt()
            return x
        }
        return null
    }

    private fun calculateBagBarGridBounds(gridX: Int, gridY: Int, expandBy: Double = 0.0): ScreenBounds {
        val gridWidth =
            (SCALED_BAG_BAR_POINT_2.x - SCALED_BAG_BAR_POINT_1.x - SCALED_GRID_GAP * (BAG_BAR_COL_NUM - 1)) / BAG_BAR_COL_NUM
        val gridHeight =
            (SCALED_BAG_BAR_POINT_2.y - SCALED_BAG_BAR_POINT_1.y - SCALED_GRID_GAP * (BAG_BAR_ROW_NUM - 1)) / BAG_BAR_ROW_NUM
        val x1 = widgetBounds.x1 + SCALED_BAG_BAR_POINT_1.x + gridX * (gridWidth + SCALED_GRID_GAP) - expandBy
        val y1 = widgetBounds.y1 + SCALED_BAG_BAR_POINT_1.y + gridY * (gridHeight + SCALED_GRID_GAP) - expandBy
        val x2 = x1 + gridWidth + expandBy * 2
        val y2 = y1 + gridHeight + expandBy * 2
        return ScreenBounds(x1, y1, x2, y2)
    }

    private fun calculateQuickBarGridBounds(gridX: Int, expandBy: Double = 0.0): ScreenBounds {
        val gridWidth =
            (SCALED_QUICK_BAR_POINT_2.x - SCALED_QUICK_BAR_POINT_1.x - SCALED_GRID_GAP * (BAG_BAR_COL_NUM - 1)) / BAG_BAR_COL_NUM
        val x1 = widgetBounds.x1 + SCALED_QUICK_BAR_POINT_1.x + gridX * (gridWidth + SCALED_GRID_GAP) - expandBy
        val y1 = widgetBounds.y1 + SCALED_QUICK_BAR_POINT_1.y - expandBy
        val x2 = x1 + gridWidth + expandBy * 2
        val y2 = widgetBounds.y1 + SCALED_QUICK_BAR_POINT_2.y + expandBy
        return ScreenBounds(x1, y1, x2, y2)
    }

    override fun onClick(e: MouseClickEvent) {
        getBagBarGridPosition(Point(e.x, e.y))?.let {
            // 计算格子边界
            val gridBounds = calculateBagBarGridBounds(it.first, it.second, expandBy = SCALED_CHECKBOX_SIZE / 2)
            itemCheckBox.place(gridBounds)
        } ?: getQuickBarGridPosition(Point(e.x, e.y))?.let {
            // 计算格子边界
            val gridBounds = calculateQuickBarGridBounds(it, expandBy = SCALED_CHECKBOX_SIZE / 2)
            itemCheckBox.place(gridBounds)
        }
    }

    override fun onKeyPress(e: KeyPressedEvent) {
        if (e.key == 69) {
            toggleVisible()
        }
    }

    override fun render() {
        super.render()
        itemCheckBox.render()
    }

    override fun cleanup() {
        super.cleanup()
        itemCheckBox.cleanup()
    }
}