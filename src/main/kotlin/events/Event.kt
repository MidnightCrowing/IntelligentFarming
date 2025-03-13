package com.midnightcrowing.events


/**
 * 事件基类，所有事件的父类。
 */
sealed class Event {

    // 窗口事件（Window Events）

    /**
     * 窗口大小调整事件
     * @param width 新窗口宽度（像素）
     * @param height 新窗口高度（像素）
     */
    data class WindowResizeEvent(val width: Int, val height: Int) : Event()

    /**
     * 窗口关闭事件
     * @param timestamp 事件触发时间戳（毫秒）
     */
    data class WindowCloseEvent(val timestamp: Long) : Event()

    /**
     * 窗口焦点变化事件
     * @param focused `true` 表示窗口获得焦点，`false` 表示失去焦点
     */
    data class WindowFocusEvent(val focused: Boolean) : Event()

    /**
     * 窗口移动事件
     * @param x 窗口左上角新的 X 坐标（屏幕坐标）
     * @param y 窗口左上角新的 Y 坐标（屏幕坐标）
     */
    data class WindowMoveEvent(val x: Int, val y: Int) : Event()


    // 输入事件（Input Events）

    /**
     * 按键事件（键盘按下或释放）
     * @param key 按键代码（GLFW 提供的键码，如 GLFW_KEY_A）
     * @param action 按键行为（GLFW_PRESS、GLFW_RELEASE 或 GLFW_REPEAT）
     * @param mods 组合键修饰符（如 `GLFW_MOD_SHIFT`、`GLFW_MOD_CONTROL`）
     */
    data class KeyEvent(val key: Int, val action: Int, val mods: Int) : Event()

    /**
     * 鼠标按键事件
     * @param button 按键代码（GLFW_MOUSE_BUTTON_LEFT、GLFW_MOUSE_BUTTON_RIGHT 等）
     * @param action 按键行为（GLFW_PRESS 或 GLFW_RELEASE）
     * @param mods 组合键修饰符（如 `GLFW_MOD_SHIFT`、`GLFW_MOD_CONTROL`）
     */
    data class MouseButtonEvent(val button: Int, val action: Int, val mods: Int) : Event()

    /**
     * 鼠标移动事件
     * @param x 鼠标的 X 坐标（窗口内）
     * @param y 鼠标的 Y 坐标（窗口内）
     */
    data class CursorMoveEvent(val x: Double, val y: Double) : Event()

    /**
     * 鼠标滚轮事件
     * @param offsetX 水平方向滚动的偏移量
     * @param offsetY 垂直方向滚动的偏移量
     */
    data class ScrollEvent(val offsetX: Double, val offsetY: Double) : Event()

    /**
     * 文字输入事件（用于文本输入，支持 Unicode）
     * @param codepoint 输入的字符（Unicode 码点）
     */
    data class CharInputEvent(val codepoint: Int) : Event()


    // 监视器事件（Monitor Events）

    /**
     * 监视器连接/断开事件
     * @param monitor 发生变化的监视器 ID
     * @param event 事件类型（GLFW_CONNECTED 或 GLFW_DISCONNECTED）
     */
    data class MonitorEvent(val monitor: Long, val event: Int) : Event()
}


/**
 * 自定义事件类，继承自 Event
 */
sealed class CustomEvent : Event() {

    /**
     * 鼠标点击事件，鼠标松开时触发。如果鼠标拖拽到按钮区域之外释放则不会触发。
     * @param x 点击的 X 屏幕坐标
     * @param y 点击的 Y 屏幕坐标
     */
    data class MouseClickEvent(val x: Float, val y: Float) : Event()

    /**
     * 鼠标按下事件，鼠标按下时触发。
     * @param x 按下的 X 屏幕坐标
     * @param y 按下的 Y 屏幕坐标
     */
    data class MousePressedEvent(val x: Float, val y: Float) : Event()

    /**
     * 鼠标释放事件，鼠标松开时触发。即使鼠标拖拽到按钮区域之外释放也会触发。
     * @param x 释放的 X 屏幕坐标
     * @param y 释放的 Y 屏幕坐标
     */
    data class MouseReleasedEvent(val x: Float, val y: Float) : Event()

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
    data class MouseMoveEvent(val x: Float, val y: Float) : Event()
}
