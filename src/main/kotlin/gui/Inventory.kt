package com.midnightcrowing.gui

import com.midnightcrowing.controllers.InventoryController
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.render.RectangleRenderer
import com.midnightcrowing.render.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.scenes.FarmScene
import org.lwjgl.glfw.GLFW.GLFW_KEY_E

class Inventory(val screen: FarmScene, private val controller: InventoryController) : Widget(screen.window, z = 2) {
    companion object {
        private const val BASE_WIDTH = 352
        private const val BASE_HEIGHT = 198
        private const val SCALE_BASE = 700.0
        const val OFFSET_Y = 40 // 向下偏移的距离

        private val BASE_BAG_BAR_POINT_1 = Point(16.0, 34.0)
        private val BASE_BAG_BAR_POINT_2 = Point(335.0, 137.0)
        private val BASE_QUICK_BAR_POINT_1 = Point(16.0, 150.0)
        private val BASE_QUICK_BAR_POINT_2 = Point(335.0, 181.0)
        private const val BASE_GRID_GAP = 4
        const val BAG_BAR_COL_NUM = 9 // 列数
        const val BAG_BAR_ROW_NUM = 3 // 行数

        private val SCALED: Double by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH: Double by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT: Double by lazy { BASE_HEIGHT * SCALED }
        val BAG_BAR_POINT_1: Point by lazy {
            Point(
                BASE_BAG_BAR_POINT_1.x * SCALED,
                BASE_BAG_BAR_POINT_1.y * SCALED
            )
        }
        val BAG_BAR_POINT_2: Point by lazy {
            Point(
                BASE_BAG_BAR_POINT_2.x * SCALED,
                BASE_BAG_BAR_POINT_2.y * SCALED
            )
        }
        val QUICK_BAR_POINT_1: Point by lazy {
            Point(
                BASE_QUICK_BAR_POINT_1.x * SCALED,
                BASE_QUICK_BAR_POINT_1.y * SCALED
            )
        }
        val QUICK_BAR_POINT_2: Point by lazy {
            Point(
                BASE_QUICK_BAR_POINT_2.x * SCALED,
                BASE_QUICK_BAR_POINT_2.y * SCALED
            )
        }
        val GRID_GAP: Double by lazy { BASE_GRID_GAP * SCALED }
    }

    init {
        controller.init(this)
        controller.setItem(0, ItemStack("minecraft:cabbage_seed", 2)) // 示例物品
    }

