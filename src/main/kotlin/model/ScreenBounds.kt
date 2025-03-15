package com.midnightcrowing.model

import com.midnightcrowing.gui.Window


// 屏幕坐标系的边界
data class ScreenBounds(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    /**
     * 将屏幕坐标转换为 NDC (Normalized Device Coordinates) 归一化坐标。
     *
     * @param window 当前窗口对象，提供屏幕的宽度和高度信息
     * @param x 屏幕坐标系中的 X 坐标，范围为 [0, window.width]
     * @param y 屏幕坐标系中的 Y 坐标，范围为 [0, window.height]
     * @return 转换后的 NDC 坐标 (ndcX, ndcY)，范围为 [-1, 1]
     */
    private fun convertScreenToNdc(window: Window, x: Float, y: Float): Point {
        val ndcX = (x / (window.width / 2f)) - 1
        val ndcY = -((y / (window.height / 2f)) - 1) // 反向 Y 轴坐标
        return Point(ndcX, ndcY)
    }

    fun toNdcBounds(window: Window): NdcBounds {
        val (left, top) = convertScreenToNdc(window, left, top)
        val (right, bottom) = convertScreenToNdc(window, right, bottom)
        return NdcBounds(left, top, right, bottom)
    }
}
