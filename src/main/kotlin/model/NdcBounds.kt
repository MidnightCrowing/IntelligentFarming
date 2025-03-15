package com.midnightcrowing.model

import com.midnightcrowing.gui.Window


// NDC坐标系的边界
data class NdcBounds(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    /**
     * 将 NDC (Normalized Device Coordinates) 归一化坐标转换为屏幕坐标。
     *
     * @param window 当前窗口对象，提供屏幕的宽度和高度信息
     * @param x NDC 坐标系中的 X 坐标，范围为 [-1, 1]
     * @param y NDC 坐标系中的 Y 坐标，范围为 [-1, 1]
     * @return 转换后的屏幕坐标 (screenX, screenY)，范围为 [0, window.width] 和 [0, window.height]
     */
    private fun convertNdcToScreen(window: Window, x: Float, y: Float): Point {
        val screenX = (x + 1) * (window.width / 2f)
        val screenY = (-y + 1) * (window.height / 2f) // OpenGL NDC Y 轴向上，屏幕 Y 轴向下
        return Point(screenX, screenY)
    }

    fun toScreenBounds(window: Window): ScreenBounds {
        val (left, top) = convertNdcToScreen(window, left, top)
        val (right, bottom) = convertNdcToScreen(window, right, bottom)
        return ScreenBounds(left, top, right, bottom)
    }
}
