package com.midnightcrowing.particles.block

import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.particles.ParticleBase
import com.midnightcrowing.texture.Texture

/**
 * 单个粒子对象
 * 负责更新位置、应用重力
 */
class BlockParticle(
    val texture: Texture,            // 贴图
    val textureBounds: ScreenBounds, // 纹理坐标
    var position: Point,             // 当前位置
    var velocity: Point,             // 速度向量
    var size: Int = 6,               // 粒子大小
    lifetime: Double,                // 剩余生命
) : ParticleBase(lifetime) {
    // 重力加速度
    private val gravity = 10.0

    /**
     * 更新粒子状态
     * @param deltaTime 时间步长
     */
    override fun update(deltaTime: Double) {
        super.update(deltaTime)

        position.x += velocity.x * deltaTime
        position.y -= velocity.y * deltaTime

        velocity.y -= gravity * (deltaTime * 60) // 模拟重力
    }

    /**
     * 更新粒子的大小
     */
    fun updateSize(newSize: Int) {
        this.size = newSize
    }
}
