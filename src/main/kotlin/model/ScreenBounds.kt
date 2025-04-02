package com.midnightcrowing.model


// 屏幕坐标系的边界
data class ScreenBounds(var x1: Double, var y1: Double, var x2: Double, var y2: Double) {
    companion object {
        val EMPTY get() = ScreenBounds(0.0, 0.0, 0.0, 0.0)
    }

    val startPoint: Point get() = Point(x1, y1)
    val endPoint: Point get() = Point(x2, y2)

    val width: Double get() = x2 - x1
    val height: Double get() = y2 - y1

    val between: Point get() = Point((x1 + x2) / 2, (y1 + y2) / 2)

    fun contains(point: Point): Boolean {
        return point.x in x1..x2 && point.y in y1..y2
    }

    // 重载 +
    operator fun plus(other: Point): ScreenBounds {
        return ScreenBounds(x1 + other.x, y1 + other.y, x2 + other.x, y2 + other.y)
    }

    // 重载 *
    operator fun times(scale: Int): ScreenBounds {
        return ScreenBounds(x1 * scale, y1 * scale, x2 * scale, y2 * scale)
    }

    operator fun times(scale: Double): ScreenBounds {
        return ScreenBounds(x1 * scale, y1 * scale, x2 * scale, y2 * scale)
    }
}
