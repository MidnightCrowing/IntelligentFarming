package com.midnightcrowing.gui

import com.midnightcrowing.controllers.InventoryController
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.render.RectangleRenderer
import com.midnightcrowing.render.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.scenes.FarmScene
import org.lwjgl.glfw.GLFW.*
import kotlin.reflect.KClass

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
        val BAG_GRID_WIDTH: Double by lazy {
            (BAG_BAR_POINT_2.x - BAG_BAR_POINT_1.x - GRID_GAP * (BAG_BAR_COL_NUM - 1)) / BAG_BAR_COL_NUM
        }
        val BAG_GRID_Height: Double by lazy {
            (BAG_BAR_POINT_2.y - BAG_BAR_POINT_1.y - GRID_GAP * (BAG_BAR_ROW_NUM - 1)) / BAG_BAR_ROW_NUM
        }
        val QUICK_GRID_WIDTH: Double by lazy {
            (QUICK_BAR_POINT_2.x - QUICK_BAR_POINT_1.x - GRID_GAP * (BAG_BAR_COL_NUM - 1)) / BAG_BAR_COL_NUM
        }
    }

    private val Point.isInBagBar: Boolean
        get() = widgetBounds.x1 + BAG_BAR_POINT_1.x < x && x < widgetBounds.x1 + BAG_BAR_POINT_2.x &&
                widgetBounds.y1 + BAG_BAR_POINT_1.y < y && y < widgetBounds.y1 + BAG_BAR_POINT_2.y

    private val Point.isInQuickBar: Boolean
        get() = x in (widgetBounds.x1 + QUICK_BAR_POINT_1.x)..(widgetBounds.x1 + QUICK_BAR_POINT_2.x) &&
                y in (widgetBounds.y1 + QUICK_BAR_POINT_1.y)..(widgetBounds.y1 + QUICK_BAR_POINT_2.y)

    private val Point.bagBarGridPosition: Pair<Int, Int>?
        get() = takeIf { isInBagBar }?.let {
            // 计算点击的格子
            val (offsetX, offsetY) = BAG_BAR_POINT_1
            val (gridWidth, gridHeight) = BAG_BAR_POINT_2.x - offsetX to BAG_BAR_POINT_2.y - offsetY

            ((x - widgetBounds.x1 - offsetX) / gridWidth * BAG_BAR_COL_NUM).toInt() to
                    ((y - widgetBounds.y1 - offsetY) / gridHeight * BAG_BAR_ROW_NUM).toInt()
        }

    private val Point.quickBarGridPosition: Int?
        get() = takeIf { isInQuickBar }?.let {
            // 计算点击的格子
            val relativeX = x - (widgetBounds.x1 + QUICK_BAR_POINT_1.x)
            val gridWidth = QUICK_BAR_POINT_2.x - QUICK_BAR_POINT_1.x

            (relativeX / gridWidth * BAG_BAR_COL_NUM).toInt()
        }

    private val Point.index: Int?
        get() = bagBarGridPosition?.let { (x, y) -> 9 + y * BAG_BAR_COL_NUM + x } ?: quickBarGridPosition

    init {
        controller.init(this)
    }

    override val renderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.INVENTORY.texture)
    private val backgroundRender: RectangleRenderer = RectangleRenderer(
        color = floatArrayOf(0f, 0f, 0f, 0.73f),
    )
    private val dragWidget: DraggingItem = DraggingItem(this).apply {
        width = BAG_GRID_WIDTH; height = BAG_GRID_Height
    }
    private val maskActiveBgRender: RectangleRenderer = RectangleRenderer(color = floatArrayOf(1f, 1f, 1f, 0.5f))
    private var maskActiveBgBounds: ScreenBounds? = null

    // 物品缓存，避免每次渲染时重复创建
    internal val itemCache: MutableMap<String, FarmItems?> = mutableMapOf<String, FarmItems?>()
    private var mouseIndex: Int? = null
    private var mouseLeftPressed: Boolean = false
    private var isShiftPressed: Boolean = false

    // 工具函数，获取物品缓存
    private fun getItemCache(id: String): FarmItems? = itemCache.getOrPut(id) { ItemRegistry.createItem(id, this) }

    private fun calculateBagBarGridBounds(gridX: Int, gridY: Int): ScreenBounds {
        val x1 = widgetBounds.x1 + BAG_BAR_POINT_1.x + gridX * (BAG_GRID_WIDTH + GRID_GAP)
        val y1 = widgetBounds.y1 + BAG_BAR_POINT_1.y + gridY * (BAG_GRID_Height + GRID_GAP)
        val x2 = x1 + BAG_GRID_WIDTH
        val y2 = y1 + BAG_GRID_Height
        return ScreenBounds(x1, y1, x2, y2)
    }

    private fun calculateQuickBarGridBounds(gridX: Int): ScreenBounds {
        val x1 = widgetBounds.x1 + QUICK_BAR_POINT_1.x + gridX * (QUICK_GRID_WIDTH + GRID_GAP)
        val y1 = widgetBounds.y1 + QUICK_BAR_POINT_1.y
        val x2 = x1 + QUICK_GRID_WIDTH
        val y2 = widgetBounds.y1 + QUICK_BAR_POINT_2.y
        return ScreenBounds(x1, y1, x2, y2)
    }

    private fun handleItemDragAndDrop(index: Int) {
        when (dragWidget.item.isEmpty() to controller.getItem(index).isEmpty()) {
            // 背包格子是空的，拖动物品也是空的，直接返回
            true to true -> return
            // 背包格子有物品，拖动物品是空的，直接取出物品
            true to false -> {
                dragWidget.item = controller.popItem(index)
                dragWidget.onParentMouseMove(getCursorPos())
            }
            // 背包格子是空的，拖动物品不为空，直接放入物品
            false to true -> {
                controller.setItem(index, dragWidget.item)
                dragWidget.item = ItemStack.EMPTY
            }
            // 背包格子有物品，拖动物品也不为空
            else -> {
                val invItem = controller.popItem(index)
                val dragItem = dragWidget.item

                if (dragItem.id == invItem.id) {
                    // id相同，叠加物品
                    val count = dragItem.count + invItem.count
                    invItem.count = minOf(count, 64)
                    dragItem.count = maxOf(0, count - 64)
                    dragWidget.item = if (dragItem.count == 0) ItemStack.EMPTY else dragItem
                    controller.setItem(index, invItem)
                    dragWidget.onParentMouseMove(getCursorPos())
                } else {
                    // id不同，交换物品
                    controller.setItem(index, dragWidget.item)
                    dragWidget.item = invItem
                    dragWidget.onParentMouseMove(getCursorPos())
                }
            }
        }
    }

    fun update() {
        if (mouseLeftPressed) {
            mouseIndex?.let { it ->
                val item = controller.popItem(it)
                val target = if (it > 8) "hotbar" else "main"
                val result = if (isShiftPressed) controller.addItem(item, target) else false

                if (!result) {
                    controller.setItem(it, item)
                }
            }
        }
    }

    override fun containsPoint(x: Double, y: Double, event: KClass<out Event>?): Boolean = true

    override fun onClick(e: MouseClickEvent) {
        if (!isShiftPressed) {
            Point(e.x, e.y).index?.let { it ->
                handleItemDragAndDrop(it)
            }
        }
    }

    override fun onRightClick(e: MouseRightClickEvent) {
        super.onRightClick(e)
    }

    override fun onMousePress(e: MousePressedEvent) {
        if (e.button == GLFW_MOUSE_BUTTON_LEFT) {
            mouseLeftPressed = true
        }
    }

    override fun onMouseRelease(e: MouseReleasedEvent) {
        if (e.button == GLFW_MOUSE_BUTTON_LEFT) {
            mouseLeftPressed = false
        }
    }

    override fun onMouseMove(e: MouseMoveEvent) {
        val pos = Point(e.x, e.y)
        mouseIndex = pos.index

        maskActiveBgBounds = pos.bagBarGridPosition
            ?.let { calculateBagBarGridBounds(it.first, it.second) }
            ?: pos.quickBarGridPosition
                ?.let { calculateQuickBarGridBounds(it) }

        maskActiveBgBounds?.let {
            maskActiveBgRender.apply { x1 = it.x1; y1 = it.y1; x2 = it.x2; y2 = it.y2 }
        }

        dragWidget.onParentMouseMove(e)
    }

    override fun onMouseLeave() {
        maskActiveBgBounds = null
    }

    override fun onKeyPress(e: KeyPressedEvent) {
        if (e.key == GLFW_KEY_E) {
            if (!isVisible) {
                screen.hotBar.setHidden(true)
            } else {
                screen.hotBar.setHidden(false)
            }
            toggleVisible()
            controller.hotBarController.update()

            val (x, y) = window.getCursorPos()
            this.onMouseMove(MouseMoveEvent(x, y))
        } else if (e.key == GLFW_KEY_LEFT_SHIFT) {
            isShiftPressed = true
        }
    }

    override fun onKeyReleased(e: KeyReleasedEvent) {
        if (e.key == GLFW_KEY_LEFT_SHIFT) {
            isShiftPressed = false
        }
    }

    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        super.place(x1, y1, x2, y2)
        backgroundRender.x2 = window.width.toDouble()
        backgroundRender.y2 = window.height.toDouble()
    }

    override fun render() {
        if (!isVisible) {
            return
        }

        // 渲染背景遮罩
        backgroundRender.render()

        super.render()
        renderItems()

        if (maskActiveBgBounds != null) {
            maskActiveBgRender.render()
        }

        dragWidget.render()
    }

    private fun renderItems() {
        // 渲染快捷栏物品
        controller.hotBarItems.forEachIndexed { index, item ->
            renderItem(item, calculateQuickBarGridBounds(index))
        }
        // 渲染背包物品
        controller.mainInvItems.forEachIndexed { index, item ->
            renderItem(item, calculateBagBarGridBounds(index % BAG_BAR_COL_NUM, index / BAG_BAR_COL_NUM))
        }

        // 清理不在items里的物品
        itemCache.keys.retainAll(controller.items.map { it.id })
    }

    private fun renderItem(stack: ItemStack, position: ScreenBounds) {
        if (!stack.isEmpty()) {
            val item = getItemCache(stack.id)
            item?.place(position)
            item?.render(stack.count)
        }
    }

    override fun cleanup() {
        super.cleanup()
        dragWidget.cleanup()
    }
}