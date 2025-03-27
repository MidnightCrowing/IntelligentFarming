package com.midnightcrowing.particles

import org.lwjgl.opengl.GL46.*

/**
 * 负责渲染粒子的类
 */
class ParticleRenderer {
    /**
     * 渲染单个粒子
     */
    fun render(particle: Particle) {
        if (particle.lifetime <= 0) return

        glEnable(GL_TEXTURE_2D)
        particle.texture.bind()
        glBegin(GL_QUADS)

        glColor4f(1f, 1f, 1f, 1f)

        val u1 = particle.textureBounds.x1.toFloat()
        val v1 = particle.textureBounds.y1.toFloat()
        val u2 = particle.textureBounds.x2.toFloat()
        val v2 = particle.textureBounds.y2.toFloat()

        val x1 = (particle.position.x - particle.size).toFloat()
        val y1 = (particle.position.y - particle.size).toFloat()
        val x2 = (particle.position.x + particle.size).toFloat()
        val y2 = (particle.position.y + particle.size).toFloat()

        glTexCoord2f(u1, v1); glVertex2f(x1, y1) // 左上角
        glTexCoord2f(u2, v1); glVertex2f(x2, y1) // 右上角
        glTexCoord2f(u2, v2); glVertex2f(x2, y2) // 右下角
        glTexCoord2f(u1, v2); glVertex2f(x1, y2) // 左下角

        glEnd()
        glDisable(GL_TEXTURE_2D)
    }

    /**
     * 渲染所有粒子
     */
    fun renderAll(particles: List<Particle>) {
        particles.forEach { render(it) }
    }
}
