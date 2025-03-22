package com.midnightcrowing.utils

object FontSizeUtil {
    fun calculateFontSize(
        windowWidth: Int,
        minFontSize: Double = 16.0, maxFontSize: Double = 24.0,
        minWindowWidth: Int = 1216, maxWindowWidth: Int = 1920,
    ): Double {
        return when {
            windowWidth <= minWindowWidth -> minFontSize
            windowWidth >= maxWindowWidth -> maxFontSize
            else -> {
                val scale = (windowWidth - minWindowWidth) / (maxWindowWidth - minWindowWidth)
                minFontSize + scale * (maxFontSize - minFontSize)
            }
        }
    }
}