    override val renderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.INVENTORY.texture)
    private val maskActiveBgRender: RectangleRenderer = RectangleRenderer(color = floatArrayOf(1f, 1f, 1f, 0.5f))
    private var maskActiveBgBounds: ScreenBounds? = null

    private fun isClickBagBar(point: Point): Boolean {
        return widgetBounds.x1 + BAG_BAR_POINT_1.x < point.x && point.x < widgetBounds.x1 + BAG_BAR_POINT_2.x &&
                widgetBounds.y1 + BAG_BAR_POINT_1.y < point.y && point.y < widgetBounds.y1 + BAG_BAR_POINT_2.y
    }

    private fun isClickQuickBar(point: Point): Boolean {
        return widgetBounds.x1 + QUICK_BAR_POINT_1.x < point.x && point.x < widgetBounds.x1 + QUICK_BAR_POINT_2.x &&
                widgetBounds.y1 + QUICK_BAR_POINT_1.y < point.y && point.y < widgetBounds.y1 + QUICK_BAR_POINT_2.y
    }

    private fun getBagBarGridPosition(point: Point): Pair<Int, Int>? {
        if (isClickBagBar(point)) {
            // 计算点击的格子
            val relativeX = point.x - (widgetBounds.x1 + BAG_BAR_POINT_1.x)
            val relativeY = point.y - (widgetBounds.y1 + BAG_BAR_POINT_1.y)
            val gridWidth = BAG_BAR_POINT_2.x - BAG_BAR_POINT_1.x
            val gridHeight = BAG_BAR_POINT_2.y - BAG_BAR_POINT_1.y

            val x = (relativeX / gridWidth * BAG_BAR_COL_NUM).toInt()
            val y = (relativeY / gridHeight * BAG_BAR_ROW_NUM).toInt()
            return Pair(x, y)
        }
        return null
    }

    private fun getQuickBarGridPosition(point: Point): Int? {
        if (isClickQuickBar(point)) {
            // 计算点击的格子
            val relativeX = point.x - (widgetBounds.x1 + QUICK_BAR_POINT_1.x)
            val gridWidth = QUICK_BAR_POINT_2.x - QUICK_BAR_POINT_1.x

            val x = (relativeX / gridWidth * BAG_BAR_COL_NUM).toInt()
            return x
        }
        return null
    }

    private fun calculateBagBarGridBounds(gridX: Int, gridY: Int): ScreenBounds {
        val gridWidth =
            (BAG_BAR_POINT_2.x - BAG_BAR_POINT_1.x - GRID_GAP * (BAG_BAR_COL_NUM - 1)) / BAG_BAR_COL_NUM
        val gridHeight =
            (BAG_BAR_POINT_2.y - BAG_BAR_POINT_1.y - GRID_GAP * (BAG_BAR_ROW_NUM - 1)) / BAG_BAR_ROW_NUM
        val x1 = widgetBounds.x1 + BAG_BAR_POINT_1.x + gridX * (gridWidth + GRID_GAP)
        val y1 = widgetBounds.y1 + BAG_BAR_POINT_1.y + gridY * (gridHeight + GRID_GAP)
        val x2 = x1 + gridWidth
        val y2 = y1 + gridHeight
        return ScreenBounds(x1, y1, x2, y2)
    }

    private fun calculateQuickBarGridBounds(gridX: Int): ScreenBounds {
        val gridWidth =
            (QUICK_BAR_POINT_2.x - QUICK_BAR_POINT_1.x - GRID_GAP * (BAG_BAR_COL_NUM - 1)) / BAG_BAR_COL_NUM
        val x1 = widgetBounds.x1 + QUICK_BAR_POINT_1.x + gridX * (gridWidth + GRID_GAP)
        val y1 = widgetBounds.y1 + QUICK_BAR_POINT_1.y
        val x2 = x1 + gridWidth
        val y2 = widgetBounds.y1 + QUICK_BAR_POINT_2.y
        return ScreenBounds(x1, y1, x2, y2)
    }

    override fun onMouseMove(e: MouseMoveEvent) {
        maskActiveBgBounds = getBagBarGridPosition(Point(e.x, e.y))
            ?.let { calculateBagBarGridBounds(it.first, it.second) }
            ?: getQuickBarGridPosition(Point(e.x, e.y))
                ?.let { calculateQuickBarGridBounds(it) }

        maskActiveBgBounds?.let {
            maskActiveBgRender.apply {
                x1 = it.x1
                y1 = it.y1
                x2 = it.x2
                y2 = it.y2
            }
        }
    }

    override fun onMouseLeave() {
        maskActiveBgBounds = null
    }

    override fun onClick(e: MouseClickEvent) {
        super.onClick(e)
    }

    override fun onKeyPress(e: KeyPressedEvent) {
        if (e.key == GLFW_KEY_E) {
            if (!isVisible) {
                screen.hotBar.setHidden(true)
            } else {
                screen.hotBar.setHidden(false)
            }
            toggleVisible()
        }
    }

    override fun render() {
        super.render()
        if (!isVisible) {
            return
        }
        // 渲染快捷栏物品
        controller.hotBarItems.forEachIndexed { index, item ->
            renderItem(item, calculateQuickBarGridBounds(index))
        }
        // 渲染背包物品
        controller.mainInvItems.forEachIndexed { index, item ->
            renderItem(item, calculateBagBarGridBounds(index % BAG_BAR_COL_NUM, index / BAG_BAR_COL_NUM))
        }
        if (maskActiveBgBounds != null) {
            maskActiveBgRender.render()
        }
    }

    fun renderItem(stack: ItemStack, position: ScreenBounds) {
        if (!stack.isEmpty()) {
            val item = ItemRegistry.createItem(stack.id, this)
            item?.place(position)
            item?.render(stack.count)
        }
    }

    override fun cleanup() {
        super.cleanup()
    }
}