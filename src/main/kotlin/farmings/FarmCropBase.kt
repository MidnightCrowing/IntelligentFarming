package com.midnightcrowing.farmings

import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.block.Block
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.utils.GameTick
import kotlin.math.sqrt
import kotlin.random.Random

abstract class FarmCropBase(val farmArea: FarmArea) : Block(farmArea) {
    abstract val growDuringTextures: Map<Int, Texture>
    protected val growInitTexture: Texture get() = growDuringTextures.values.first()
    protected val growFullTexture: Texture get() = growDuringTextures.values.last()

    var nowTextures: Texture? = null
        set(value) {
            field = value
            renderer.texture = value
        }

    protected open var plantedTick: Long = 0
    protected open var growthDuration: Double = triangularRandom(80000.0, 200000.0, 13000.0)
    var growthProgress: Double = 0.0
        private set(value) {
            field = value
            setGrowthProgressTexture(value)
        }
    val isFullyGrown: Boolean get() = GameTick.tick - plantedTick >= growthDuration

    /**
     * 设置当前纹理为生长进度纹理
     * @param progress 生长进度，范围为 `[0.0, 1.0]`
     */
    private fun setGrowthProgressTexture(progress: Double) {
        val maxIndex = growDuringTextures.size - 1
        val progressIndex = (progress * growDuringTextures.size).toInt()
            .let { if (it == maxIndex && !isFullyGrown) it - 1 else it }
            .coerceIn(0, maxIndex)

        nowTextures = growDuringTextures[progressIndex] ?: growInitTexture
    }

    fun setShadow() {
        nowTextures = growInitTexture
        renderer.alpha = 0.6
    }

    fun setPlanting() {
        renderer.alpha = 1.0
        plantedTick = GameTick.tick
    }

    override fun update() {
        if (isFullyGrown) {
            nowTextures = growFullTexture
        } else {
            val elapsedTicks = GameTick.tick - plantedTick
            growthProgress = elapsedTicks / growthDuration
        }
    }

    /**
     * 获取作物物品信息用于`CropInfoDisplay`展示
     */
    open fun getItemStack(): ItemStack = ItemStack.EMPTY

    /**
     * 使用骨粉催熟作物
     * @return 是否成功催熟（已成熟则返回 false）
     */
    open fun applyBoneMeal(): Boolean {
        if (isFullyGrown) return false

        // 缩短生长时长，使生长进度提升 20% ~ 40%
        val boostRatio = Random.nextDouble(0.2, 0.4)
        plantedTick -= (growthDuration * boostRatio).toLong()

        // 避免过度回退（例如生长进度 > 1.0）
        plantedTick = maxOf(plantedTick, GameTick.tick - growthDuration.toLong())

        return true
    }

    /**
     * 获取掉落物
     * @param l 时运等级
     * @return 掉落物列表
     */
    open fun getDrops(l: Int): Array<ItemStack> = arrayOf(ItemStack.EMPTY)

    /**
     * 使用三角分布生成随机值。
     * @param min 最小值。
     * @param max 最大值。
     * @param mode 中心概率最大的点
     * @return 一个介于 min 和 max 之间的随机值。
     */
    protected fun triangularRandom(min: Double, max: Double, mode: Double): Double {
        val u = Random.Default.nextDouble()
        return if (u < (mode - min) / (max - min)) {
            min + sqrt(u * (max - min) * (mode - min))
        } else {
            max - sqrt((1 - u) * (max - min) * (max - mode))
        }
    }

    /**
     * 模拟二项分布生成掉落物数量
     * @param n 最大掉落次数
     * @param l 时运等级
     * @param p 单次掉落概率
     * @return 掉落物数量
     */
    protected fun generateDropCount(n: Int, l: Int, p: Double): Int {
        var dropCount = 0
        repeat(n + l) {
            if (Random.nextDouble() < p) {
                dropCount++
            }
        }
        return dropCount
    }

    override fun toString(): String = "未知农作物"

    /**
     * 右键点击事件
     * @param l 时运等级
     * @return 掉落物列表
     */
    open fun onFarmRightClick(l: Int): Array<ItemStack>? = null

    fun copy(): FarmCropBase {
        val newCrop = this::class.constructors.first().call(farmArea)
        newCrop.place(this.bounds)
        newCrop.nowTextures = this.nowTextures
        return newCrop
    }
}