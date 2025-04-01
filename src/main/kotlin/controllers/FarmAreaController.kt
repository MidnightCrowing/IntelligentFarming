package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.gui.publics.CropInfoDisplay
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.particles.ParticleSystem

class FarmAreaController(farmController: FarmController) {
    // region controllers
    lateinit var farmArea: FarmArea

    val cropInfoController: CropInfoDisplayControllers = farmController.cropInfo
    val cropInfo: CropInfoDisplay by lazy { cropInfoController.cropInfoDisplay }
    val hotController: HotBarController by lazy { farmController.hotBar }
    val invController: InventoryController by lazy { farmController.inventory }

    // endregion

    data class GridPosition(val x: Int, val y: Int)

    private fun GridPosition.hasCrop(): Boolean = isExist(this)
    private val GridPosition.crop: FarmCropBase? get() = cropsGrid[y][x]

    // 农田参数
    private lateinit var farmlandBoard: List<Int>    // 农田布局位掩码（每个Int表示一列的可用行）
    private var rowCount: Int = 0
    private var columnCount: Int = 0

    // 农田作物
    lateinit var cropsGrid: Array<Array<FarmCropBase?>>
    var activeSeedCrop: FarmCropBase? = null
        set(value) {
            field?.cleanup()
            field = value?.apply {
                setShadow()
                setVisible(field?.isVisible != false)
                place(field?.widgetBounds ?: ScreenBounds.EMPTY)
            }
        }

    // 粒子系统，用于生成和管理粒子效果
    val particleSystem: ParticleSystem = ParticleSystem()

    // 鼠标参数
    var mousePosition: GridPosition? = null  // 当前鼠标悬停位置
    var isLeftClick: Boolean = false
    var isRightClick: Boolean = false

    fun init(farmArea: FarmArea, farmlandBoard: List<Int>, rowCount: Int, columnCount: Int) {
        this.farmArea = farmArea
        this.rowCount = rowCount
        this.columnCount = columnCount
        this.farmlandBoard = farmlandBoard
        this.cropsGrid = Array(rowCount) { arrayOfNulls(columnCount) }
    }

    // region GridPosition 工具函数
    /**
     * 判断指定位置是否可用。
     * @return 如果位置可用且未超出范围，返回true；否则返回false
     */
    fun isAvailable(pos: GridPosition): Boolean {
        if (pos.x !in 0 until columnCount || pos.y !in 0 until rowCount) return false
        return (farmlandBoard[pos.x] and (1 shl (rowCount - 1 - pos.y))) != 0
    }

    /**
     * 判断指定位置是否已存在作物。
     * @return 如果位置存在作物，返回true；否则返回false
     */
    fun isExist(pos: GridPosition) = cropsGrid.getOrNull(pos.y)?.getOrNull(pos.x) != null

    // endregion

    /**
     * 更新农田和作物的位置。
     */
    fun update() {
        cropsGrid.forEach { row -> row.forEach { it?.update() } }
        cropInfo.update()
        particleSystem.update(0.016f) // Assuming 60 FPS, so deltaTime is approximately 1/60

        if (mousePosition != null) {
            if (!mousePosition!!.hasCrop()) {
                activeSeedCrop?.setHidden(false)
                activeSeedCrop?.place(farmArea.getBlockBounds(mousePosition!!))
            } else {
                activeSeedCrop?.setHidden(true)
            }
            when {
                isLeftClick -> handleLeftClick()
                isRightClick -> handleRightClick()
            }
        } else {
            activeSeedCrop?.setHidden(true)
        }

        cropInfoController.update(mousePosition?.crop)
    }

    /**
     * 处理鼠标左键点击事件。
     */
    private fun handleLeftClick() {
        if (mousePosition!!.hasCrop()) {
            // 如果有作物，尝试移除
            generateParticles(mousePosition!!.crop!!, mousePosition!!)
            removeCrop(mousePosition!!)
        }
    }

    /**
     * 处理鼠标右键点击事件。
     */
    private fun handleRightClick() {
        if (!mousePosition!!.hasCrop()) {
            // 如果没有作物，尝试种植
            if (activeSeedCrop != null) {
                plantCropAt(mousePosition!!)
            }
        } else {
            // 如果有作物，尝试执行作物的右键操作
            mousePosition!!.crop!!.onFarmRightClick()
        }
    }

    /**
     * 在指定位置种植作物。
     * @param pos 作物位置
     */
    private fun plantCropAt(pos: GridPosition) {
        activeSeedCrop?.let { original ->
            val newCrop = original.copy().apply { setPlanting() }
            cropsGrid[pos.y][pos.x] = newCrop

            hotController.onPlantCrop()
        }
    }

    /**
     * 移除指定位置的作物。
     * @param pos 作物位置
     */
    private fun removeCrop(pos: GridPosition) {
        pos.crop?.let { crop ->
            // 获取掉落物
            for (item in crop.getDrops()) {
                if (item.isEmpty() || item.count <= 0) continue
                invController.addItem(item)
                hotController.update()
            }
            // 清理作物
            crop.cleanup()
        }
        cropsGrid[pos.y][pos.x] = null
    }

    /**
     * 生成粒子效果。
     * @param crop 作物对象
     * @param pos 作物位置
     */
    private fun generateParticles(crop: FarmCropBase, pos: GridPosition) {
        crop.nowTextures?.let {
            particleSystem.generateParticles(farmArea.getBlockBounds(pos).between, it, 40)
        }
    }
}