package com.midnightcrowing.model


data class Point(var x: Double, var y: Double) {
    companion object {
        val EMPTY = Point(0.0, 0.0)
    }
}
