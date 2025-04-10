package com.midnightcrowing.gui.publics.compost

import com.midnightcrowing.events.CustomEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.layouts.InventoryLayout
import com.midnightcrowing.gui.publics.DraggingItem
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.block.Composter
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemRenderCache
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.renderer.RectangleRenderer
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import org.lwjgl.nanovg.NanoVG
import kotlin.math.min
import kotlin.reflect.KClass

typealias MaterialGridPosition = Pair<Int, Int>?

class Compost(
    parent: Widget,
    private val controller: CompostController,
    z: Int? = null,
) : Widget(parent, z) {
    companion object {
        private const val BASE_WIDTH = 176
        private const val BASE_HEIGHT = 166
        private const val SCALE_BASE = 600.0
        const val OFFSET_Y = 0 // 向下偏移的距离

        // 文字坐标
        private val BASE_TEXT_TITLE_POINT = Point(9.0, 10.0)
        private val BASE_TEXT_INVENTORY_POINT = Point(8.0, 77.0)

        // 物品背包栏的坐标
        private val BASE_BAG_BAR_BOUNDS = ScreenBounds(8.0, 84.0, 168.0, 136.0)
        private val BASE_QUICK_BAR_BOUNDS = ScreenBounds(8.0, 142.0, 168.0, 158.0)
        private const val BASE_GRID_GAP = 2
        const val BAG_BAR_COL_NUM = 9 // 列数
        const val BAG_BAR_ROW_NUM = 3 // 行数

        // 堆肥桶区域的坐标
        private val BASE_COMPOST_AREA_BOUNDS = ScreenBounds(10.0, 17.0, 62.0, 69.0)

        // 原料和产物槽的坐标
        private val BASE_MATERIAL_BOUNDS = ScreenBounds(64.0, 17.0, 98.0, 69.0)
        private val BASE_PRODUCT_BOUNDS = ScreenBounds(142.5, 33.5, 159.5, 50.5)
        private val BASE_ARROW_BOUNDS = ScreenBounds(106.0, 34.0, 129.0, 50.0)
        const val MATERIAL_COL_NUM = 2 // 列数
        const val MATERIAL_ROW_NUM = 3 // 行数

        private val SCALED: Double by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH: Double by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT: Double by lazy { BASE_HEIGHT * SCALED }
        val TEXT_TITLE_POINT: Point by lazy { BASE_TEXT_TITLE_POINT * SCALED }
        val TEXT_INVENTORY_POINT: Point by lazy { BASE_TEXT_INVENTORY_POINT * SCALED }
        val BAG_BAR_BOUNDS: ScreenBounds by lazy { BASE_BAG_BAR_BOUNDS * SCALED }
        val QUICK_BAR_BOUNDS: ScreenBounds by lazy { BASE_QUICK_BAR_BOUNDS * SCALED }
        val GRID_GAP: Double by lazy { BASE_GRID_GAP * SCALED }
        val COMPOST_AREA_BOUNDS: ScreenBounds by lazy { BASE_COMPOST_AREA_BOUNDS * SCALED }
        val MATERIAL_BOUNDS: ScreenBounds by lazy { BASE_MATERIAL_BOUNDS * SCALED }
        val PRODUCT_BOUNDS: ScreenBounds by lazy { BASE_PRODUCT_BOUNDS * SCALED }
        val ARROW_BOUNDS: ScreenBounds by lazy { BASE_ARROW_BOUNDS * SCALED }
        val MATERIAL_GRID_WIDTH: Double by lazy {
            (MATERIAL_BOUNDS.width - GRID_GAP * (MATERIAL_COL_NUM - 1)) / MATERIAL_COL_NUM
        }
        val MATERIAL_GRID_HEIGHT: Double by lazy {
            (MATERIAL_BOUNDS.height - GRID_GAP * (MATERIAL_ROW_NUM - 1)) / MATERIAL_ROW_NUM
        }

        val COMPOSTER_ORIGIN_OFFSET: Point = Point(-12.0, -13.0) // 堆肥桶的原点距离区域中心点偏移量
    }

    private val Point.isInMaterial: Boolean
        get() = (MATERIAL_BOUNDS + widgetBounds.startPoint).contains(this)

    private val Point.isInProduct: Boolean
        get() = (PRODUCT_BOUNDS + widgetBounds.startPoint).contains(this)

    private val Point.materialGridPosition: MaterialGridPosition
        get() = takeIf { isInMaterial }?.let {
            // 计算点击的格子
            val relativeX = x - widgetBounds.x1 - MATERIAL_BOUNDS.x1
            val relativeY = y - widgetBounds.y1 - MATERIAL_BOUNDS.y1

            (relativeX / MATERIAL_BOUNDS.width * MATERIAL_COL_NUM).toInt() to
                    (relativeY / MATERIAL_BOUNDS.height * MATERIAL_ROW_NUM).toInt()
        }

    private val MaterialGridPosition.materialIndex: Int?
        get() = this?.let { (x, y) -> y * MATERIAL_COL_NUM + x }

    init {
        controller.init(this)

        controller.particleSystem.configure(
            Point(386.0, 163.0) - Point(418.0, 192.0),
            Point(468.0, 174.0) - Point(418.0, 192.0),
        )
    }

    // region 渲染器 & 组件
    override val renderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.GUI_COMPOST.texture)
    private val backgroundRender: RectangleRenderer = RectangleRenderer(
        color = floatArrayOf(0f, 0f, 0f, 0.73f),
    )
    private val compostAreaRender: TextureRenderer = TextureRenderer(TextureResourcesEnum.GUI_COMPOST_ARROW.texture)
    private var progressArrowBounds: ScreenBounds = ScreenBounds.EMPTY

    // 文字渲染器
    private val titleTextRenderer: TextRenderer = TextRenderer.createTextRendererForGUI(window.nvg)
        .apply { text = "堆肥"; textAlign = NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_MIDDLE }
    private val inventoryTextRenderer: TextRenderer = TextRenderer.createTextRendererForGUI(window.nvg)
        .apply { text = "物品栏"; textAlign = NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_MIDDLE }

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

    // endregion

    val composterBlock: Composter = Composter(this)

    // 物品缓存，避免每次渲染时重复创建
    private val itemRenderCache: ItemRenderCache = ItemRenderCache(this)

    // 输入状态
    private var mousePosition: Point = Point.EMPTY
    private var mouseMaterialIndex: Int? = null

    private fun calculateMaterialGridBounds(gridX: Int, gridY: Int): ScreenBounds {
        val x1 = widgetBounds.x1 + MATERIAL_BOUNDS.x1 + gridX * (MATERIAL_GRID_WIDTH + GRID_GAP)
        val y1 = widgetBounds.y1 + MATERIAL_BOUNDS.y1 + gridY * (MATERIAL_GRID_HEIGHT + GRID_GAP)
        val x2 = x1 + MATERIAL_GRID_WIDTH
        val y2 = y1 + MATERIAL_GRID_HEIGHT
        return ScreenBounds(x1, y1, x2, y2)
    }

    private fun calculateProductGridBounds(): ScreenBounds {
        val x1 = widgetBounds.x1 + PRODUCT_BOUNDS.x1
        val y1 = widgetBounds.y1 + PRODUCT_BOUNDS.y1
        val x2 = x1 + PRODUCT_BOUNDS.width
        val y2 = y1 + PRODUCT_BOUNDS.height
        return ScreenBounds(x1, y1, x2, y2)
    }

    override fun update() {
        invLayout.update()

        if (invLayout.isShiftPressed && invLayout.mouseLeftPressed) {
            // 处理原料槽位
            mouseMaterialIndex?.let { index ->
                if (controller.invController.addItem(controller.popItem(index))) {
                    controller.setItem(index, ItemStack.EMPTY)
                }
            }

            // 处理产物槽位
            if (mousePosition.isInProduct) {
                controller.productSlotItem.takeIf { !it.isEmpty() }?.let { productItem ->
                    if (controller.invController.addItem(productItem)) {
                        controller.productSlotItem = ItemStack.EMPTY
                    }
                }
            }
        }
    }

    override fun containsPoint(x: Double, y: Double, event: KClass<out Event>?): Boolean = true

    // region 事件处理
    /**
     * 处理物品交互
     * @param index 原料槽位索引
     * @param exchangeFn 交换函数
     */
    private fun handleItemInteraction(index: Int, exchangeFn: (ItemStack, ItemStack) -> Pair<ItemStack, ItemStack>) {
        val materialItem = controller.popItem(index)
        val dragItem = dragWidget.item

        val (newInvItem, newDragItem) = exchangeFn(materialItem, dragItem)
        controller.setItem(index, newInvItem)
        dragWidget.item = newDragItem

        dragWidget.onParentMouseMove(getCursorPos())
    }

    /**
     * 处理产物槽位交互
     */
    private fun handleProductSlotInteraction() {
        val productItem = controller.productSlotItem
        val dragItem = dragWidget.item

        if (dragItem.isEmpty()) {
            // 拖拽栏为空，直接获取物品
            dragWidget.item = productItem.copy()
            controller.productSlotItem = ItemStack.EMPTY
        } else if (dragItem.id == productItem.id) {
            // 尝试合并相同物品
            val total = dragItem.count + productItem.count
            val max = ItemRegistry.getItemMaxCount(dragItem.id)

            dragItem.count = min(total, max)
            controller.productSlotItem =
                if (total > max) productItem.copy(count = total - max)
                else ItemStack.EMPTY
        }

        // 鼠标位置更新
        dragWidget.onParentMouseMove(getCursorPos())
    }

    override fun onClick(e: CustomEvent.MouseClickEvent) {
        if (invLayout.isShiftPressed) {
            return
        }

        mouseMaterialIndex?.let {
            handleItemInteraction(it, controller.invController::exchangeAndMergeItems)
        }

        if (mousePosition.isInProduct) {
            handleProductSlotInteraction()
        }
    }

    override fun onRightClick(e: CustomEvent.MouseRightClickEvent) {
        mouseMaterialIndex?.let {
            handleItemInteraction(it, controller.invController::rightClickExchangeItems)
        }
    }

    override fun onMousePress(e: CustomEvent.MousePressedEvent) = invLayout.onMousePress(e)

    override fun onMouseRelease(e: CustomEvent.MouseReleasedEvent) = invLayout.onMouseRelease(e)

    override fun onMouseMove(e: CustomEvent.MouseMoveEvent) {
        invLayout.onMouseMove(e)

        val pos = Point(e.x, e.y)
        val gridPos = pos.materialGridPosition

        mousePosition = pos
        mouseMaterialIndex = gridPos.materialIndex

        maskActiveBgBounds = when {
            gridPos != null -> calculateMaterialGridBounds(gridPos.first, gridPos.second)
            pos.isInProduct -> calculateProductGridBounds()
            else -> null
        }
        maskActiveBgBounds?.let {
            maskActiveBgRender.apply { x1 = it.x1; y1 = it.y1; x2 = it.x2 + 1; y2 = it.y2 + 1 }
        }
    }

    // endregion

    // region 位置 & 渲染 & 清理
    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        super.place(x1, y1, x2, y2)

        // 设置背景遮罩的坐标
        backgroundRender.x2 = window.width.toDouble()
        backgroundRender.y2 = window.height.toDouble()

        // 设置进度箭头的坐标
        progressArrowBounds = ARROW_BOUNDS + widgetBounds.startPoint

        // 设置文字坐标
        titleTextRenderer.x = widgetBounds.x1 + TEXT_TITLE_POINT.x
        titleTextRenderer.y = widgetBounds.y1 + TEXT_TITLE_POINT.y
        inventoryTextRenderer.x = widgetBounds.x1 + TEXT_INVENTORY_POINT.x
        inventoryTextRenderer.y = widgetBounds.y1 + TEXT_INVENTORY_POINT.y

        composterBlock.place(COMPOST_AREA_BOUNDS + widgetBounds.startPoint)

        invLayout.place()
    }

    override fun render() {
        if (!isVisible) {
            return
        }
        backgroundRender.render()  // 渲染背景遮罩
        super.render()             // 渲染组件
    }

    override fun doRender() {
        renderProgressArrow()              // 渲染进度箭头
        renderText()                       // 渲染文字
        composterBlock.render()            // 渲染堆肥桶
        invLayout.render()                 // 渲染物品栏布局
        renderCompostItems()               // 渲染堆肥物品
        if (maskActiveBgBounds != null) {
            maskActiveBgRender.render()    // 渲染物品格子高亮
        }
        controller.particleSystem.render() // 渲染粒子效果
        renderHoverCompostItemName()       // 渲染鼠标悬停物品名称
        dragWidget.render()                // 渲染拖动物品组件
    }

    /**
     * 渲染进度箭头
     */
    private fun renderProgressArrow() {
        val progress = (composterBlock.compostLevel / 7.0).coerceIn(0.0, 1.0)

        val x1 = progressArrowBounds.x1
        val y1 = progressArrowBounds.y1
        val x2 = x1 + progressArrowBounds.width * progress
        val y2 = progressArrowBounds.y2

        compostAreaRender.render(
            u1 = 0.0, v1 = 0.0, u2 = progress, v2 = 1.0,
            x1 = x1, y1 = y1, x2 = x2, y2 = y2,
        )
    }

    /**
     * 渲染文字
     */
    private fun renderText() {
        titleTextRenderer.render()
        inventoryTextRenderer.render()
    }

    /**
     * 渲染堆肥物品
     */
    private fun renderCompostItems() {
        // 渲染原料物品
        controller.materialSlotItems.forEachIndexed { index, item ->
            renderItem(item, calculateMaterialGridBounds(index % MATERIAL_COL_NUM, index / MATERIAL_COL_NUM))
        }
        // 渲染产物物品
        renderItem(controller.productSlotItem, calculateProductGridBounds())

        // 清理不在itemList里的物品
        itemRenderCache.retainAll(
            buildSet {
                controller.materialSlotItems.mapTo(this) { it.id }
                add(controller.productSlotItem.id)
            }
        )
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
     * 渲染鼠标悬停在原料和产物槽位时的物品名称
     */
    private fun renderHoverCompostItemName() {
        // 如果拖动物品不为空，直接返回
        if (!dragWidget.item.isEmpty()) return

        // 获取鼠标指向的物品，如果鼠标没有指向任何槽位，直接返回
        val item = when {
            mouseMaterialIndex != null -> controller.getItem(mouseMaterialIndex!!)
            mousePosition.isInProduct -> controller.productSlotItem
            else -> return
        }

        // 如果物品为空，直接返回
        if (item.isEmpty()) return

        // 渲染物品名称
        itemRenderCache.getItemCache(item.id)?.renderTooltip(mousePosition.x, mousePosition.y)
    }

    override fun doCleanup() {
        composterBlock.cleanup()
        compostAreaRender.cleanup()
        dragWidget.cleanup()
    }

    // endregion
}