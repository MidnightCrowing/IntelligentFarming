package com.midnightcrowing.resource

enum class ColorEnum(val value: FloatArray) {
    WHITE(floatArrayOf(1f, 1f, 1f, 1f)),
    BLACK(floatArrayOf(0f, 0f, 0f, 1f)),
    RED(floatArrayOf(1f, 0f, 0f, 1f)),
    GREEN(floatArrayOf(0f, 1f, 0f, 1f)),
    BLUE(floatArrayOf(0f, 0f, 1f, 1f)),
    YELLOW(floatArrayOf(1f, 1f, 0f, 1f)),
}