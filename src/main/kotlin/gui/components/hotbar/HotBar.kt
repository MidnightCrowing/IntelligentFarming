package com.midnightcrowing.gui.components.hotbar

import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum


class HotBar(window: Window) : Widget(window) {
    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.COMPONENTS_HOT_BAR.inputStream)
    private val checkBoxRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.CHECK_BOX.inputStream)

    private companion object {
        // 基础尺寸常量
        const val BASE_WIDTH = 364
        const val BASE_HEIGHT = 44
        const val BASE_GRID_LEFT_BORDER = 8
        const val BASE_GRID_TOP_BORDER = 8
        const val BASE_GRID_BOTTOM_BORDER = 37
        const val BASE_GRID_WIDTH = 29
        const val BASE_GRID_GAP = 11
        const val BASE_CHECKBOX_SIZE = 7
        const val SCALE_BASE = 700f

        // 逻辑常量
        const val DEFAULT_SELECT_ID = 1
    }

    // 缩放因子扩展属性（复用缩放逻辑）
    private val Int.scaled: Float get() = this * (SCALE_BASE / BASE_WIDTH)

    // 使用 lazy 延迟计算缩放尺寸
    private val scaledWidth by lazy { BASE_WIDTH.scaled }
    private val scaledHeight by lazy { BASE_HEIGHT.scaled }
    private val gridLeftBorder by lazy { BASE_GRID_LEFT_BORDER.scaled }
    private val gridTopBorder by lazy { BASE_GRID_TOP_BORDER.scaled }
    private val gridBottomBorder by lazy { BASE_GRID_BOTTOM_BORDER.scaled }
    private val gridWidth by lazy { BASE_GRID_WIDTH.scaled }
    private val gridGap by lazy { BASE_GRID_GAP.scaled }
    private val checkboxSize by lazy { BASE_CHECKBOX_SIZE.scaled }

    // NDC 坐标转换（提取公共方法）
    override val screenLeft: Float get() = (window.width - scaledWidth) / 2
    override val screenRight: Float get() = screenLeft + scaledWidth
    override val screenTop: Float get() = window.height - scaledHeight
    override val screenBottom: Float get() = window.height.toFloat()

    // 网格起始坐标
    private val gridStartX: Float get() = screenLeft + gridLeftBorder
    private val gridStartY: Float get() = screenTop + gridTopBorder
    private val gridEndY: Float get() = screenTop + gridBottomBorder

    // 选中网格 ID
    private var selectedGridId: Int = DEFAULT_SELECT_ID
        set(value) {
            require(value in 1..9) { "selectedGridId 必须在 1 到 9 之间" }
            field = value
        }

    // 计算网格边界
    private fun calculateGridBounds(id: Int, expandBy: Float = 0f): ScreenBounds {
        require(id in 1..9) { "id must be between 1 and 9" }

        val startX: Float = gridStartX + (gridWidth + gridGap) * (id - 1) - expandBy
        val endX: Float = startX + gridWidth + expandBy * 2
        val startY: Float = gridStartY - expandBy
        val endY: Float = gridEndY + expandBy

        return ScreenBounds(startX, startY, endX, endY)
    }

    // 获取普通网格位置
    fun getGridBounds(id: Int): ScreenBounds = calculateGridBounds(id)

    // 获取带选中框的网格位置
    fun getGridBoundsWithCheckbox(id: Int): ScreenBounds = calculateGridBounds(id, checkboxSize)

    // 通过坐标获取网格ID
    private fun findGridCheckboxIdAt(x: Float): Int? =
        (1..9).firstOrNull { id ->
            val bounds = getGridBoundsWithCheckbox(id)
            x in bounds.left..bounds.right
        }

    override fun onClick(e: MouseClickEvent) {
        findGridCheckboxIdAt(e.x)?.let { selectedGridId = it }
    }

    override fun render() {
        super.render()

        val checkBoxGridPosition = getGridBoundsWithCheckbox(selectedGridId)
        checkBoxRenderer.render(checkBoxGridPosition.toNdcBounds(window))
    }

    override fun cleanup() {
        super.cleanup()
        checkBoxRenderer.cleanup()
    }
}