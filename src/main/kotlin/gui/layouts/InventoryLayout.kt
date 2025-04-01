package com.midnightcrowing.gui.layouts

import com.midnightcrowing.controllers.InventoryController
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.publics.DraggingItem
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.renderer.RectangleRenderer
import org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT


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
    private val itemCache: MutableMap<String, FarmItems?> = mutableMapOf<String, FarmItems?>()

    // 输入状态
    private var mousePosition: Point = Point.EMPTY
    private var mouseIndex: Int? = null
    private var mouseLeftPressed: Boolean = false
    private var isShiftPressed: Boolean = false

    // 工具函数，获取物品缓存
    private fun getItemCache(id: String): FarmItems? = itemCache.getOrPut(id) { ItemRegistry.createItem(id, this) }

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

    // region 事件处理
    override fun onClick(e: MouseClickEvent) {
        if (!isShiftPressed) {
            Point(e.x, e.y).index?.let { it ->
                handleItemDragAndDrop(it)
            }
        }
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
        itemCache.keys.retainAll(controller.items.map { it.id })
    }

    private fun renderItem(stack: ItemStack, position: ScreenBounds) {
        if (!stack.isEmpty()) {
            val item = getItemCache(stack.id)
            item?.place(position)
            item?.render(stack.count)
        }
    }

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
        getItemCache(item.id)?.renderItemName(mousePosition.x + 30, mousePosition.y - 25)
    }

    // endregion
}