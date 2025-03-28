package com.midnightcrowing.events

import com.midnightcrowing.events.Event.*
import com.midnightcrowing.events.listeners.*
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import org.lwjgl.glfw.GLFW.*
import kotlin.reflect.KClass


// 事件管理器
class EventManager(val window: Window) {
    // 使用 Set 来确保每个事件类型的监听器是唯一的
    private val listeners = mutableMapOf<KClass<out Event>, MutableSet<EventListener<out Event>>>()

    /* 设置GLFW事件回调 */
    fun initGLFWCallback() {
        // 监听鼠标点击
        glfwSetMouseButtonCallback(window.handle) { _, button, action, mods ->
            triggerMouseButtonEvent(button, action, mods)
        }
        // 监听鼠标移动
        glfwSetCursorPosCallback(window.handle) { _, xPos, yPos ->
            triggerCursorPosEvent(xPos, yPos)
        }
        // 监听鼠标滚轮
        glfwSetScrollCallback(window.handle) { _, offsetX, offsetY ->
            triggerScrollEvent(offsetX, offsetY)
        }
        // 监听窗口大小变化
        glfwSetFramebufferSizeCallback(window.handle) { _, width, height ->
            triggerWindowResizeEvent(width, height)
        }
        // 监听键盘按下
        glfwSetKeyCallback(window.handle) { _, key, scancode, action, mods ->
            triggerKeyEvent(key, action, mods)
        }
    }

    fun triggerMouseButtonEvent(button: Int, action: Int, mods: Int) {
        listeners[MouseButtonEvent::class]?.forEach { listener ->
            @Suppress("UNCHECKED_CAST") // 告诉编译器忽略类型转换的警告，因类型转换在此时是安全的
            (listener as EventListener<MouseButtonEvent>).eventFilter(
                MouseButtonEvent(button, action, mods)
            )
        }
    }

    fun triggerCursorPosEvent(xPos: Double, yPos: Double) {
        listeners[CursorMoveEvent::class]?.forEach { listener ->
            @Suppress("UNCHECKED_CAST")
            (listener as EventListener<CursorMoveEvent>).eventFilter(
                CursorMoveEvent(xPos, yPos)
            )
        }
    }

    fun triggerScrollEvent(offsetX: Double, offsetY: Double) {
        listeners[ScrollEvent::class]?.forEach { listener ->
            @Suppress("UNCHECKED_CAST")
            (listener as EventListener<ScrollEvent>).eventFilter(
                ScrollEvent(offsetX, offsetY)
            )
        }
    }

    fun triggerWindowResizeEvent(width: Int, height: Int) {
        listeners[WindowResizeEvent::class]?.forEach { listener ->
            @Suppress("UNCHECKED_CAST")
            (listener as EventListener<WindowResizeEvent>).eventFilter(
                WindowResizeEvent(width, height)
            )
        }
    }

    fun triggerKeyEvent(key: Int, action: Int, mods: Int) {
        listeners[KeyEvent::class]?.forEach { listener ->
            @Suppress("UNCHECKED_CAST")
            (listener as EventListener<KeyEvent>).eventFilter(
                KeyEvent(key, action, mods)
            )
        }
    }

    fun initListener() {
        // 初始化监听器
        MouseClickListener(window, this)
        MouseRightClickListener(window, this)
        MouseMoveListener(window, this)
        MousePressedListener(window, this)
        MouseReleasedListener(window, this)
        MouseScrollListener(window, this)
        KeyPressedListener(window, this)
        WindowResizeEventListener(window, this)
    }

    // 注册监听器
    fun <T : Event> registerListener(listener: EventListener<T>) {
        // 获取接收事件类型的 class
        val eventType = listener.getReceiveEventType()

        // 获取当前事件类型的监听集合（如果没有则创建一个新的集合）
        val eventListeners = listeners.computeIfAbsent(eventType) { mutableSetOf() }

        // 将监听器添加到事件类型对应的监听集合中
        eventListeners.add(listener)
    }

    // 注册组件的事件回调
    fun <T : Event> registerWidget(eventType: KClass<T>, widget: Widget) {
        // 查找与事件类型匹配的第一个监听器
        val listener = listeners.values
            .flatten() // 将所有的监听器集合平铺
            .firstOrNull { it.getSendEventType().contains(eventType) }

        // 如果找到了相应的监听器，则注册组件的事件回调
        listener?.registerWidget(widget)
    }

    // 取消注册组件的事件回调
    fun <T : Event> unregisterWidget(eventType: KClass<T>, widget: Widget) {
        // 查找与事件类型匹配的第一个监听器
        val listener = listeners.values
            .flatten() // 将所有的监听器集合平铺
            .firstOrNull { it.getSendEventType().contains(eventType) }

        // 如果找到了相应的监听器，则取消注册组件的事件回调
        listener?.unregisterWidget(widget)
    }
}
