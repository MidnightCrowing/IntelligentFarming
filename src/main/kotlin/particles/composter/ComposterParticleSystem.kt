package com.midnightcrowing.particles.composter

import com.midnightcrowing.model.Point
import com.midnightcrowing.particles.ParticleSystemBase
import kotlin.random.Random

class ComposterParticleSystem : ParticleSystemBase<ComposterParticle>() {
    override val particles = mutableListOf<ComposterParticle>()

    private var areaX: Point = Point.EMPTY  // 平行四边形区域X方向向量
    private var areaY: Point = Point.EMPTY  // 平行四边形区域Y方向向量

    /**
     * 生成堆肥桶被填充时产生的粒子效果
     * @param origin 生成粒子的原点位置
     */
    fun generateParticles(origin: Point) {
        val count: Int = Random.nextInt(4, 8) // 生成粒子的数量
        repeat(count) {
            val randomPosition = generateRandomPositionInArea(origin, areaX, areaY)
            val velocity = areaX * Random.nextDouble(0.1) + areaY * Random.nextDouble(0.1)
            val lifetime = Random.nextDouble() * 0.3 + Random.nextDouble() * 0.2
            val particleSize = Random.nextInt(9, 16)

            particles += ComposterParticle(
                position = randomPosition,
                velocity = velocity,
                size = particleSize,
                lifetime = lifetime,
            )
        }
    }

    fun configure(areaX: Point, areaY: Point) {
        this.areaX = areaX
        this.areaY = areaY
    }
}