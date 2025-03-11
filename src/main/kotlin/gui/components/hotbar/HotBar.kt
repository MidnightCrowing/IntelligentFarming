package com.midnightcrowing.gui.components.hotbar

import com.midnightcrowing.events.listeners.ClickEvent
import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import com.midnightcrowing.render.Renderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.utils.WindowUtils.convertScreenToNdcBounds
import com.midnightcrowing.utils.WindowUtils.convertScreenToNdcX
import com.midnightcrowing.utils.WindowUtils.convertScreenToNdcY
import com.midnightcrowing.utils.CoordinateSystem as CoordSys

data class GridBounds(val left: Float, val top: Float, val right: Float, val bottom: Float)

class HotBar(window: Window) : Widget(window) {
    override val renderer: Renderer = getRenderer(ResourcesEnum.COMPONENTS_HOT_BAR.path)
    private val checkBoxRenderer: Renderer = getRenderer(ResourcesEnum.CHECK_BOX.path)

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

    // 屏幕坐标计算属性
    private val screenX1 get() = (window.width - scaledWidth) / 2f
    private val screenY1 get() = window.height - scaledHeight
    private val screenX2 get() = screenX1 + scaledWidth

    // NDC 坐标转换（提取公共方法）
    override val left: Float get() = convertScreenToNdcX(window, screenX1)
    override val right: Float get() = convertScreenToNdcX(window, screenX2)
    override val top: Float get() = convertScreenToNdcY(window, screenY1)
    override val bottom: Float = -1f

    // 网格起始坐标
    private val gridStartX: Float get() = screenX1 + gridLeftBorder
    private val gridStartY: Float get() = screenY1 + gridTopBorder
    private val gridEndY: Float get() = screenY1 + gridBottomBorder

    // 选中网格 ID
    private var selectedGridId: Int = DEFAULT_SELECT_ID
        set(value) {
            require(value in 1..9) { "selectedGridId 必须在 1 到 9 之间" }
            field = value
        }

    // 计算网格边界
    private fun calculateGridBounds(id: Int, expandBy: Float = 0f, coordSys: CoordSys): GridBounds {
        require(id in 1..9) { "id must be between 1 and 9" }

        val startX = gridStartX + (gridWidth + gridGap) * (id - 1) - expandBy
        val endX = startX + gridWidth + expandBy * 2
        val startY = gridStartY - expandBy
        val endY = gridEndY + expandBy

        return if (coordSys == CoordSys.SCREEN) {
            GridBounds(startX, startY, endX, endY)
        } else {
            val (left, top, right, bottom) = convertScreenToNdcBounds(window, startX, startY, endX, endY)
            GridBounds(left, top, right, bottom)
        }
    }

    // 获取普通网格位置
    fun getGridBounds(id: Int, coordSys: CoordSys = CoordSys.NDC): GridBounds =
        calculateGridBounds(id, coordSys = coordSys)

    // 获取带选中框的网格位置
    fun getGridBoundsWithCheckbox(id: Int, coordSys: CoordSys = CoordSys.NDC): GridBounds =
        calculateGridBounds(id, expandBy = checkboxSize, coordSys = coordSys)

    // 通过坐标获取网格ID
    private fun findGridIdAt(x: Float, y: Float): Int? =
        (1..9).firstOrNull { id ->
            val bounds = getGridBoundsWithCheckbox(id, CoordSys.SCREEN)
            x in bounds.left..bounds.right && y in bounds.top..bounds.bottom
        }

    override fun onClick(e: ClickEvent) {
        findGridIdAt(e.x, e.y)?.let { selectedGridId = it }
    }

    override fun render() {
        super.render()

        val checkBoxGridPosition = getGridBoundsWithCheckbox(selectedGridId)
        checkBoxRenderer.render(
            checkBoxGridPosition.left,
            checkBoxGridPosition.top,
            checkBoxGridPosition.right,
            checkBoxGridPosition.bottom
        )
    }

    override fun cleanup() {
        super.cleanup()
        checkBoxRenderer.cleanup()
    }
}