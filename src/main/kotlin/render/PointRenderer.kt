package com.midnightcrowing.render

import com.midnightcrowing.model.Point
import org.lwjgl.opengl.GL11.*

class PointRenderer(private val point: Point, private val size: Float = 5f) {
    fun render() {
        glPointSize(size)
        glColor4f(1f, 0f, 0f, 1f)
        glBegin(GL_POINTS)
        glVertex2f(point.x, point.y)
        glEnd()
    }
}