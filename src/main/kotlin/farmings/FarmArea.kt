package com.midnightcrowing.farmings

import com.midnightcrowing.controllers.FarmAreaController
import com.midnightcrowing.controllers.FarmAreaController.GridPosition
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemRenderCache
import com.midnightcrowing.renderer.ItemRenderer
import com.midnightcrowing.renderer.LineRenderer
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT

/**
 * 农场区域类，用于管理农田的布局和作物种植。
 * 继承自[Widget]，支持鼠标事件处理和渲染。
 */
class FarmArea(
    parent: Widget,
    private val controller: FarmAreaController,
    farmlandBoard: List<Int>,
    z: Int? = null,
) : Widget(parent, z) {
    // region 配置参数
    /* 布局参数 */
    private lateinit var leftPoint: Point    // 农田左侧基准点
    private lateinit var middlePoint: Point  // 农田中心基准点
    private lateinit var rightPoint: Point   // 农田右侧基准点
    private val edgePadding = 20             // 农田区域边缘留白
    private var blockDeep = 0.0              // 耕地深度
    private var blockHeight = 0.0            // 地块高度
    private val rowCount: Int = farmlandBoard.maxOf { Integer.SIZE - Integer.numberOfLeadingZeros(it) }
    private val columnCount: Int = farmlandBoard.size

    /* 地块尺寸计算属性 */
    private val leftBlockWidth: Double get() = (middlePoint.x - leftPoint.x) / rowCount
    private val leftBlockHeight: Double get() = (middlePoint.y - leftPoint.y) / rowCount
    private val rightBlockWidth: Double get() = (rightPoint.x - middlePoint.x) / columnCount
    private val rightBlockHeight: Double get() = (middlePoint.y - rightPoint.y) / columnCount

    // endregion

    init {
        controller.init(this, farmlandBoard, rowCount, columnCount)
    }

    var mousePosition: Point? = null

    // 缓存物品，最多缓存 10 个物品
    var itemRenderCache: ItemRenderCache = ItemRenderCache(this, maxSize = 10)
    var handheldItemRenderer: ItemRenderer? = null

    // region 位置计算 & 坐标转换
    /**
     * 获取指定位置的屏幕边界。
     * @return 如果位置可用，返回对应的[ScreenBounds]；否则返回[ScreenBounds.EMPTY]
     */
    fun getBlockBounds(pos: GridPosition): ScreenBounds {
        if (!controller.isAvailable(pos)) return ScreenBounds.EMPTY

        val (x, y) = pos
        return ScreenBounds(
            x1 = middlePoint.x - leftBlockWidth * (y + 1) + rightBlockWidth * x,
            x2 = middlePoint.x - leftBlockWidth * y + rightBlockWidth * (x + 1),
            y1 = middlePoint.y - blockHeight - leftBlockHeight * (y + 1) - rightBlockHeight * (x + 1),
            y2 = middlePoint.y - leftBlockHeight * y - rightBlockHeight * x
        )
    }

    /**
     * 根据鼠标坐标查找对应的农田位置。
     * @param pos 鼠标坐标
     * @return 如果找到有效位置，返回对应的(列, 行)索引；否则返回null
     */
    private fun findMouseInField(pos: Point): GridPosition? {
        val vx: Point = (middlePoint - rightPoint) / columnCount
        val vy: Point = (middlePoint - leftPoint) / rowCount

        val dx: Double = middlePoint.x - pos.x
        val dy: Double = middlePoint.y - pos.y

        val determinant = vx.x * vy.y - vx.y * vy.x
        if (determinant == 0.0) return null

        val col = (vy.y * dx - vy.x * dy) / determinant
        val row = (-vx.y * dx + vx.x * dy) / determinant
        if (col < 0 || row < 0) return null

        val x = col.toInt()
        val y = row.toInt()
        return if (x in 0 until columnCount && y in 0 until rowCount) GridPosition(x, y) else null
    }

    // endregion

    // region 鼠标事件处理
    /* 交互状态 */
    override fun onRightClick(e: MouseRightClickEvent) = controller.handleRightClick()

    override fun onMouseMove(e: MouseMoveEvent) {
        val pos = Point(e.x, e.y)
        mousePosition = pos
        controller.mouseGridPosition = findMouseInField(pos)?.takeIf { controller.isAvailable(it) }
    }

    override fun onMousePress(e: MousePressedEvent) {
        controller.isLeftClick = e.button == GLFW_MOUSE_BUTTON_LEFT
        controller.isRightClick = e.button == GLFW_MOUSE_BUTTON_RIGHT
    }

    override fun onMouseRelease(e: MouseReleasedEvent) {
        when (e.button) {
            GLFW_MOUSE_BUTTON_LEFT -> controller.isLeftClick = false
            GLFW_MOUSE_BUTTON_RIGHT -> controller.isRightClick = false
        }
    }

    // endregion

    // region 更新
    /**
     * 设置农田的布局参数并更新作物位置。
     * @param blockDeep 农田块的深度
     * @param blockHeight 农田块的高度
     * @param leftPoint 左侧点坐标
     * @param middlePoint 中心点坐标
     * @param rightPoint 右侧点坐标
     */
    fun place(blockDeep: Double, blockHeight: Double, leftPoint: Point, middlePoint: Point, rightPoint: Point) {
        this.blockDeep = blockDeep
        this.blockHeight = blockHeight
        this.leftPoint = leftPoint
        this.middlePoint = middlePoint
        this.rightPoint = rightPoint

        super.place(
            x1 = leftPoint.x - edgePadding,
            x2 = rightPoint.x + edgePadding,
            y1 = leftPoint.y - middlePoint.y + rightPoint.y - edgePadding,
            y2 = middlePoint.y + edgePadding
        )

        updateCropsPosition()

        controller.blockParticleSystem.configure(
            Point(blockHeight / 16 * 15, blockHeight / 64 * 55),
            blockHeight.toInt() / 10
        )
        controller.cropGrowthParticleSystem.configure(
            Point(blockHeight / 16 * 15, blockHeight / 64 * 55),
            blockHeight.toInt() / 10
        )
    }

    /**
     * 更新农田和作物的位置。
     */
    override fun update() {
        controller.cropsGrid.forEach { row -> row.forEach { it?.update() } }
    }

    /**
     * 更新所有作物的位置。
     */
    private fun updateCropsPosition() {
        for (y in 0 until rowCount) {
            for (x in 0 until columnCount) {
                controller.cropsGrid[y][x]?.place(getBlockBounds(GridPosition(x, y)))
            }
        }
    }

    // endregion

    // region 渲染与清理
    /* 渲染组件 */
    private val borderRenderer = LineRenderer(width = 2.0, color = floatArrayOf(0f, 0f, 0f, 0.7f))

    /**
     * 渲染农田和作物。
     */
    override fun doRender() {
        renderBorderline()
        controller.activeSeedCrop?.render()
        controller.cropsGrid.reversed().forEach { row -> row.reversed().forEach { it?.render() } }
        controller.blockParticleSystem.render()
        controller.cropGrowthParticleSystem.render()
        renderHandheldItem()
    }

    /**
     * 渲染农田的边界线。
     * 如果鼠标坐标有效，则绘制边界线。
     */
    private fun renderBorderline() {
        val mousePosition = controller.mouseGridPosition
        if (mousePosition == null) return

        val point1 = Point(
            middlePoint.x - leftBlockWidth * mousePosition.y + rightBlockWidth * mousePosition.x,
            middlePoint.y - leftBlockHeight * mousePosition.y - rightBlockHeight * mousePosition.x
        )
        val point2 = Point(point1.x - leftBlockWidth, point1.y - leftBlockHeight)
        val point3 = Point(point1.x + rightBlockWidth, point1.y - rightBlockHeight)
        val point4 = Point(point2.x + rightBlockWidth, point2.y - rightBlockHeight)

        val points = listOf(point1, point2, point4, point3, point1) // 形成闭环

        for (i in 0 until 4) {
            borderRenderer.x1 = points[i].x
            borderRenderer.y1 = points[i].y
            borderRenderer.x2 = points[i + 1].x
            borderRenderer.y2 = points[i + 1].y
            borderRenderer.render()
        }
    }

    /**
     * 渲染手持物品。
     *
     * 如果鼠标坐标和手持物品渲染器有效，则绘制手持物品。
     */
    private fun renderHandheldItem() {
        if (controller.mouseGridPosition == null || mousePosition == null || handheldItemRenderer == null) {
            return
        }

        handheldItemRenderer?.place(
            mousePosition!!.x + 7,
            mousePosition!!.y + 3,
            mousePosition!!.x + 47,
            mousePosition!!.y + 43
        )
        handheldItemRenderer?.render()
    }

    /**
     * 清理农田和作物资源。
     */
    override fun doCleanup() {
        controller.activeSeedCrop?.cleanup()
        controller.cropsGrid.forEach { row -> row.forEach { it?.cleanup() } }
    }
    // endregion
}