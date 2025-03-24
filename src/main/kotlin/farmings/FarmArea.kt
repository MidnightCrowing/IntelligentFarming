package com.midnightcrowing.farmings

import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.farmings.crops.FarmCropBase
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.particles.ParticleSystem
import com.midnightcrowing.render.LineRenderer

/**
 * 农场区域类，用于管理农田的布局和作物种植。
 * 继承自[Widget]，支持鼠标事件处理和渲染。
 */
class FarmArea : Widget {
    private val lineRenderer: LineRenderer = LineRenderer(width = 2.0, color = floatArrayOf(0f, 0f, 0f, 0.7f))

    private val farmlandBoard: List<Int> // 农田布局数据，每个整数表示一列的可用地块
    private val rowCount: Int // 农田的行数
    private val columnCount: Int // 农田的列数
    private val cropsGrid: Array<Array<FarmCropBase?>> // 作物网格，存储每个地块上的作物

    private companion object {
        const val SPARE_AREA_WIDTH = 20 // 农田边缘留白宽度
    }

    // 布局参数
    private var middlePoint = Point.EMPTY // 农田中心点坐标
    private var leftPoint = Point.EMPTY // 农田左侧点坐标
    private var rightPoint = Point.EMPTY // 农田右侧点坐标
    private var blockHeight = 0.0 // 农田块的高度
    private var blockDeep = 0.0 // 农田块的深度

    // 计算属性：农田块的宽度和高度
    private val blockLeftWidth: Double get() = (middlePoint.x - leftPoint.x) / rowCount
    private val blockLeftHeight: Double get() = (middlePoint.y - leftPoint.y) / rowCount
    private val blockRightWidth: Double get() = (rightPoint.x - middlePoint.x) / columnCount
    private val blockRightHeight: Double get() = (middlePoint.y - rightPoint.y) / columnCount

    private var mouseX: Int? = null
    private var mouseY: Int? = null

    /**
     * 当前激活的种子作物。
     * 设置新值时，会清理旧作物并为其设置阴影。
     */
    var activeSeedCrop: FarmCropBase? = null
        set(value) {
            field?.cleanup()
            field = value?.apply { setShadow() }
        }

    /**
     * 构造函数，基于窗口和农田布局数据初始化。
     * @param window 父窗口
     * @param farmlandBoard 农田布局数据
     */
    constructor(window: Window, farmlandBoard: List<Int>) : super(window) {
        this.farmlandBoard = farmlandBoard
        rowCount = calculateRowCount()
        columnCount = farmlandBoard.size
        cropsGrid = Array(rowCount) { arrayOfNulls(columnCount) }
    }

    /**
     * 构造函数，基于父组件和农田布局数据初始化。
     * @param parent 父组件
     * @param farmlandBoard 农田布局数据
     */
    constructor(parent: Widget, farmlandBoard: List<Int>) : super(parent) {
        this.farmlandBoard = farmlandBoard
        rowCount = calculateRowCount()
        columnCount = farmlandBoard.size
        cropsGrid = Array(rowCount) { arrayOfNulls(columnCount) }
    }

    /**
     * 计算农田的行数。
     * @return 行数
     */
    private fun calculateRowCount() = farmlandBoard.maxOf {
        Integer.SIZE - Integer.numberOfLeadingZeros(it)
    }

    /**
     * 判断指定位置是否可用。
     * @param x 列索引
     * @param y 行索引
     * @return 如果位置可用且未超出范围，返回true；否则返回false
     */
    fun isAvailable(x: Int, y: Int): Boolean {
        if (x !in 0 until columnCount || y !in 0 until rowCount) return false
        return (farmlandBoard[x] and (1 shl (rowCount - 1 - y))) != 0
    }

    /**
     * 判断指定位置是否已存在作物。
     * @param x 列索引
     * @param y 行索引
     * @return 如果位置存在作物，返回true；否则返回false
     */
    fun isExist(x: Int, y: Int) = cropsGrid.getOrNull(y)?.getOrNull(x) != null

    /**
     * 获取指定位置的屏幕边界。
     * @param x 列索引
     * @param y 行索引
     * @return 如果位置可用，返回对应的[ScreenBounds]；否则返回[ScreenBounds.EMPTY]
     */
    fun getBlockBounds(x: Int, y: Int): ScreenBounds {
        if (!isAvailable(x, y)) return ScreenBounds.EMPTY

        return ScreenBounds(
            x1 = middlePoint.x - blockLeftWidth * (y + 1) + blockRightWidth * x,
            x2 = middlePoint.x - blockLeftWidth * y + blockRightWidth * (x + 1),
            y1 = middlePoint.y - blockHeight - blockLeftHeight * (y + 1) - blockRightHeight * (x + 1),
            y2 = middlePoint.y - blockLeftHeight * y - blockRightHeight * x
        )
    }

