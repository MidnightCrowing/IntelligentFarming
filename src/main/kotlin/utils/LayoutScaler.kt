package com.midnightcrowing.utils

object LayoutScaler {
    /**
     * 根据窗口宽度计算自适应数值（可用于字体大小、偏移量、间距等 UI 适配）
     *
     * @param windowWidth 当前窗口宽度
     * @param minValue 最小值（窗口较小时的值）
     * @param maxValue 最大值（窗口较大时的值）
     * @param minWindowWidth 适配的最小窗口宽度
     * @param maxWindowWidth 适配的最大窗口宽度
     * @return 计算出的适配值
     */
    fun scaleValue(
        windowWidth: Int,
        minValue: Double = 16.0, maxValue: Double = 24.0,
        minWindowWidth: Int = 1216, maxWindowWidth: Int = 1920,
    ): Double {
        return when {
            windowWidth <= minWindowWidth -> minValue
            windowWidth >= maxWindowWidth -> maxValue
            else -> {
                val scale = (windowWidth - minWindowWidth).toDouble() / (maxWindowWidth - minWindowWidth)
                minValue + scale * (maxValue - minValue)
            }
        }
    }
}