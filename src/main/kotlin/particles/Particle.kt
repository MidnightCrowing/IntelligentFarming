package com.midnightcrowing.particles

import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.Texture

/**
 * 单个粒子对象
 * 负责更新位置、应用重力
 */
class Particle(
    val texture: Texture,            // 贴图
    val textureBounds: ScreenBounds, // 纹理坐标
    var position: Point,             // 当前位置
    var velocity: Point,             // 速度向量
    var lifetime: Double,            // 剩余生命
    var size: Int = 6,                // 粒子大小
) {
    // 重力加速度
    private val gravity = 10.0

    /**
     * 更新粒子状态
     * @param deltaTime 时间步长
     */
    fun update(deltaTime: Float) {
        position.x += velocity.x * deltaTime
        position.y -= velocity.y * deltaTime
        lifetime -= deltaTime

        velocity.y -= gravity // 模拟重力
    }

    /**
     * 检查粒子是否已死亡
     */
    fun isDead(): Boolean = lifetime <= 0

    /**
     * 更新粒子的大小
     */
    fun updateSize(newSize: Int) {
        this.size = newSize
    }
}
