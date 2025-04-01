package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.controllers.InventoryController
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

class Inventory(
    val screen: FarmScene,
    private val controller: InventoryController,
    z: Int? = null,
) : Widget(screen, z) {
    companion object {
        private const val BASE_WIDTH = 352
        private const val BASE_HEIGHT = 198
        private const val SCALE_BASE = 700.0
        const val OFFSET_Y = 40 // 向下偏移的距离

        private val BASE_BAG_BAR_BOUNDS = ScreenBounds(16.0, 34.0, 335.0, 137.0)
        private val BASE_QUICK_BAR_BOUNDS = ScreenBounds(16.0, 150.0, 335.0, 181.0)
        private const val BASE_GRID_GAP = 4
        const val BAG_BAR_COL_NUM = 9 // 列数
        const val BAG_BAR_ROW_NUM = 3 // 行数

        private val SCALED: Double by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH: Double by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT: Double by lazy { BASE_HEIGHT * SCALED }
        val BAG_BAR_BOUNDS: ScreenBounds by lazy { BASE_BAG_BAR_BOUNDS * SCALED }
        val QUICK_BAR_BOUNDS: ScreenBounds by lazy { BASE_QUICK_BAR_BOUNDS * SCALED }
        val GRID_GAP: Double by lazy { BASE_GRID_GAP * SCALED }
    }

    init {
        controller.init(this)
    }

    // 渲染器 & 组件
    override val renderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.INVENTORY.texture)
    private val backgroundRender: RectangleRenderer = RectangleRenderer(
        color = floatArrayOf(0f, 0f, 0f, 0.73f),
    )
    private val dragWidget: DraggingItem = DraggingItem(this)
    private val invLayout: InventoryLayout = InventoryLayout(
        parent = this,
        controller = controller,
        dragWidget = dragWidget,
        bagBarBounds = BAG_BAR_BOUNDS,
        quickBarBounds = QUICK_BAR_BOUNDS,
        gridGap = GRID_GAP,
        bagBarColNum = BAG_BAR_COL_NUM,
        bagBarRowNum = BAG_BAR_ROW_NUM,
    )

    fun update() = invLayout.update()

    override fun containsPoint(x: Double, y: Double, event: KClass<out Event>?): Boolean = true

    override fun onClick(e: MouseClickEvent) = super.onClick(e)

    override fun onRightClick(e: MouseRightClickEvent) = super.onRightClick(e)

    override fun onMousePress(e: MousePressedEvent) = invLayout.onMousePress(e)

    override fun onMouseRelease(e: MouseReleasedEvent) = invLayout.onMouseRelease(e)

    override fun onMouseMove(e: MouseMoveEvent) = invLayout.onMouseMove(e)

    override fun onMouseLeave() = super.onMouseLeave()

    override fun onMouseScroll(e: MouseScrollEvent) = super.onMouseScroll(e)

    override fun onKeyPress(e: KeyPressedEvent): Boolean = true

    override fun onKeyReleased(e: KeyReleasedEvent): Boolean = true

    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        super.place(x1, y1, x2, y2)
        backgroundRender.x2 = window.width.toDouble()
        backgroundRender.y2 = window.height.toDouble()
        invLayout.place()
    }

    override fun render() {
        if (!isVisible) {
            return
        }
        backgroundRender.render()  // 渲染背景遮罩
        super.render()             // 渲染组件
        invLayout.render()         // 渲染物品栏布局
        dragWidget.render()        // 渲染拖动物品组件
    }

    override fun cleanup() {
        super.cleanup()
        dragWidget.cleanup()
    }
}