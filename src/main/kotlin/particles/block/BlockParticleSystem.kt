package com.midnightcrowing.particles.block

import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.particles.ParticleSystemBase
import com.midnightcrowing.texture.Texture
import kotlin.random.Random


class BlockParticleSystem : ParticleSystemBase<BlockParticle>() {
    override val particles = mutableListOf<BlockParticle>()
    private val renderer = BlockParticleRenderer() // 负责渲染粒子

    // 位置随机性控制
    private var positionVariation: Point = Point(60.0, 55.0)

    // 粒子贴图大小
    private var textureSize: Int = 50

    // 粒子缩放大小
    private var particleSize: Int = 6

    /**
     * 生成方块破坏粒子效果
     * @param origin 生成粒子的中心位置
     * @param texture 粒子贴图
     * @param count 生成粒子的数量
     */
    fun generateParticles(origin: Point, texture: Texture, count: Int) {
        val (imgWidth, imgHeight) = texture.run { width to height }

        repeat(count) {
            val randomPosition = generateRandomPositionForBlock(origin, positionVariation)

            val velocity = Point(
                x = Random.nextDouble(-positionVariation.x / 3 * 4, positionVariation.x / 3 * 4),
                y = Random.nextDouble(positionVariation.y / 3, positionVariation.y / 3 * 2)
            )

            val lifetime = Random.nextDouble() * 0.3 + Random.nextDouble() * 0.2

            val startX = Random.nextInt(imgWidth - textureSize).toDouble()
            val startY = Random.nextInt(imgHeight - textureSize).toDouble()

            particles += BlockParticle(
                texture = texture,
                textureBounds = ScreenBounds(
                    startX / imgWidth,
                    startY / imgHeight,
                    (startX + textureSize) / imgWidth,
                    (startY + textureSize) / imgHeight
                ),
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
    override fun render() {
        renderer.renderAll(particles)
    }
}