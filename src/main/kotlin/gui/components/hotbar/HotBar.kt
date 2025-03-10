package org.intelligentfarming.gui.components.hotbar

import org.intelligentfarming.gui.GuiBase
import org.intelligentfarming.gui.Window
import org.intelligentfarming.render.Renderer
import org.intelligentfarming.render.Texture
import org.intelligentfarming.resource.Resources

data class GridPosition(val left: Float, val top: Float, val right: Float, val bottom: Float)

class HotBar(window: Window) : GuiBase(window) {
    override val renderer: Renderer

    private val halfBaseWidth = 350
    private val halfBaseHeight = 1100 / 13

    // 预计算缩放因子，避免重复计算
    private val scaleFactorX get() = halfBaseWidth / scaleX
    private val scaleFactorY get() = halfBaseHeight / scaleY

    // 主要坐标计算
    override val left get() = -scaleFactorX
    override val right get() = scaleFactorX
    override val top get() = scaleFactorY - 1
    override val bottom = -1f

    // 预计算 Grid 相关缩放因子
    private val gridFactorX get() = scaleFactorX * (8f / 182)
    private val gridFactorY get() = scaleFactorY * (8f / 44)

    // Grid 坐标计算
    private val gridLeft get() = -(scaleFactorX - gridFactorX)
    private val gridTop get() = (scaleFactorY - gridFactorY) - 1
    private val gridBottom get() = (scaleFactorY - scaleFactorY * 37 / 44) - 1
    private val gridWidth get() = scaleFactorX * (29f / 182)
    private val gridGapWidth get() = scaleFactorX * (11f / 182)

    private val gridBoxBorderHorizontalWidth get() = scaleFactorX * (8f / 182)
    private val gridBoxBorderVerticalWidth get() = scaleFactorY * (33f / 182)

    init {
        val texture = Texture(Resources.COMPONENTS_HOT_BAR.path).apply { load() }
        renderer = Renderer(texture)
    }

    fun getGridPosition(id: Int): GridPosition {
        require(id in 1..9) { "id must be between 1 and 9" }

        val left = gridLeft + (gridWidth + gridGapWidth) * (id - 1)
        return GridPosition(
            left = left,              // 网格左侧 x 坐标
            top = gridTop,            // 顶部 y 坐标
            right = left + gridWidth, // 网格右侧 x 坐标（修正）
            bottom = gridBottom       // 网格间隔宽度
        )
    }

    fun getGridBoxPosition(id: Int): GridPosition {
        require(id in 1..9) { "id must be between 1 and 9" }

        val left = gridLeft + (gridWidth + gridGapWidth) * (id - 1)
        return GridPosition(
            left = left - gridBoxBorderHorizontalWidth,              // 网格左侧 x 坐标
            top = gridTop + gridBoxBorderVerticalWidth,            // 顶部 y 坐标
            right = left + gridWidth + gridBoxBorderHorizontalWidth, // 网格右侧 x 坐标（修正）
            bottom = gridBottom - gridBoxBorderVerticalWidth       // 网格间隔宽度
        )
    }
}