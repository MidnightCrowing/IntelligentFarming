package com.midnightcrowing.gui

import com.midnightcrowing.controllers.HotBarController
import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.events.Event.WindowResizeEvent
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.scenes.FarmScene


class HotBar(val screen: FarmScene) : Widget(screen.window) {
    companion object {
        // 基础尺寸常量
        private const val BASE_WIDTH = 364
        private const val BASE_HEIGHT = 44
        private const val BASE_GRID_LEFT_BORDER = 8
        private const val BASE_GRID_TOP_BORDER = 8
        private const val BASE_GRID_BOTTOM_BORDER = 37
        private const val BASE_GRID_WIDTH = 29
        private const val BASE_GRID_GAP = 11
        private const val BASE_CHECKBOX_SIZE = 7
        private const val SCALE_BASE = 700f

        // 计算缩放比例
        private val SCALED: Float by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT by lazy { BASE_HEIGHT * SCALED }
        val GRID_LEFT_BORDER by lazy { BASE_GRID_LEFT_BORDER * SCALED }
        val GRID_TOP_BORDER by lazy { BASE_GRID_TOP_BORDER * SCALED }
        val GRID_BOTTOM_BORDER by lazy { BASE_GRID_BOTTOM_BORDER * SCALED }
        val GRID_WIDTH by lazy { BASE_GRID_WIDTH * SCALED }
        val GRID_GAP by lazy { BASE_GRID_GAP * SCALED }
        val CHECKBOX_SIZE by lazy { BASE_CHECKBOX_SIZE * SCALED }

        // 逻辑常量
        const val DEFAULT_SELECT_ID = 0
    }

    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.COMPONENTS_HOT_BAR.inputStream)

    val controller = HotBarController(this)

    // 网格起始坐标
    private var gridStartX: Float = 0f
    private var gridStartY: Float = 0f
    private var gridEndY: Float = 0f

    override fun onWindowResize(e: WindowResizeEvent) = updatePlace()

    override fun place(x1: Float, y1: Float, x2: Float, y2: Float) {
        super.place(x1, y1, x2, y2)
        updatePlace()
        screen.itemCheckBox.place(getGridBoundsWithCheckbox(selectedGridId))
    }

    fun updatePlace() {
        gridStartX = widgetBounds.x1 + GRID_LEFT_BORDER
        gridStartY = widgetBounds.y1 + GRID_TOP_BORDER
        gridEndY = widgetBounds.y1 + GRID_BOTTOM_BORDER
    }

    // 选中网格 ID
    private var selectedGridId: Int = DEFAULT_SELECT_ID
        set(value) {
            require(value in 0..8) { "selectedGridId 必须在 0 到 8 之间" }
            field = value
        }

    // 计算网格边界
    private fun calculateGridBounds(id: Int, expandBy: Float = 0f): ScreenBounds {
        require(id in 0..8) { "id must be between 0 and 8" }

        val startX: Float = gridStartX + (GRID_WIDTH + GRID_GAP) * id - expandBy
        val endX: Float = startX + GRID_WIDTH + expandBy * 2
        val startY: Float = gridStartY - expandBy
        val endY: Float = gridEndY + expandBy

        return ScreenBounds(startX, startY, endX, endY)
    }

    // 获取普通网格位置
    fun getGridBounds(id: Int): ScreenBounds = calculateGridBounds(id)

    // 获取带选中框的网格位置
    fun getGridBoundsWithCheckbox(id: Int): ScreenBounds = calculateGridBounds(id, CHECKBOX_SIZE)

    // 通过坐标获取网格ID
    private fun findGridCheckboxIdAt(x: Float): Int? =
        (0..8).firstOrNull { id ->
            val bounds = getGridBoundsWithCheckbox(id)
            x in bounds.x1..bounds.x2
        }

    override fun onClick(e: MouseClickEvent) {
        findGridCheckboxIdAt(e.x)?.let {
            selectedGridId = it
            screen.itemCheckBox.place(getGridBoundsWithCheckbox(it))
            controller.changeActiveItem(it)
        }
    }

    override fun render() {
        super.render()

        for ((index, item) in controller.itemsList) {
            item?.place(getGridBounds(index))
            item?.render()
        }
    }
}