package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.controllers.TradeController
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.gui.bases.RadioButtonGroup
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.layouts.InventoryLayout
import com.midnightcrowing.gui.publics.DraggingItem
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemCache
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.renderer.RectangleRenderer
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE
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

        // 文字坐标
        private val BASE_TEXT_TITLE_POINT = Point(188.0, 10.5)
        private val BASE_TEXT_TRADE_POINT = Point(47.5, 10.5)
        private val BASE_TEXT_INVENTORY_POINT = Point(107.0, 76.0)

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
        val TEXT_TITLE_POINT: Point by lazy { BASE_TEXT_TITLE_POINT * SCALED }
        val TEXT_TRADE_POINT: Point by lazy { BASE_TEXT_TRADE_POINT * SCALED }
        val TEXT_INVENTORY_POINT: Point by lazy { BASE_TEXT_INVENTORY_POINT * SCALED }
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
        get() = (SCROLL_SLOT_BOUNDS + widgetBounds.startPoint).contains(this)

    private val Point.scrollPercent: Double
        get() = (getCursorPos().second - (SCROLL_SLOT_BOUNDS + widgetBounds.startPoint).y1) / SCROLL_SLOT_BOUNDS.height

    private val Point.isInButtonSlot: Boolean
        get() = (BUTTON_SLOT_BOUNDS + widgetBounds.startPoint).contains(this)

    init {
        controller.init(this)
    }

    // region 渲染器 & 组件
    override val renderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.TRADE.texture)
    private val backgroundRender: RectangleRenderer = RectangleRenderer(
        color = floatArrayOf(0f, 0f, 0f, 0.73f),
    )

    // 文字渲染器
    private val titleTextRenderer: TextRenderer = TextRenderer.createTextRendererForGUI(window.nvg).apply {
        text = "农民（奸商）"
    }
    private val tradeTextRenderer: TextRenderer = TextRenderer.createTextRendererForGUI(window.nvg).apply {
        text = "交易"
    }
    private val inventoryTextRenderer: TextRenderer = TextRenderer.createTextRendererForGUI(window.nvg).apply {
        text = "物品栏"; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }

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

    // 交易列表按钮
    private val tradeButtons: List<TradeButton> = controller.tradeList.map { tradeRecipe ->
        TradeButton(parent = this, tradeRecipe)
    }
    private var renderTradeButtons: List<TradeButton> = tradeButtons.take(BUTTON_NUM)  // 渲染出来的交易按钮
    val tradeButtonGroup: RadioButtonGroup = RadioButtonGroup().apply {
        tradeButtons.forEach { addButton(it) }
        updateSelectCallback = { controller.updateTrade() }
    }
    private var tradeButtonStartIndex: Int = 0
        set(value) {
            field = value
            renderTradeButtons = tradeButtons.onEach { it.setHidden(true) }
                .drop(value)
                .take(BUTTON_NUM)
                .onEach { it.setHidden(false) }
        }

    // 交易列表滚动条
    private val scrollRender: TextureRenderer = TextureRenderer(
        if (controller.tradeList.size > BUTTON_NUM) {
            TextureResourcesEnum.TRADE_SCROLL_ACTIVE.texture
        } else {
            TextureResourcesEnum.TRADE_SCROLL_DISABLED.texture
        }
    )
    private val scrollBounds: ScreenBounds = ScreenBounds.EMPTY
    private val buttonScrollStep: Double = 1.0 / maxOf(1, tradeButtons.size - BUTTON_NUM + 1)  // 用于控制按钮滚动
    private val scrollbarScrollStep: Double = 1.0 / maxOf(1, tradeButtons.size - BUTTON_NUM)   // 用于控制自身滚动

    // endregion

    // 槽位物品
    var tradeSlot0Item: ItemStack = ItemStack.EMPTY
    var tradeSlot1Item: ItemStack = ItemStack.EMPTY
    var tradeSlot2Item: ItemStack = ItemStack.EMPTY

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
        controller.updateTrade()
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

    override fun update() {
        invLayout.update()

        tradeButtons.forEach { button -> button.update() }

        if (invLayout.isShiftPressed && invLayout.mouseLeftPressed) {
            mouseTradeIndex
                ?.takeIf { it != 2 }
                ?.let { index ->
                    val tradeSlotItem = getTradeSlotItem(index)
                    val result = controller.invController.addItem(tradeSlotItem)

                    if (result) {
                        setTradeSlotItem(index, ItemStack.EMPTY)
                    }
                }
        }
    }

    fun clear() {
        tradeButtonStartIndex = 0
        tradeButtonGroup.clear()
        controller.updateTrade()
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

    /**
     * 处理交易槽位的交互
     */
    private fun handleTradeSlotInteraction() = controller.executeTrade { sellItem ->
        when {
            // 拖拽栏为空，直接放置物品
            dragWidget.item.isEmpty() -> {
                dragWidget.item = sellItem.copy()
                dragWidget.onParentMouseMove(getCursorPos())
                true
            }

            // 拖拽栏已有相同物品，尝试叠加
            dragWidget.item.id == sellItem.id -> {
                val totalCount = dragWidget.item.count + sellItem.count
                if (totalCount <= 64) {
                    dragWidget.item.count = totalCount
                    dragWidget.onParentMouseMove(getCursorPos())
                    true
                } else {
                    false // 数量超出最大限制
                }
            }

            // 类型不匹配，无法叠加
            else -> false
        }
    }

    override fun onClick(e: MouseClickEvent) {
        if (invLayout.isShiftPressed) {
            return
        }

        val pos = Point(e.x, e.y)

        pos.isInTradeSlot?.let {
            if (it == 2) {
                handleTradeSlotInteraction()
            } else {
                handleItemDragAndDrop(it)
            }
        }
    }

    override fun onMousePress(e: MousePressedEvent) = invLayout.onMousePress(e)

    override fun onMouseRelease(e: MouseReleasedEvent) = invLayout.onMouseRelease(e)

    override fun onMouseMove(e: MouseMoveEvent) {
        invLayout.onMouseMove(e)

        val pos = Point(e.x, e.y)
        mousePosition = pos

        // 计算鼠标是否在交易槽位上
        mouseTradeIndex = pos.isInTradeSlot
        maskActiveBgBounds = mouseTradeIndex?.let { calculateTradeBounds(it) }
        maskActiveBgBounds?.let {
            maskActiveBgRender.apply { x1 = it.x1; y1 = it.y1; x2 = it.x2 + 1; y2 = it.y2 + 1 }
        }

        if (invLayout.mouseLeftPressed && pos.isInScrollSlot) {
            tradeButtonStartIndex = (pos.scrollPercent / buttonScrollStep).toInt()
        }

        renderTradeButtons
            .filter { button -> button.containsPoint(e.x, e.y, event = MouseMoveEvent::class) }
            .forEach { button -> button.onParentMouseMove(e) }
    }

    override fun onMouseScroll(e: MouseScrollEvent) {
        if (mousePosition.isInButtonSlot or mousePosition.isInScrollSlot) {
            tradeButtonStartIndex = (tradeButtonStartIndex - e.offsetY.toInt())
                .coerceAtLeast(0)
                .coerceAtMost(tradeButtons.size - BUTTON_NUM)
        }
    }

    // endregion

    // region 位置 & 渲染
    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        super.place(x1, y1, x2, y2)

        // 设置背景遮罩的坐标
        backgroundRender.x2 = window.width.toDouble()
        backgroundRender.y2 = window.height.toDouble()

        // 设置文字坐标
        titleTextRenderer.x = widgetBounds.x1 + TEXT_TITLE_POINT.x
        titleTextRenderer.y = widgetBounds.y1 + TEXT_TITLE_POINT.y
        tradeTextRenderer.x = widgetBounds.x1 + TEXT_TRADE_POINT.x
        tradeTextRenderer.y = widgetBounds.y1 + TEXT_TRADE_POINT.y
        inventoryTextRenderer.x = widgetBounds.x1 + TEXT_INVENTORY_POINT.x
        inventoryTextRenderer.y = widgetBounds.y1 + TEXT_INVENTORY_POINT.y

        invLayout.place()
    }

    override fun render() {
        if (!isVisible) {
            return
        }
        backgroundRender.render()          // 渲染背景遮罩
        super.render()                     // 渲染组件
        renderText()                       // 渲染文字
        scrollRender.render(scrollBounds)  // 渲染滚动条
        invLayout.render()                 // 渲染物品栏布局
        renderTradeSlots()                 // 渲染交易槽位物品
        renderTradeScroll()                // 渲染交易列表滚动条
        renderTradeButtons()               // 渲染交易按钮
        if (maskActiveBgBounds != null) {
            maskActiveBgRender.render()    // 渲染物品格子高亮
        }
        renderHoverTradeItemName()         // 渲染鼠标悬停在交易槽位时的物品名称
        dragWidget.render()                // 渲染拖动物品组件
    }

    /**
     * 渲染文字
     */
    private fun renderText() {
        titleTextRenderer.render()
        tradeTextRenderer.render()
        inventoryTextRenderer.render()
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
     * 渲染交易列表滚动条
     */
    private fun renderTradeScroll() {
        val percent = tradeButtonStartIndex * scrollbarScrollStep
        val scrollSlotBounds = SCROLL_SLOT_BOUNDS + widgetBounds.startPoint
        val yOffset = (scrollSlotBounds.height - SCROLL_HEIGHT) * percent

        val x1 = scrollSlotBounds.x1
        val x2 = scrollSlotBounds.x2
        val y1 = scrollSlotBounds.y1 + yOffset
        val y2 = y1 + SCROLL_HEIGHT

        scrollRender.render(x1, y1, x2, y2)
    }

    /**
     * 渲染交易按钮
     */
    private fun renderTradeButtons() {
        renderTradeButtons.forEachIndexed { index, button ->
            button.place(calculateButtonBounds(index))
            button.render()
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
        itemCache.getItemCache(item.id)?.renderTooltip(mousePosition.x, mousePosition.y, position = "after-top")
    }

    override fun doCleanup() = dragWidget.cleanup()

    // endregion
}