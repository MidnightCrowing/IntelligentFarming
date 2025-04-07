package com.midnightcrowing.particles.composter

import com.midnightcrowing.model.Point
import com.midnightcrowing.particles.ParticleBase
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum

open class ComposterParticle(
    var position: Point,             // 当前位置
    var velocity: Point,             // 速度向量
    var size: Int,                   // 粒子大小
    lifetime: Double,                // 剩余生命
) : ParticleBase(lifetime) {
    private val renderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.PE_GLINT.texture)

    /**
     * 更新粒子状态
     * @param deltaTime 时间步长
     */
    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        position.x += velocity.x * deltaTime
        position.y -= velocity.y * deltaTime
    }

    /**
     * 渲染粒子
     */
    override fun render() = renderer.render(
        x1 = position.x - size,
        y1 = position.y - size,
        x2 = position.x + size,
        y2 = position.y + size,
    )
}
