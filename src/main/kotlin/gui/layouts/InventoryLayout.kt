package com.midnightcrowing.gui.layouts

import com.midnightcrowing.controllers.InventoryController
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.publics.DraggingItem
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemRenderCache
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.renderer.RectangleRenderer
import org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT

/**
 * 用于管理和渲染背包和快捷栏的布局。
 *
 * 需要父组件转发 `update()`、`onMousePress(e)`、`onMouseRelease(e)`、`onMouseMove(e)` 方法与事件。
 *
 * @param parent 父级 Widget
 * @param controller InventoryController 控制器
 * @param dragWidget DraggingItem 拖动的物品
 * @param bagBarBounds 背包栏的屏幕边界
 * @param quickBarBounds 快捷栏的屏幕边界
 * @param gridGap 网格间距
 * @param bagBarColNum 背包栏的列数
 * @param bagBarRowNum 背包栏的行数
 */
class InventoryLayout(
    parent: Widget,
    private val controller: InventoryController,
    private val dragWidget: DraggingItem,
    private val bagBarBounds: ScreenBounds,
    private val quickBarBounds: ScreenBounds,
    private val gridGap: Double,
    private val bagBarColNum: Int = 9, // 列数
    private val bagBarRowNum: Int = 3, // 行数
) : Widget(parent) {
    private val gridWidth: Double = (bagBarBounds.width - gridGap * (bagBarColNum - 1)) / bagBarColNum
    private val gridHeight: Double = (bagBarBounds.height - gridGap * (bagBarRowNum - 1)) / bagBarRowNum

    private val Point.isInBagBar: Boolean
        get() = x in widgetBounds.x1..widgetBounds.x2 &&
                y in widgetBounds.y1..(widgetBounds.y1 + bagBarBounds.height)

    private val Point.isInQuickBar: Boolean
        get() = x in widgetBounds.x1..widgetBounds.x2 &&
                y in (widgetBounds.y2 - quickBarBounds.height)..widgetBounds.y2

    private val Point.bagBarGridPosition: Pair<Int, Int>?
        get() = takeIf { isInBagBar }?.let {
            // 计算点击的格子
            val relativeX = x - widgetBounds.x1
            val relativeY = y - widgetBounds.y1

            (relativeX / bagBarBounds.width * bagBarColNum).toInt() to
                    (relativeY / bagBarBounds.height * bagBarRowNum).toInt()
        }

    private val Point.quickBarGridPosition: Int?
        get() = takeIf { isInQuickBar }?.let {
            // 计算点击的格子
            val relativeX = x - widgetBounds.x1

            (relativeX / quickBarBounds.width * bagBarColNum).toInt()
        }

    private val Point.index: Int?
        get() = bagBarGridPosition?.let { (x, y) -> 9 + y * bagBarColNum + x } ?: quickBarGridPosition

    init {
        dragWidget.width = gridWidth
        dragWidget.height = gridHeight
    }

    // 渲染器
    private val maskActiveBgRender: RectangleRenderer = RectangleRenderer(color = floatArrayOf(1f, 1f, 1f, 0.5f))
    private var maskActiveBgBounds: ScreenBounds? = null

    // 物品缓存，避免每次渲染时重复创建
    private val itemRenderCache: ItemRenderCache = ItemRenderCache(this)

    // 输入状态
    private var mousePosition: Point = Point.EMPTY
    private var mouseIndex: Int? = null
    var mouseLeftPressed: Boolean = false
    var isShiftPressed: Boolean = false

    private fun calculateBagBarGridBounds(gridX: Int, gridY: Int): ScreenBounds {
        val x1 = widgetBounds.x1 + gridX * (gridWidth + gridGap)
        val y1 = widgetBounds.y1 + gridY * (gridHeight + gridGap)
        val x2 = x1 + gridWidth
        val y2 = y1 + gridHeight
        return ScreenBounds(x1, y1, x2, y2)
    }

    private fun calculateQuickBarGridBounds(gridX: Int): ScreenBounds {
        val x1 = widgetBounds.x1 + gridX * (gridWidth + gridGap)
        val y1 = widgetBounds.y2 - gridHeight
        val x2 = x1 + gridWidth
        val y2 = widgetBounds.y2
        return ScreenBounds(x1, y1, x2, y2)
    }

    override fun update() {
        if (isShiftPressed && mouseLeftPressed) {
            mouseIndex?.let { index ->
                val item = controller.popItem(index)
                val target = if (index > 8) "hotbar" else "main"
                val result = if (isShiftPressed) controller.addItem(item, target) else false

                if (!result) {
                    controller.setItem(index, item)
                }
            }
        }
    }

    // region 事件处理
    /**
     * 处理物品交互
     * @param x 鼠标X坐标
     * @param y 鼠标Y坐标
     * @param exchangeFn 交换函数
     */
    private fun handleItemInteraction(
        x: Double,
        y: Double,
        exchangeFn: (ItemStack, ItemStack) -> Pair<ItemStack, ItemStack>,
    ) {
        Point(x, y).index?.let { index ->
            val invItem = controller.popItem(index)
            val dragItem = dragWidget.item

            val (newInvItem, newDragItem) = exchangeFn(invItem, dragItem)
            dragWidget.item = newDragItem
            controller.setItem(index, newInvItem)

            dragWidget.onParentMouseMove(getCursorPos())
        }
    }

    override fun onClick(e: MouseClickEvent) {
        if (isShiftPressed) return
        handleItemInteraction(e.x, e.y, controller::exchangeAndMergeItems)
    }

    override fun onRightClick(e: MouseRightClickEvent) {
        handleItemInteraction(e.x, e.y, controller::rightClickExchangeItems)
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
        mousePosition = pos
        mouseIndex = pos.index

        maskActiveBgBounds = pos.bagBarGridPosition
            ?.let { calculateBagBarGridBounds(it.first, it.second) }
            ?: pos.quickBarGridPosition
                ?.let { calculateQuickBarGridBounds(it) }

        maskActiveBgBounds?.let {
            maskActiveBgRender.apply { x1 = it.x1; y1 = it.y1; x2 = it.x2 + 1; y2 = it.y2 + 1 }
        }

        dragWidget.onParentMouseMove(e)
    }

    override fun onMouseLeave() {
        maskActiveBgBounds = null
        mouseIndex = null
    }

    override fun onKeyPress(e: KeyPressedEvent): Boolean {
        if (e.key == GLFW_KEY_LEFT_SHIFT) {
            isShiftPressed = true
        }
        return true
    }

    override fun onKeyReleased(e: KeyReleasedEvent): Boolean {
        if (e.key == GLFW_KEY_LEFT_SHIFT) {
            isShiftPressed = false
        }
        return true
    }

    // endregion

    // region 位置 & 渲染
    fun place() {
        if (parent != null) {
            super.place(
                x1 = parent.widgetBounds.x1 + bagBarBounds.x1,
                y1 = parent.widgetBounds.y1 + bagBarBounds.y1,
                x2 = parent.widgetBounds.x1 + quickBarBounds.x2,
                y2 = parent.widgetBounds.y1 + quickBarBounds.y2
            )
        }
    }

    override fun doRender() {
        renderItems()                      // 渲染物品
        if (maskActiveBgBounds != null) {
            maskActiveBgRender.render()    // 渲染物品格子高亮
        }
        renderHoverItemName()              // 渲染高亮物品名称
    }

    /**
     * 渲染物品
     */
    private fun renderItems() {
        // 渲染快捷栏物品
        controller.hotBarItems.forEachIndexed { index, item ->
            renderItem(item, calculateQuickBarGridBounds(index))
        }
        // 渲染背包物品
        controller.mainInvItems.forEachIndexed { index, item ->
            renderItem(item, calculateBagBarGridBounds(index % bagBarColNum, index / bagBarColNum))
        }

        // 清理不在items里的物品
        itemRenderCache.retainAll(controller.items.map { it.id })
    }

    /**
     * 渲染单个物品
     * @param stack 物品堆叠
     * @param position 物品位置
     */
    private fun renderItem(stack: ItemStack, position: ScreenBounds) {
        itemRenderCache.getItemCache(stack.id)?.apply {
            place(position)
            render(stack.count)
        }
    }

    /**
     * 渲染鼠标悬停的物品名称
     */
    private fun renderHoverItemName() {
        // 如果鼠标没有指向任何格子，直接返回
        val index = mouseIndex ?: return

        // 如果拖动物品不为空，直接返回
        if (!dragWidget.item.isEmpty()) return

        // 获取鼠标指向的物品
        val item = controller.getItem(index)

        // 如果物品为空，直接返回
        if (item.isEmpty()) return

        // 渲染物品名称
        itemRenderCache.getItemCache(item.id)?.renderTooltip(mousePosition.x, mousePosition.y)
    }

    // endregion
}