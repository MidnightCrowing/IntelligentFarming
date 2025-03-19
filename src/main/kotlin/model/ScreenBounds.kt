package com.midnightcrowing.model

import com.midnightcrowing.gui.base.Window


// 屏幕坐标系的边界
data class ScreenBounds(val x1: Float, val y1: Float, val x2: Float, val y2: Float) {
    companion object {
        val EMPTY = ScreenBounds(0f, 0f, 0f, 0f)
    }

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
        val (left, top) = convertScreenToNdc(window, x1, y1)
        val (right, bottom) = convertScreenToNdc(window, x2, y2)
        return NdcBounds(left, top, right, bottom)
    }
}
