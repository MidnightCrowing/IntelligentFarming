package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.controllers.TradeController
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.layouts.InventoryLayout
import com.midnightcrowing.gui.publics.DraggingItem
import com.midnightcrowing.model.ScreenBounds
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
        private val BASE_BAG_BAR_BOUNDS = ScreenBounds(108.0, 84.0, 267.0, 135.0)
        private val BASE_QUICK_BAR_BOUNDS = ScreenBounds(108.0, 142.0, 267.0, 157.0)
        private const val BASE_GRID_GAP = 2
        const val BAG_BAR_COL_NUM = 9 // 列数
        const val BAG_BAR_ROW_NUM = 3 // 行数

        // 交易选项和滚动条的坐标
        private val BASE_BUTTON_SLOT_BOUNDS = ScreenBounds(5.0, 18.0, 92.0, 157.0)
        private val BASE_SCROLL_SLOT_BOUNDS = ScreenBounds(94.0, 18.0, 99.0, 157.0)
        private const val BASE_SCROLL_HEIGHT: Double = 27.0

        // 交易物品槽的坐标
        private val BASE_TRADE_SLOT_1_BOUNDS = ScreenBounds(136.0, 37.0, 151.0, 52.0)
        private val BASE_TRADE_SLOT_2_BOUNDS = ScreenBounds(162.0, 37.0, 177.0, 52.0)
        private val BASE_TRADE_SLOT_3_BOUNDS = ScreenBounds(216.0, 34.0, 239.0, 57.0)
        private val BASE_TRADE_ARROW_BOUNDS = ScreenBounds(183.0, 35.0, 210.0, 55.0)

        private val SCALED: Double by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH: Double by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT: Double by lazy { BASE_HEIGHT * SCALED }
        val BAG_BAR_BOUNDS: ScreenBounds by lazy { BASE_BAG_BAR_BOUNDS * SCALED }
        val QUICK_BAR_BOUNDS: ScreenBounds by lazy { BASE_QUICK_BAR_BOUNDS * SCALED }
        val BUTTON_SLOT_BOUNDS: ScreenBounds by lazy { BASE_BUTTON_SLOT_BOUNDS * SCALED }
        val SCROLL_SLOT_BOUNDS: ScreenBounds by lazy { BASE_SCROLL_SLOT_BOUNDS * SCALED }
        val TRADE_SLOT_1_BOUNDS: ScreenBounds by lazy { BASE_TRADE_SLOT_1_BOUNDS * SCALED }
        val TRADE_SLOT_2_BOUNDS: ScreenBounds by lazy { BASE_TRADE_SLOT_2_BOUNDS * SCALED }
        val TRADE_SLOT_3_BOUNDS: ScreenBounds by lazy { BASE_TRADE_SLOT_3_BOUNDS * SCALED }
        val TRADE_ARROW_BOUNDS: ScreenBounds by lazy { BASE_TRADE_ARROW_BOUNDS * SCALED }
        val SCROLL_HEIGHT: Double by lazy { BASE_SCROLL_HEIGHT * SCALED }
        val GRID_GAP: Double by lazy { BASE_GRID_GAP * SCALED }
    }

    // 渲染器 & 组件
    override val renderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.TRADE.texture)
    private val backgroundRender: RectangleRenderer = RectangleRenderer(
        color = floatArrayOf(0f, 0f, 0f, 0.73f),
    )
    private val scrollRender: TextureRenderer = TextureRenderer(TextureResourcesEnum.TRADE_SCROLL_DISABLED.texture)
    private val scrollBounds: ScreenBounds = ScreenBounds.EMPTY
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

    override fun containsPoint(x: Double, y: Double, event: KClass<out Event>?): Boolean = true

    override fun onMousePress(e: MousePressedEvent) = invLayout.onMousePress(e)

    override fun onMouseRelease(e: MouseReleasedEvent) = invLayout.onMouseRelease(e)

    override fun onMouseMove(e: MouseMoveEvent) = invLayout.onMouseMove(e)

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
        backgroundRender.render()         // 渲染背景遮罩
        super.render()                    // 渲染组件
        scrollRender.render(scrollBounds) // 渲染滚动条
        invLayout.render()                // 渲染物品栏布局
        dragWidget.render()               // 渲染拖动物品组件
    }

    override fun cleanup() {
        super.cleanup()
        dragWidget.cleanup()
    }
}