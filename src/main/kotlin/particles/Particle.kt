package com.midnightcrowing.particles

import com.midnightcrowing.model.Point
import org.lwjgl.opengl.GL11.*

class Particle(
    var position: Point,
    var velocity: Point,
    var color: FloatArray,
    var life: Double,
) {
    private val g = 10.0

    fun update(deltaTime: Float) {
        position.x += velocity.x * deltaTime
        position.y -= velocity.y * deltaTime
        life -= deltaTime

        velocity.y -= g
    }

    fun render() {
        if (life > 0) {
            glColor4f(color[0], color[1], color[2], color[3])
            glBegin(GL_QUADS)
            glVertex2f(position.x.toFloat() - 4.5f, position.y.toFloat() - 4.5f)
            glVertex2f(position.x.toFloat() + 4.5f, position.y.toFloat() - 4.5f)
            glVertex2f(position.x.toFloat() + 4.5f, position.y.toFloat() + 4.5f)
            glVertex2f(position.x.toFloat() - 4.5f, position.y.toFloat() + 4.5f)
            glEnd()
        }
    }
}