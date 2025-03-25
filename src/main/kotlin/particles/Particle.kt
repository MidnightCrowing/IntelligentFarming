package com.midnightcrowing.particles

import com.midnightcrowing.model.Image
import com.midnightcrowing.model.Point
import com.midnightcrowing.render.ImageRenderer

/**
 * 单个粒子对象
 * 负责更新位置、应用重力，并进行渲染
 */
class Particle(
    var position: Point,    // 粒子当前位置
    var velocity: Point,    // 速度向量
    image: Image,           // 贴图
    var lifetime: Double,   // 粒子生命周期
    var size: Int = 6,      // 粒子大小
) {
    private val renderer: ImageRenderer = ImageRenderer.createImageRenderer(image)

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
     * 渲染粒子
     */
    fun render() {
        if (lifetime > 0) {
            renderer.render(
                x1 = position.x - size,
                y1 = position.y - size,
                x2 = position.x + size,
                y2 = position.y + size
            )
        }
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
