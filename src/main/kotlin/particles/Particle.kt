package com.midnightcrowing.particles

import com.midnightcrowing.model.Point
import org.lwjgl.opengl.GL11.*

class Particle(
    var position: Point,
    var velocity: Point,
    var color: FloatArray,
    var life: Double,
) {
    fun update(deltaTime: Float) {
        position.x += velocity.x * deltaTime
        position.y += velocity.y * deltaTime
        life -= deltaTime
    }

    fun render() {
        if (life > 0) {
//            println("position: $position, life: $life , color: ${color.joinToString(",")}")
            glDisable(GL_TEXTURE_2D) // 禁用纹理，防止影响纯色矩形
            glColor4f(color[0], color[1], color[2], color[3] * life.toFloat())
            glBegin(GL_QUADS)
            glVertex2f(position.x.toFloat() - 3f, position.y.toFloat() - 3f)
            glVertex2f(position.x.toFloat() + 3f, position.y.toFloat() - 3f)
            glVertex2f(position.x.toFloat() + 3f, position.y.toFloat() + 3f)
            glVertex2f(position.x.toFloat() - 3f, position.y.toFloat() + 3f)
            glEnd()
            glEnable(GL_TEXTURE_2D)  // 重新启用纹理
        }
    }
}