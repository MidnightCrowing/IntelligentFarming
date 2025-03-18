package com.midnightcrowing.utils

object FontSizeUtil {
    fun calculateFontSize(
        windowWidth: Int,
        minFontSize: Float = 16f, maxFontSize: Float = 24f,
        minWindowWidth: Int = 1216, maxWindowWidth: Int = 1920,
    ): Float {
        return when {
            windowWidth <= minWindowWidth -> minFontSize
            windowWidth >= maxWindowWidth -> maxFontSize
            else -> {
                val scale = (windowWidth - minWindowWidth).toFloat() / (maxWindowWidth - minWindowWidth)
                minFontSize + scale * (maxFontSize - minFontSize)
            }
        }
    }
}