    /**
     * 根据鼠标坐标查找对应的农田位置。
     * @param mouseX 鼠标X坐标
     * @param mouseY 鼠标Y坐标
     * @return 如果找到有效位置，返回对应的(列, 行)索引；否则返回null
     */
    fun findMouseInField(mouseX: Double, mouseY: Double): Pair<Int, Int>? {
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
        return if (x in 0 until columnCount && y in 0 until rowCount) x to y else null
    }

    /**
     * 处理鼠标移动事件。
     * 如果当前有激活的种子作物，则将其移动到鼠标所在的有效位置。
     */
    override fun onMouseMove(e: MouseMoveEvent) {
        activeSeedCrop ?: return

        val (x, y) = findMouseInField(e.x, e.y) ?: run {
            activeSeedCrop?.place(ScreenBounds.EMPTY)
            mouseX = null
            mouseY = null
            return
        }

        mouseX = x
        mouseY = y

        activeSeedCrop?.place(
            if (isAvailable(x, y) && !isExist(x, y)) getBlockBounds(x, y)
            else ScreenBounds.EMPTY
        )
    }

    /**
     * 处理鼠标右键点击事件。
     * 如果当前有激活的种子作物，则将其种植到鼠标点击的有效位置。
     */
    override fun onRightClick(e: MouseRightClickEvent) {
        val (x, y) = findMouseInField(e.x, e.y) ?: return
        if (!isAvailable(x, y)) return

        if (isExist(x, y)) {
            cropsGrid[y][x]?.onFarmRightClick()
            return
        }

        activeSeedCrop?.let { original ->
            original.place(getBlockBounds(x, y))
            val newCrop = original.copy().apply { setPlanting() }
            cropsGrid[y][x] = newCrop
        }
    }

    private val particleSystem = ParticleSystem()

    override fun onClick(e: MouseClickEvent) {
        val (x, y) = findMouseInField(e.x, e.y) ?: return
//        if (!isAvailable(x, y) || !isExist(x, y)) return
//        val crop = cropsGrid[y][x]
//        crop?.cleanup()
//        cropsGrid[y][x] = null
//
//        // Generate particles at the crop's position
        val bounds = getBlockBounds(x, y)
        val position = Point((bounds.x1 + bounds.x2) / 2, (bounds.y1 + bounds.y2) / 2)
        particleSystem.generateParticles(position, floatArrayOf(217 / 255f, 185 / 255f, 100 / 255f, 1f), 40)
    }

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
            x1 = leftPoint.x - SPARE_AREA_WIDTH,
            x2 = rightPoint.x + SPARE_AREA_WIDTH,
            y1 = leftPoint.y - middlePoint.y + rightPoint.y - SPARE_AREA_WIDTH,
            y2 = middlePoint.y + SPARE_AREA_WIDTH
        )

        updateCropsPosition()
    }

    /**
     * 更新农田和作物的位置。
     */
    fun update() {
        cropsGrid.forEach { row -> row.forEach { it?.update() } }
        particleSystem.update(0.016f) // Assuming 60 FPS, so deltaTime is approximately 1/60
    }

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
    fun renderBorderline() {
        if (mouseX == null || mouseY == null) return

        val point1 = Point(
            middlePoint.x - blockLeftWidth * mouseY!! + blockRightWidth * mouseX!!,
            middlePoint.y - blockLeftHeight * mouseY!! - blockRightHeight * mouseX!!
        )
        val point2 = Point(point1.x - blockLeftWidth, point1.y - blockLeftHeight)
        val point3 = Point(point1.x + blockRightWidth, point1.y - blockRightHeight)
        val point4 = Point(point2.x + blockRightWidth, point2.y - blockRightHeight)

        val points = listOf(point1, point2, point4, point3, point1) // 形成闭环

        for (i in 0 until 4) {
            lineRenderer.x1 = points[i].x
            lineRenderer.y1 = points[i].y
            lineRenderer.x2 = points[i + 1].x
            lineRenderer.y2 = points[i + 1].y
            lineRenderer.render()
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

    /**
     * 更新所有作物的位置。
     */
    private fun updateCropsPosition() {
        for (y in 0 until rowCount) {
            for (x in 0 until columnCount) {
                cropsGrid[y][x]?.place(getBlockBounds(x, y))
            }
        }
    }
}