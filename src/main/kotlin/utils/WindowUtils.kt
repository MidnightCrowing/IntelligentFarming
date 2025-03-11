package com.midnightcrowing.utils

import com.midnightcrowing.gui.Window

// 坐标系类型
enum class CoordinateSystem { SCREEN, NDC }

object WindowUtils {

    /**
     * 将 NDC (Normalized Device Coordinates) 归一化坐标转换为屏幕坐标。
     *
     * @param window 当前窗口对象，提供屏幕的宽度和高度信息
     * @param x NDC 坐标系中的 X 坐标，范围为 [-1, 1]
     * @param y NDC 坐标系中的 Y 坐标，范围为 [-1, 1]
     * @return 转换后的屏幕坐标 (screenX, screenY)，范围为 [0, window.width] 和 [0, window.height]
     */
    fun convertNdcToScreen(window: Window, x: Float, y: Float): Pair<Float, Float> {
        val screenX = (x + 1) * (window.width / 2f)
        val screenY = (-y + 1) * (window.height / 2f) // OpenGL NDC Y 轴向上，屏幕 Y 轴向下
        return screenX to screenY
    }

    /**
     * 将屏幕坐标转换为 NDC (Normalized Device Coordinates) 归一化坐标。
     *
     * @param window 当前窗口对象，提供屏幕的宽度和高度信息
     * @param x 屏幕坐标系中的 X 坐标，范围为 [0, window.width]
     * @param y 屏幕坐标系中的 Y 坐标，范围为 [0, window.height]
     * @return 转换后的 NDC 坐标 (ndcX, ndcY)，范围为 [-1, 1]
     */
    fun convertScreenToNdc(window: Window, x: Float, y: Float): Pair<Float, Float> {
        val ndcX = (x / (window.width / 2f)) - 1
        val ndcY = -((y / (window.height / 2f)) - 1) // 反向 Y 轴坐标
        return ndcX to ndcY
    }

    /**
     * 将两个屏幕坐标区间转换为 NDC 坐标区间。
     *
     * @param window 当前窗口对象，提供屏幕的宽度和高度信息
     * @param x1 屏幕坐标区间的左边界 X 坐标
     * @param y1 屏幕坐标区间的上边界 Y 坐标
     * @param x2 屏幕坐标区间的右边界 X 坐标
     * @param y2 屏幕坐标区间的下边界 Y 坐标
     * @return 转换后的 NDC 坐标区间 (left, top, right, bottom)
     */
    fun convertScreenToNdcBounds(window: Window, x1: Float, y1: Float, x2: Float, y2: Float): List<Float> {
        val (left, top) = convertScreenToNdc(window, x1, y1)
        val (right, bottom) = convertScreenToNdc(window, x2, y2)
        return listOf(left, top, right, bottom)
    }

    /**
     * 将屏幕 X 坐标转换为 NDC 坐标。
     *
     * @param window 当前窗口对象，提供屏幕的宽度信息
     * @param screenX 屏幕 X 坐标，范围为 [0, window.width]
     * @return 转换后的 NDC X 坐标，范围为 [-1, 1]
     */
    fun convertScreenToNdcX(window: Window, screenX: Float): Float {
        return convertScreenToNdc(window, screenX, 0f).first
    }

    /**
     * 将屏幕 Y 坐标转换为 NDC 坐标。
     *
     * @param window 当前窗口对象，提供屏幕的高度信息
     * @param screenY 屏幕 Y 坐标，范围为 [0, window.height]
     * @return 转换后的 NDC Y 坐标，范围为 [-1, 1]
     */
    fun convertScreenToNdcY(window: Window, screenY: Float): Float {
        return convertScreenToNdc(window, 0f, screenY).second
    }
}
