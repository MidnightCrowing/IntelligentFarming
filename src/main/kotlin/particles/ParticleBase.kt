package com.midnightcrowing.particles

abstract class ParticleBase(
    var lifetime: Double, // 剩余生命
) {
    /**
     * 渲染粒子
     */
    open fun render() {}

    /**
     * 更新粒子状态
     * @param deltaTime 时间步长
     */
    open fun update(deltaTime: Float) {
        lifetime -= deltaTime
    }

    /**
     * 检查粒子是否已死亡
     */
    fun isDead(): Boolean = lifetime <= 0
}