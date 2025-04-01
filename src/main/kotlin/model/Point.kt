package com.midnightcrowing.model


data class Point(var x: Double, var y: Double) {
    companion object {
        val EMPTY get() = Point(0.0, 0.0)
    }

    // 重载 +
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    // 重载 -
    operator fun minus(other: Point): Point {
        return Point(x - other.x, y - other.y)
    }

    // 重载 *
    operator fun times(scale: Int): Point {
        return Point(x * scale, y * scale)
    }

    operator fun times(scale: Double): Point {
        return Point(x * scale, y * scale)
    }

    // 重载 /
    operator fun div(scale: Int): Point {
        return Point(x / scale, y / scale)
    }

    operator fun div(scale: Double): Point {
        return Point(x / scale, y / scale)
    }
}
