package com.midnightcrowing.particles

import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.Texture
import kotlin.random.Random

/**
 * 粒子系统管理类
 * 负责生成、更新和渲染粒子效果
 */
class ParticleSystem {
    private val particles = mutableListOf<Particle>()
    private val renderer = ParticleRenderer() // 负责渲染粒子

    // 位置随机性控制
    private var positionVariation: Point = Point(60.0, 55.0)

    // 粒子贴图大小
    private var textureSize: Int = 50

    // 粒子缩放大小
    private var particleSize: Int = 6

    /**
     * 生成粒子效果
     * @param origin 生成粒子的中心位置
     * @param texture 粒子贴图
     * @param count 生成粒子的数量
     */
    fun generateParticles(origin: Point, texture: Texture, count: Int) {
        val (imgWidth, imgHeight) = texture.image.run { width to height }

        repeat(count) {
            val randomPosition = Point(
                x = origin.x + Random.nextDouble(
                    from = -positionVariation.x / 2,
                    until = positionVariation.x / 2
                ) + Random.nextDouble(-10.0, 10.0),
                y = origin.y + Random.nextDouble(
                    from = -positionVariation.y / 11 * 3,
                    until = positionVariation.y / 11 * 8
                ) + Random.nextDouble(-5.0, 5.0)
            )

            val velocity = Point(
                x = Random.nextDouble(-positionVariation.x / 3 * 4, positionVariation.x / 3 * 4),
                y = Random.nextDouble(positionVariation.y / 3, positionVariation.y / 3 * 2)
            )

            val lifetime = Random.nextFloat() * 0.5

            val startX = Random.nextInt(imgWidth - textureSize).toDouble()
            val startY = Random.nextInt(imgHeight - textureSize).toDouble()

            particles += Particle(
                texture = texture,
                textureBounds = ScreenBounds(
                    startX / imgWidth,
                    startY / imgHeight,
                    (startX + textureSize) / imgWidth,
                    (startY + textureSize) / imgHeight
                ),
                position = randomPosition,
                velocity = velocity,
                lifetime = lifetime,
                size = particleSize
            )
        }
    }

    /**
     * 更新所有粒子的状态
     * @param deltaTime 时间步长
     */
    fun update(deltaTime: Float) {
        particles.forEach { it.update(deltaTime) }
        particles.removeIf { it.isDead() } // 移除生命周期结束的粒子
    }

    /**
     * 设置粒子的随机位置偏移和大小
     * @param variation 随机性范围
     * @param size      粒子尺寸
     */
    fun configure(variation: Point, size: Int) {
        this.positionVariation = variation
        this.particleSize = size
        particles.forEach { it.updateSize(size) }
    }

    /**
     * 渲染所有粒子
     */
    fun render() {
        renderer.renderAll(particles)
    }
}
