package com.midnightcrowing.renderer

import com.midnightcrowing.model.ScreenBounds
import org.lwjgl.opengl.GL46.*

class RectangleRenderer(
    var x1: Double = 0.0,
    var y1: Double = 0.0,
    var x2: Double = 0.0,
    var y2: Double = 0.0,
    var color: FloatArray = floatArrayOf(1f, 1f, 1f, 1f), // 默认白色，不透明
) {
    constructor(bounds: ScreenBounds = ScreenBounds.EMPTY, color: FloatArray = floatArrayOf(1f, 1f, 1f, 1f)) : this(
        bounds.x1,
        bounds.y1,
        bounds.x2,
        bounds.y2,
        color
    )

    fun render() {
        glColor4f(color[0], color[1], color[2], color[3])
        glBegin(GL_QUADS)
        glVertex2f(x1.toFloat(), y1.toFloat())
        glVertex2f(x2.toFloat(), y1.toFloat())
        glVertex2f(x2.toFloat(), y2.toFloat())
        glVertex2f(x1.toFloat(), y2.toFloat())
        glEnd()
    }
}