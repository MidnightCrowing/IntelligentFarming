package com.midnightcrowing.events


/**
 * 自定义事件类，继承自 Event
 */
sealed class CustomEvent : Event() {

    /**
     * 鼠标点击事件，鼠标按下时触发。如果鼠标拖拽到按钮区域之外释放则不会触发。
     * @param x 点击的 X 屏幕坐标
     * @param y 点击的 Y 屏幕坐标
     */
    data class MouseClickEvent(val x: Double, val y: Double) : Event()

    /**
     * 鼠标右键点击事件，鼠标按下时触发。如果鼠标拖拽到按钮区域之外释放则不会触发。
     * @param x 双击的 X 屏幕坐标
     * @param y 双击的 Y 屏幕坐标
     */
    data class MouseRightClickEvent(val x: Double, val y: Double) : Event()

    /**
     * 鼠标按下事件，鼠标按下时触发。
     * @param x 按下的 X 屏幕坐标
     * @param y 按下的 Y 屏幕坐标
     */
    data class MousePressedEvent(val x: Double, val y: Double, val button: Int) : Event()

    /**
     * 鼠标释放事件，鼠标松开时触发。即使鼠标拖拽到按钮区域之外释放也会触发。
     * @param x 释放的 X 屏幕坐标
     * @param y 释放的 Y 屏幕坐标
     */
    data class MouseReleasedEvent(val x: Double, val y: Double, val button: Int) : Event()

    /**
     * 鼠标移入事件
     */
    object MouseEnterEvent : Event()

    /**
     * 鼠标移出事件
     */
    object MouseLeaveEvent : Event()

    /**
     * 鼠标移动事件
     * @param x 鼠标的 X 屏幕坐标
     * @param y 鼠标的 Y 屏幕坐标
     */
    data class MouseMoveEvent(val x: Double, val y: Double) : Event()

    /**
     * 鼠标滚轮事件
     * @param offsetX 滚动的 X 偏移量
     * @param offsetY 滚动的 Y 偏移量
     */
    data class MouseScrollEvent(val offsetX: Double, val offsetY: Double) : Event()

    /**
     * 按键按下事件
     * @param key 按键代码
     */
    data class KeyPressedEvent(val key: Int) : Event()

    /**
     * 按键释放事件
     * @param key 按键代码
     */
    data class KeyReleasedEvent(val key: Int) : Event()
}