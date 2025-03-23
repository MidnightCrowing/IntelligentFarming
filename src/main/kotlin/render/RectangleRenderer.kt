package com.midnightcrowing.render

import com.midnightcrowing.model.ScreenBounds
import org.lwjgl.opengl.GL46.*

class RectangleRenderer {
    var x1: Double
    var y1: Double
    var x2: Double
    var y2: Double
    var color: FloatArray

    constructor(
        x1: Double = 0.0,
        y1: Double = 0.0,
        x2: Double = 0.0,
        y2: Double = 0.0,
        color: FloatArray = floatArrayOf(1f, 1f, 1f, 1f), // 默认白色，不透明
    ) {
        this.x1 = x1
        this.y1 = y1
        this.x2 = x2
        this.y2 = y2
        this.color = color
    }

    constructor(bounds: ScreenBounds, color: FloatArray = floatArrayOf(1f, 1f, 1f, 1f)) {
        this.x1 = bounds.x1
        this.y1 = bounds.y1
        this.x2 = bounds.x2
        this.y2 = bounds.y2
        this.color = color
    }

    fun render() {
        glDisable(GL_TEXTURE_2D) // 禁用纹理，防止影响纯色矩形

        glColor4f(color[0], color[1], color[2], color[3])
        glBegin(GL_QUADS)
        glVertex2f(x1.toFloat(), y1.toFloat())
        glVertex2f(x2.toFloat(), y1.toFloat())
        glVertex2f(x2.toFloat(), y2.toFloat())
        glVertex2f(x1.toFloat(), y2.toFloat())
        glEnd()

        glEnable(GL_TEXTURE_2D)  // 重新启用纹理
    }
}