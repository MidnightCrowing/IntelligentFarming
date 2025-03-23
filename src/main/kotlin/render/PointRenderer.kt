package com.midnightcrowing.render

import com.midnightcrowing.model.Point
import org.lwjgl.opengl.GL46.*

class PointRenderer(private var point: Point, private val size: Double = 5.0) {
    fun render() {
        glDisable(GL_TEXTURE_2D) // 禁用纹理，防止影响纯色矩形

        glPointSize(size.toFloat())
        glColor4f(1f, 0f, 0f, 1f)
        glBegin(GL_POINTS)
        glVertex2f(point.x.toFloat(), point.y.toFloat())
        glEnd()

        glEnable(GL_TEXTURE_2D)  // 重新启用纹理
    }
}