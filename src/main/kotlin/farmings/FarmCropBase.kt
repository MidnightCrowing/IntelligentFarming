package com.midnightcrowing.farmings

import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.utils.GameTick
import kotlin.math.sqrt
import kotlin.random.Random

abstract class FarmCropBase(val farmArea: FarmArea) : Widget(farmArea) {
    abstract val growDuringTextures: Map<Int, Texture>
    protected val growInitTexture: Texture get() = growDuringTextures.values.first()
    protected val growFullTexture: Texture get() = growDuringTextures.values.last()

    var nowTextures: Texture? = null
        set(value) {
            field = value
            renderer.texture = value
        }

    open var plantedTick: Long = 0
    open var growthDuration: Double = triangularRandom(80000.0, 200000.0, 13000.0)
    var growthProgress: Double = 0.0
        private set(value) {
            field = value
            setGrowthProgressTexture(value)
        }
    val isFullyGrown: Boolean get() = GameTick.tick - plantedTick >= growthDuration

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

    open fun update() {
        if (isFullyGrown) {
            nowTextures = growFullTexture
        } else {
            val elapsedTicks = GameTick.tick - plantedTick
            growthProgress = elapsedTicks / growthDuration
        }
    }

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

    open fun getItemStack(): ItemStack = ItemStack.EMPTY

    open fun getDrops(): Array<ItemStack> = arrayOf(ItemStack.EMPTY)

    /**
     * 模拟二项分布生成掉落物数量
     * @param n 最大掉落次数
     * @param p 单次掉落概率
     * @return 掉落物数量
     */
    protected fun generateDropCount(n: Int, p: Double): Int {
        var dropCount = 0
        repeat(n) {
            if (Random.nextDouble() < p) {
                dropCount++
            }
        }
        return dropCount
    }

    override fun toString(): String = "未知农作物"

    open fun onFarmRightClick(): Array<ItemStack>? = null

    abstract fun copy(): FarmCropBase
}