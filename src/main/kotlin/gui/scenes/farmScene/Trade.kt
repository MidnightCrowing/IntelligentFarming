package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.controllers.TradeController
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.layouts.InventoryLayout
import com.midnightcrowing.gui.publics.DraggingItem
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemCache
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.renderer.RectangleRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import kotlin.reflect.KClass

class Trade(
    val screen: FarmScene,
    private val controller: TradeController,
    z: Int? = null,
) : Widget(screen, z) {
    companion object {
        private const val BASE_WIDTH = 276
        private const val BASE_HEIGHT = 166
        private const val SCALE_BASE = 900.0
        const val OFFSET_Y = 0 // 向下偏移的距离

        // 物品背包栏的坐标
        private val BASE_BAG_BAR_BOUNDS = ScreenBounds(108.0, 84.0, 268.0, 136.0)
        private val BASE_QUICK_BAR_BOUNDS = ScreenBounds(108.0, 142.0, 268.0, 158.0)
        private const val BASE_GRID_GAP = 2
        const val BAG_BAR_COL_NUM = 9 // 列数
        const val BAG_BAR_ROW_NUM = 3 // 行数

        // 交易选项和滚动条的坐标
        private val BASE_BUTTON_SLOT_BOUNDS = ScreenBounds(5.0, 18.0, 93.0, 158.0)
        private val BASE_SCROLL_SLOT_BOUNDS = ScreenBounds(94.0, 18.0, 100.0, 157.0)
        private const val BASE_SCROLL_HEIGHT: Double = 27.0
        const val BUTTON_NUM = 7 // 交易选项数量

        // 交易物品槽的坐标
        private val BASE_TRADE_SLOT_0_BOUNDS = ScreenBounds(136.0, 37.0, 152.0, 53.0)
        private val BASE_TRADE_SLOT_1_BOUNDS = ScreenBounds(162.0, 37.0, 178.0, 53.0)
        private val BASE_TRADE_SLOT_2_BOUNDS = ScreenBounds(220.0, 38.0, 236.0, 54.0)
        private val BASE_TRADE_ARROW_BOUNDS = ScreenBounds(183.0, 35.0, 210.0, 55.0)

        private val SCALED: Double by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH: Double by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT: Double by lazy { BASE_HEIGHT * SCALED }
        val BAG_BAR_BOUNDS: ScreenBounds by lazy { BASE_BAG_BAR_BOUNDS * SCALED }
        val QUICK_BAR_BOUNDS: ScreenBounds by lazy { BASE_QUICK_BAR_BOUNDS * SCALED }
        val BUTTON_SLOT_BOUNDS: ScreenBounds by lazy { BASE_BUTTON_SLOT_BOUNDS * SCALED }
        val SCROLL_SLOT_BOUNDS: ScreenBounds by lazy { BASE_SCROLL_SLOT_BOUNDS * SCALED }
        val TRADE_SLOT_0_BOUNDS: ScreenBounds by lazy { BASE_TRADE_SLOT_0_BOUNDS * SCALED }
        val TRADE_SLOT_1_BOUNDS: ScreenBounds by lazy { BASE_TRADE_SLOT_1_BOUNDS * SCALED }
        val TRADE_SLOT_2_BOUNDS: ScreenBounds by lazy { BASE_TRADE_SLOT_2_BOUNDS * SCALED }
        val TRADE_ARROW_BOUNDS: ScreenBounds by lazy { BASE_TRADE_ARROW_BOUNDS * SCALED }
        val BUTTON_HEIGHT: Double by lazy { BUTTON_SLOT_BOUNDS.height / BUTTON_NUM }
        val SCROLL_HEIGHT: Double by lazy { BASE_SCROLL_HEIGHT * SCALED }
        val GRID_GAP: Double by lazy { BASE_GRID_GAP * SCALED }
    }

    private val Point.isInTradeSlot: Int?
        get() = when {
            (TRADE_SLOT_0_BOUNDS + widgetBounds.startPoint).contains(this) -> 0
            (TRADE_SLOT_1_BOUNDS + widgetBounds.startPoint).contains(this) -> 1
            (TRADE_SLOT_2_BOUNDS + widgetBounds.startPoint).contains(this) -> 2
            else -> null
        }

    private val Point.isInScrollSlot: Boolean
        get() = SCROLL_SLOT_BOUNDS.contains(this)

    private val Point.isInButtonSlot: Boolean
        get() = BUTTON_SLOT_BOUNDS.contains(this)

    init {
        controller.init(this)
    }

    // 渲染器 & 组件
    override val renderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.TRADE.texture)
    private val backgroundRender: RectangleRenderer = RectangleRenderer(
        color = floatArrayOf(0f, 0f, 0f, 0.73f),
    )
    private val maskActiveBgRender: RectangleRenderer = RectangleRenderer(color = floatArrayOf(1f, 1f, 1f, 0.5f))
    private var maskActiveBgBounds: ScreenBounds? = null
    private val dragWidget: DraggingItem = DraggingItem(this)
    private val invLayout: InventoryLayout = InventoryLayout(
        parent = this,
        controller = controller.invController,
        dragWidget = dragWidget,
        bagBarBounds = BAG_BAR_BOUNDS,
        quickBarBounds = QUICK_BAR_BOUNDS,
        gridGap = GRID_GAP,
        bagBarColNum = BAG_BAR_COL_NUM,
        bagBarRowNum = BAG_BAR_ROW_NUM,
    )
    private val tradeButtons: List<TradeButton> = List(BUTTON_NUM) { index ->
        TradeButton(
            parent = this,
        )
    }
    private val scrollRender: TextureRenderer = TextureRenderer(TextureResourcesEnum.TRADE_SCROLL_DISABLED.texture)
    private val scrollBounds: ScreenBounds = ScreenBounds.EMPTY

    // 槽位物品
    private var tradeSlot0Item: ItemStack = ItemStack.EMPTY
    private var tradeSlot1Item: ItemStack = ItemStack.EMPTY
    private var tradeSlot2Item: ItemStack = ItemStack.EMPTY

    // 物品缓存，避免每次渲染时重复创建
    private val itemCache: ItemCache = ItemCache(this)

    // 输入状态
    private var mousePosition: Point = Point.EMPTY
    private var mouseTradeIndex: Int? = null

    /**
     * 根据索引获取交易槽位
     */
    private fun getTradeSlotItem(index: Int): ItemStack = when (index) {
        0 -> tradeSlot0Item
        1 -> tradeSlot1Item
        2 -> tradeSlot2Item
        else -> ItemStack.EMPTY
    }

    /**
     * 根据索引设置交易槽位
     */
    private fun setTradeSlotItem(index: Int, item: ItemStack) {
        when (index) {
            0 -> tradeSlot0Item = item
            1 -> tradeSlot1Item = item
            2 -> tradeSlot2Item = item
        }
    }

    private fun calculateTradeBounds(index: Int): ScreenBounds? = when (index) {
        0 -> TRADE_SLOT_0_BOUNDS + widgetBounds.startPoint
        1 -> TRADE_SLOT_1_BOUNDS + widgetBounds.startPoint
        2 -> TRADE_SLOT_2_BOUNDS + widgetBounds.startPoint
        else -> null
    }

    private fun calculateButtonBounds(index: Int): ScreenBounds {
        val x1 = widgetBounds.x1 + BUTTON_SLOT_BOUNDS.x1
        val y1 = widgetBounds.y1 + BUTTON_SLOT_BOUNDS.y1 + index * BUTTON_HEIGHT
        val x2 = widgetBounds.x1 + BUTTON_SLOT_BOUNDS.x2
        val y2 = y1 + BUTTON_HEIGHT
        return ScreenBounds(x1, y1, x2, y2)
    }

    fun update() {
        invLayout.update()

        if (invLayout.isShiftPressed && invLayout.mouseLeftPressed) {
            mouseTradeIndex?.let { index ->
                val tradeSlotItem = getTradeSlotItem(index)
                val result = controller.invController.addItem(tradeSlotItem)

                if (result) {
                    setTradeSlotItem(index, ItemStack.EMPTY)
                }
            }
        }
    }

    override fun containsPoint(x: Double, y: Double, event: KClass<out Event>?): Boolean = true

    // region 事件处理
    /**
     * 处理物品拖放
     * @param index 交易槽位索引
     */
    private fun handleItemDragAndDrop(index: Int) {
        val tradeSlotItem = getTradeSlotItem(index)
        val dragItem = dragWidget.item

        val (newInvItem, newDragItem) = controller.invController.exchangeAndMergeItems(tradeSlotItem, dragItem)
        setTradeSlotItem(index, newInvItem)
        dragWidget.item = newDragItem

        dragWidget.onParentMouseMove(getCursorPos())
    }

    override fun onClick(e: MouseClickEvent) {
        if (invLayout.isShiftPressed) {
            return
        }

        Point(e.x, e.y).isInTradeSlot
            ?.takeIf { it != 2 } // 交易槽位2不处理
            ?.let { handleItemDragAndDrop(it) }
    }

    override fun onMousePress(e: MousePressedEvent) = invLayout.onMousePress(e)

    override fun onMouseRelease(e: MouseReleasedEvent) = invLayout.onMouseRelease(e)

    override fun onMouseMove(e: MouseMoveEvent) {
        invLayout.onMouseMove(e)

        val pos = Point(e.x, e.y)
        mousePosition = pos
        mouseTradeIndex = pos.isInTradeSlot
        maskActiveBgBounds = mouseTradeIndex?.let { calculateTradeBounds(it) }
        maskActiveBgBounds?.let {
            maskActiveBgRender.apply { x1 = it.x1; y1 = it.y1; x2 = it.x2 + 1; y2 = it.y2 + 1 }
        }
    }

    // endregion

    // region 位置 & 渲染
    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        super.place(x1, y1, x2, y2)

        // 设置背景遮罩的坐标
        backgroundRender.x2 = window.width.toDouble()
        backgroundRender.y2 = window.height.toDouble()

        // 设置滚动条坐标
        scrollBounds.x1 = widgetBounds.x1 + SCROLL_SLOT_BOUNDS.x1
        scrollBounds.x2 = widgetBounds.x1 + SCROLL_SLOT_BOUNDS.x2
        scrollBounds.y1 = widgetBounds.y1 + SCROLL_SLOT_BOUNDS.y1
        scrollBounds.y2 = scrollBounds.y1 + SCROLL_HEIGHT

        invLayout.place()
    }

    override fun render() {
        if (!isVisible) {
            return
        }
        backgroundRender.render()          // 渲染背景遮罩
        super.render()                     // 渲染组件
        scrollRender.render(scrollBounds)  // 渲染滚动条
        invLayout.render()                 // 渲染物品栏布局
        renderTradeSlots()                 // 渲染交易槽位物品
        tradeButtons.forEachIndexed { index, it ->
            it.place(calculateButtonBounds(index))
            it.render()
        }

        if (maskActiveBgBounds != null) {
            maskActiveBgRender.render()    // 渲染物品格子高亮
        }
        renderHoverTradeItemName()         // 渲染鼠标悬停在交易槽位时的物品名称
        dragWidget.render()                // 渲染拖动物品组件
    }

    /**
     * 渲染交易槽位物品
     */
    private fun renderTradeSlots() {
        renderTradeSlot(0, tradeSlot0Item)
        renderTradeSlot(1, tradeSlot1Item)
        renderTradeSlot(2, tradeSlot2Item)
    }

    /**
     * 渲染单个交易槽位物品
     * @param index 交易槽位索引
     * @param stack 物品堆叠
     */
    private fun renderTradeSlot(index: Int, stack: ItemStack) {
        val bounds = calculateTradeBounds(index) ?: return
        itemCache.getItemCache(stack.id)?.apply {
            place(bounds)
            render(stack.count)
        }
    }

    /**
     * 渲染鼠标悬停在交易槽位时的物品名称
     */
    private fun renderHoverTradeItemName() {
        // 如果鼠标没有指向任何槽位，直接返回
        val index = mouseTradeIndex ?: return

        // 如果拖动物品不为空，直接返回
        if (!dragWidget.item.isEmpty()) return

        // 获取鼠标指向的物品
        val item = getTradeSlotItem(index)

        // 如果物品为空，直接返回
        if (item.isEmpty()) return

        // 渲染物品名称
        itemCache.getItemCache(item.id)?.renderItemName(mousePosition.x + 30, mousePosition.y - 25)
    }

    override fun doCleanup() = dragWidget.cleanup()

    // endregion
}