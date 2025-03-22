package com.midnightcrowing.resource

enum class ColorEnum(val value: DoubleArray) {
    WHITE(doubleArrayOf(1.0, 1.0, 1.0, 1.0)),
    BLACK(doubleArrayOf(0.0, 0.0, 0.0, 1.0)),
    RED(doubleArrayOf(1.0, 0.0, 0.0, 1.0)),
    GREEN(doubleArrayOf(0.0, 1.0, 0.0, 1.0)),
    BLUE(doubleArrayOf(0.0, 0.0, 1.0, 1.0)),
    YELLOW(doubleArrayOf(1.0, 1.0, 0.0, 1.0)),
}