package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.Texture
import com.midnightcrowing.utils.GameTick
import kotlin.math.sqrt
import kotlin.random.Random

abstract class FarmCropBase(val farmArea: FarmArea) : Widget(farmArea) {
    abstract val growDuringTextures: Map<Int, Texture>
    val growInitTexture: Texture get() = growDuringTextures.values.first()
    val growFullTexture: Texture get() = growDuringTextures.values.last()

    open var plantedTick: Long = 0
    open var growthDuration: Int = triangularRandom(0.0, 200000.0, 10000.0).toInt()
    val isFullyGrown: Boolean get() = GameTick.tick - plantedTick >= growthDuration

    fun setGrowthProgressTexture(progress: Float) {
        val progressIndex = (progress * growDuringTextures.size).toInt()
        renderer.texture = growDuringTextures[progressIndex] ?: growInitTexture
    }

    fun setShadow() {
        renderer.texture = growInitTexture
        renderer.alpha = 0.6f
    }

    fun setPlanting() {
        renderer.alpha = 1f
        plantedTick = GameTick.tick
    }

    open fun update() {
        if (isFullyGrown) {
            renderer.texture = growFullTexture
        } else {
            val elapsedTicks = GameTick.tick - plantedTick
            val growthProgress = elapsedTicks.toFloat() / growthDuration
            setGrowthProgressTexture(growthProgress)
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

    open fun onRightClick() {}

    abstract fun copy(): FarmCropBase
}