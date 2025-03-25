package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.Texture
import com.midnightcrowing.utils.GameTick
import kotlin.math.sqrt
import kotlin.random.Random

abstract class FarmCropBase(val farmArea: FarmArea) : Widget(farmArea) {
    abstract val growDuringTextures: Map<Int, Texture>
    val growInitTexture: Texture get() = growDuringTextures.values.first()
    val growFullTexture: Texture get() = growDuringTextures.values.last()

    var nowTextures: Texture? = null
        private set(value) {
            field = value
            renderer.texture = value
        }

    open var plantedTick: Long = 0
    open var growthDuration: Double = triangularRandom(0.0, 20000.0, 1000.0)
    var growthProgress: Double = 0.0
        private set(value) {
            field = value
            setGrowthProgressTexture(value)
        }
    val isFullyGrown: Boolean get() = GameTick.tick - plantedTick >= growthDuration

    fun setGrowthProgressTexture(progress: Double) {
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
    fun triangularRandom(min: Double, max: Double, mode: Double): Double {
        val u = Random.nextDouble()
        return if (u < (mode - min) / (max - min)) {
            min + sqrt(u * (max - min) * (mode - min))
        } else {
            max - sqrt((1 - u) * (max - min) * (max - mode))
        }
    }

    abstract fun getFarmItem(parent: Widget): FarmItems

    override fun toString(): String = "未知农作物"

    open fun onFarmRightClick() {}

    abstract fun copy(): FarmCropBase
}