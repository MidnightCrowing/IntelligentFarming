package com.midnightcrowing.model


// 屏幕坐标系的边界
data class ScreenBounds(var x1: Double, var y1: Double, var x2: Double, var y2: Double) {
    companion object {
        val EMPTY = ScreenBounds(0.0, 0.0, 0.0, 0.0)
    }

    val width: Double get() = x2 - x1
    val height: Double get() = y2 - y1

    val between: Point get() = Point((x1 + x2) / 2, (y1 + y2) / 2)
}
