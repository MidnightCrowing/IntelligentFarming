package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.gui.publics.CropInfoDisplay
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.Item
import com.midnightcrowing.model.item.Items.BONE_MEAL
import com.midnightcrowing.model.item.Items.CABBAGE_SEED
import com.midnightcrowing.model.item.Items.CARROT
import com.midnightcrowing.model.item.Items.CORN_SEED
import com.midnightcrowing.model.item.Items.COTTON_SEED
import com.midnightcrowing.model.item.Items.DIAMOND_HOE
import com.midnightcrowing.model.item.Items.GOLDEN_HOE
import com.midnightcrowing.model.item.Items.IRON_HOE
import com.midnightcrowing.model.item.Items.NETHERITE_HOE
import com.midnightcrowing.model.item.Items.ONION
import com.midnightcrowing.model.item.Items.POTATO
import com.midnightcrowing.model.item.Items.TOMATO_SEED
import com.midnightcrowing.model.item.Items.WHEAT_SEED
import com.midnightcrowing.particles.ParticleSystem

class FarmAreaController(farmController: FarmController) {
    // region controllers
    lateinit var farmArea: FarmArea

    val cropInfoController: CropInfoDisplayController = farmController.cropInfo
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
                setHidden(field?.isVisible == false)
                place(field?.bounds ?: ScreenBounds.EMPTY)
            }
        }

    // 手持物品
    // 可展示的物品列表
    private val displayedItemIds: Set<String> = setOf(
        CARROT.id, ONION.id, POTATO.id,                                                // 作物种子
        CABBAGE_SEED.id, CORN_SEED.id, COTTON_SEED.id, TOMATO_SEED.id, WHEAT_SEED.id,  // 作物种子
        IRON_HOE.id, GOLDEN_HOE.id, DIAMOND_HOE.id, NETHERITE_HOE.id,                  // 工具
        BONE_MEAL.id                                                                   // 骨粉
    )
    var handheldItem: Item? = null
        set(value) {
            field = value
            farmArea.handheldItemRenderer = value
                ?.takeIf { it.id in displayedItemIds }
                ?.let { farmArea.itemRenderCache.getItemCache(it.id) }
        }

    // 粒子系统，用于生成和管理粒子效果
    val particleSystem: ParticleSystem = ParticleSystem()

    // 鼠标参数
    var mouseGridPosition: GridPosition? = null  // 当前鼠标悬停位置
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

        if (mouseGridPosition != null) {
            if (!mouseGridPosition!!.hasCrop()) {
                activeSeedCrop?.setHidden(false)
                activeSeedCrop?.place(farmArea.getBlockBounds(mouseGridPosition!!))
            } else {
                activeSeedCrop?.setHidden(true)
            }
            when {
                isLeftClick -> handleLeftKeepClick()
                isRightClick -> handleRightKeepClick()
            }
        } else {
            activeSeedCrop?.setHidden(true)
        }

        cropInfoController.update(mouseGridPosition?.crop)
    }

    /**
     * 处理鼠标右键单次点击事件。
     */
    fun handleRightClick() {
        if (mouseGridPosition!!.hasCrop()) {
            // 检查手持物品是否是骨粉
            handheldItem?.takeIf { it.id == BONE_MEAL.id }?.let {
                // 使用骨粉
                if (mouseGridPosition!!.crop!!.applyBoneMeal()) {
                    hotController.onUseBoneMeal()
                }
            }
        }
    }

    /**
     * 处理鼠标左键连续点击事件。
     */
    private fun handleLeftKeepClick() {
        if (mouseGridPosition!!.hasCrop()) {
            // 如果有作物，尝试移除
            generateParticles(mouseGridPosition!!.crop!!, mouseGridPosition!!)
            removeCrop(mouseGridPosition!!)
        }
    }

    /**
     * 处理鼠标右键连续点击事件。
     */
    private fun handleRightKeepClick() {
        if (!mouseGridPosition!!.hasCrop()) {
            // 如果没有作物，尝试种植
            activeSeedCrop?.let { plantCropAt(mouseGridPosition!!) }
        } else {
            // 如果有作物，且不手持骨粉，尝试执行作物的右键操作
            if (handheldItem?.id != BONE_MEAL.id) {
                mouseGridPosition!!.crop!!.onFarmRightClick(getToolFortune())
                    ?.filter { !it.isEmpty() && it.count > 0 }
                    ?.forEach {
                        invController.addItem(it)
                        hotController.update()
                    }
            }
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
            for (item in crop.getDrops(getToolFortune())) {
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
     * 获取当前手持工具的时运值。
     * @return 当前工具的时运值
     */
    private fun getToolFortune(): Int {
        return hotController.getSelectedItem()?.fortune ?: 0
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