package com.midnightcrowing.particles.cropGrowth

import com.midnightcrowing.model.Point
import com.midnightcrowing.particles.ParticleSystemBase
import kotlin.random.Random

class CropGrowthParticleSystem : ParticleSystemBase<CropGrowthParticle>() {
    override val particles = mutableListOf<CropGrowthParticle>()

    // 位置随机性控制
    private var positionVariation: Point = Point(60.0, 55.0)

    // 粒子缩放基础大小
    private var particleBaseSize: Int = 13

    /**
     * 生成使用骨粉时的生长粒子效果
     * @param origin 生成粒子的原点位置
     * @param count 生成粒子的数量
     */
    fun generateParticles(origin: Point, count: Int) {
        repeat(count) {
            val randomPosition = generateRandomPositionForBlock(origin, positionVariation)

            val velocity = Point(
                x = Random.nextDouble(-positionVariation.x / 3, positionVariation.x / 3),
                y = Random.nextDouble(positionVariation.y / 4, positionVariation.y / 2)
            )

            val lifetime = Random.nextDouble() * 0.5 + Random.nextDouble() * 0.3
            val particleSize = Random.nextInt(particleBaseSize + 2, particleBaseSize + 8)

            particles += CropGrowthParticle(
                position = randomPosition,
                velocity = velocity,
                size = particleSize,
                lifetime = lifetime,
            )
        }
    }

    /**
     * 设置粒子的随机位置偏移和大小
     * @param variation 随机性范围
     * @param size      粒子基础尺寸
     */
    fun configure(variation: Point, size: Int) {
        this.positionVariation = variation
        this.particleBaseSize = size
    }
}