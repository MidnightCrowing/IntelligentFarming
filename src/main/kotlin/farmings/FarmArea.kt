package com.midnightcrowing.farmings

import com.midnightcrowing.controllers.FarmAreaController
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.particles.ParticleSystem
import com.midnightcrowing.render.LineRenderer
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT

/**
 * 农场区域类，用于管理农田的布局和作物种植。
 * 继承自[Widget]，支持鼠标事件处理和渲染。
 */
class FarmArea : Widget {
    // region 数据结构 & 配置参数
    /**
     * 网格位置数据类（列，行）
     */
    data class GridPosition(val x: Int, val y: Int)

    private fun GridPosition?.validatePosition(): GridPosition? = this?.takeIf { pos -> isAvailable(pos) }
    private fun GridPosition.isValid() = isAvailable(this)
    private fun GridPosition.hasCrop() = isExist(this)
    private fun GridPosition.crop() = cropsGrid[y][x]

    private val controller: FarmAreaController

    /* 配置参数 */
    private val farmlandBoard: List<Int>    // 农田布局位掩码（每个Int表示一列的可用行）
    private val edgePadding = 20            // 农田区域边缘留白

    /* 布局参数 */
    private lateinit var leftPoint: Point    // 农田左侧基准点
    private lateinit var middlePoint: Point  // 农田中心基准点
    private lateinit var rightPoint: Point   // 农田右侧基准点
    private var blockDeep = 0.0              // 耕地深度
    private var blockHeight = 0.0            // 地块高度
    private val rowCount: Int by lazy { farmlandBoard.maxOf { Integer.SIZE - Integer.numberOfLeadingZeros(it) } }
    private val columnCount: Int by lazy { farmlandBoard.size }

    /* 地块尺寸计算属性 */
    private val leftBlockWidth: Double get() = (middlePoint.x - leftPoint.x) / rowCount
    private val leftBlockHeight: Double get() = (middlePoint.y - leftPoint.y) / rowCount
    private val rightBlockWidth: Double get() = (rightPoint.x - middlePoint.x) / columnCount
    private val rightBlockHeight: Double get() = (middlePoint.y - rightPoint.y) / columnCount

    // endregion

    // region 初始化
    /**
     * 构造函数，基于窗口和农田布局数据初始化。
     * @param window 父窗口
     * @param farmlandBoard 农田布局数据
     */
    constructor(
        window: Window,
        controller: FarmAreaController,
        farmlandBoard: List<Int>,
    ) : super(window) {
        this.farmlandBoard = farmlandBoard
        this.controller = controller
        this.cropsGrid = Array(rowCount) { arrayOfNulls(columnCount) }

        controller.init(this)
    }
    // endregion

    // region 作物管理
    private val cropsGrid: Array<Array<FarmCropBase?>>
    var activeSeedCrop: FarmCropBase? = null         // 当前选中的种子
        set(value) {
            field?.cleanup()
            field = value?.apply { setShadow() }
        }

    /**
     * 在指定位置种植作物。
     * @param pos 作物位置
     */
    private fun plantCropAt(pos: GridPosition) {
        activeSeedCrop?.let { original ->
            original.place(getBlockBounds(pos))
            val newCrop = original.copy().apply { setPlanting() }
            cropsGrid[pos.y][pos.x] = newCrop
            controller.cropInfoController.setFarmCrop(newCrop)
        }
    }

    /**
     * 移除指定位置的作物，并生成粒子效果。
     * @param pos 作物位置
     */
    private fun removeCropWithEffect(pos: GridPosition) {
        pos.crop()?.let { crop ->
            generateParticles(crop, pos)
            crop.cleanup()
        }
        cropsGrid[pos.y][pos.x] = null
        controller.cropInfo.clear()
        controller.cropInfo.setHidden(true)
    }

    /**
     * 生成粒子效果。
     * @param crop 作物对象
     * @param pos 作物位置
     */
    private fun generateParticles(crop: FarmCropBase, pos: GridPosition) {
        crop.nowTextures?.let {
            particleSystem.generateParticles(getBlockBounds(pos).between, it, 40)
        }
    }

    fun plantAllCrops() {
        for (y in 0 until rowCount) {
            for (x in 0 until columnCount) {
                val pos = GridPosition(x, y)
                if (pos.isValid() && !pos.hasCrop()) {
                    plantCropAt(pos)
                }
            }
        }
    }

    fun clearAllCrops() {
        for (y in 0 until rowCount) {
            for (x in 0 until columnCount) {
                val pos = GridPosition(x, y)
                if (pos.hasCrop()) {
                    removeCropWithEffect(pos)
                }
            }
        }
    }

    // endregion

    // region 位置计算 & 坐标转换
    /**
     * 判断指定位置是否可用。
     * @return 如果位置可用且未超出范围，返回true；否则返回false
     */
    private fun isAvailable(pos: GridPosition): Boolean {
        if (pos.x !in 0 until columnCount || pos.y !in 0 until rowCount) return false
        return (farmlandBoard[pos.x] and (1 shl (rowCount - 1 - pos.y))) != 0
    }

    /**
     * 判断指定位置是否已存在作物。
     * @return 如果位置存在作物，返回true；否则返回false
     */
    private fun isExist(pos: GridPosition) = cropsGrid.getOrNull(pos.y)?.getOrNull(pos.x) != null

    /**
     * 获取指定位置的屏幕边界。
     * @return 如果位置可用，返回对应的[ScreenBounds]；否则返回[ScreenBounds.EMPTY]
     */
    private fun getBlockBounds(pos: GridPosition): ScreenBounds {
        if (!pos.isValid()) return ScreenBounds.EMPTY

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
     * @param mouseX 鼠标X坐标
     * @param mouseY 鼠标Y坐标
     * @return 如果找到有效位置，返回对应的(列, 行)索引；否则返回null
     */
    private fun findMouseInField(mouseX: Double, mouseY: Double): GridPosition? {
        val vx = Point(
            (middlePoint.x - rightPoint.x) / columnCount,
            (middlePoint.y - rightPoint.y) / columnCount
        )
        val vy = Point(
            (middlePoint.x - leftPoint.x) / rowCount,
            (middlePoint.y - leftPoint.y) / rowCount
        )

        val dx = middlePoint.x - mouseX
        val dy = middlePoint.y - mouseY

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
    private var hoverPosition: GridPosition? = null  // 当前鼠标悬停位置
    private var isDragging = false                   // 拖动种植状态
    private var lastDragPosition: GridPosition? = null

    override fun onMouseMove(e: MouseMoveEvent) {
        activeSeedCrop ?: return

        // 获取当前鼠标位置并更新悬停状态
        val currentPos = findMouseInField(e.x, e.y).validatePosition()
        updateHoverState(currentPos)

        // 处理拖动种植
        if (isDragging && currentPos != null) {
            handleDragPlanting(currentPos)
        }
    }

    override fun onMouseRelease(e: MouseReleasedEvent) {
        if (e.button == GLFW_MOUSE_BUTTON_RIGHT) {
            isDragging = false
            lastDragPosition = null
        }
    }

    override fun onRightClick(e: MouseRightClickEvent) {
        val currentPos = findMouseInField(e.x, e.y).validatePosition()
        currentPos ?: return

        when {
            currentPos.hasCrop() -> handleDragPlanting(currentPos)
            else -> startDragPlanting(currentPos)
        }
    }

    override fun onClick(e: MouseClickEvent) {
        findMouseInField(e.x, e.y).validatePosition()?.let { pos ->
            if (pos.isValid() && pos.hasCrop()) {
                removeCropWithEffect(pos)
            }
        }
    }

    /**
     * 更新鼠标悬停状态。
     * @param pos 当前鼠标悬停的网格位置，可能为空。
     */
    private fun updateHoverState(pos: GridPosition?) {
        hoverPosition = pos

        when {
            pos == null -> clearHover()
            pos.hasCrop() -> showCropInfo(pos)
            else -> showActiveSeed(pos)
        }
    }

    /**
     * 处理拖动种植作物的逻辑。
     * @param pos 当前拖动到的网格位置。
     */
    private fun handleDragPlanting(pos: GridPosition) {
        if (!pos.hasCrop()) {
            plantCropAt(pos)
        } else {
            pos.crop()?.onFarmRightClick()
        }
        lastDragPosition = pos
    }

    /**
     * 开始拖动种植作物。
     * @param pos 开始拖动的网格位置。
     */
    private fun startDragPlanting(pos: GridPosition) {
        plantCropAt(pos)
        isDragging = true
        lastDragPosition = pos

        controller.cropInfo.setVisible(true)
    }

    /**
     * 清除鼠标悬停状态，隐藏相关 UI 元素。
     */
    private fun clearHover() {
        activeSeedCrop?.setHidden(true)
        controller.cropInfo.setHidden(true)
    }

    /**
     * 显示指定网格位置的作物信息。
     * @param pos 目标网格位置，需包含作物。
     */
    private fun showCropInfo(pos: GridPosition) {
        activeSeedCrop?.setHidden(true)
        controller.cropInfoController.setFarmCrop(pos.crop())
        controller.cropInfo.setHidden(false)
    }

    /**
     * 显示当前选中的种子作物。
     * @param pos 目标网格位置。
     */
    private fun showActiveSeed(pos: GridPosition) {
        activeSeedCrop?.apply {
            setHidden(false)
            place(getBlockBounds(pos))
        }
        controller.cropInfo.setHidden(true)
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

        particleSystem.configure(
            Point(blockHeight / 16 * 15, blockHeight / 64 * 55),
            blockHeight.toInt() / 10
        )
    }

    /**
     * 更新农田和作物的位置。
     */
    fun update() {
        cropsGrid.forEach { row -> row.forEach { it?.update() } }
        controller.cropInfo.update()
        particleSystem.update(0.016f) // Assuming 60 FPS, so deltaTime is approximately 1/60
    }

    /**
     * 更新所有作物的位置。
     */
    private fun updateCropsPosition() {
        for (y in 0 until rowCount) {
            for (x in 0 until columnCount) {
                cropsGrid[y][x]?.place(getBlockBounds(GridPosition(x, y)))
            }
        }
    }

    // endregion

    // region 渲染与清理
    /* 渲染组件 */
    private val borderRenderer = LineRenderer(width = 2.0, color = floatArrayOf(0f, 0f, 0f, 0.7f))

    // 粒子系统，用于生成和管理粒子效果
    private val particleSystem = ParticleSystem()

    /**
     * 渲染农田和作物。
     */
    override fun render() {
        super.render()
        renderBorderline()
        activeSeedCrop?.render()
        cropsGrid.reversed().forEach { row -> row.reversed().forEach { it?.render() } }
        particleSystem.render()
    }

    /**
     * 渲染农田的边界线。
     * 如果鼠标坐标有效，则绘制边界线。
     */
    private fun renderBorderline() {
        if (hoverPosition == null) return

        val point1 = Point(
            middlePoint.x - leftBlockWidth * hoverPosition!!.y + rightBlockWidth * hoverPosition!!.x,
            middlePoint.y - leftBlockHeight * hoverPosition!!.y - rightBlockHeight * hoverPosition!!.x
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
     * 清理农田和作物资源。
     */
    override fun cleanup() {
        super.cleanup()
        activeSeedCrop?.cleanup()
        cropsGrid.forEach { row -> row.forEach { it?.cleanup() } }
    }
    // endregion
}