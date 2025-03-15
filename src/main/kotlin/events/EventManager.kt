package com.midnightcrowing.events

import com.midnightcrowing.events.Event.CursorMoveEvent
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.listeners.*
import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback
import org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback


// 事件管理器
class EventManager(window: Window) {
    // 使用 Set 来确保每个事件类型的监听器是唯一的
    private val listeners = mutableMapOf<Class<out Event>, MutableSet<EventListener<out Event>>>()

    init {
        // 注入子监听器
        MouseClickListener(window, this)
        MouseMoveListener(window, this)
        MousePressedListener(window, this)
        MouseReleasedListener(window, this)

        // 设置GLFW事件回调
        // 监听鼠标点击
        glfwSetMouseButtonCallback(window.handle) { _, button, action, mods ->
            listeners[MouseButtonEvent::class.java]?.forEach { listener ->
                @Suppress("UNCHECKED_CAST") // 告诉编译器忽略类型转换的警告，因类型转换在此时是安全的
                (listener as EventListener<MouseButtonEvent>).eventFilter(
                    MouseButtonEvent(button, action, mods)
                )
            }
        }
        // 监听鼠标移动
        glfwSetCursorPosCallback(window.handle) { _, xPos, yPos ->
            listeners[CursorMoveEvent::class.java]?.forEach { listener ->
                @Suppress("UNCHECKED_CAST")
                (listener as EventListener<CursorMoveEvent>).eventFilter(
                    CursorMoveEvent(xPos, yPos)
                )
            }
        }
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
    fun <T : Event> registerWidget(eventType: Class<T>, widget: Widget) {
        // 查找与事件类型匹配的第一个监听器
        val listener = listeners.values
            .flatten() // 将所有的监听器集合平铺
            .firstOrNull { it.getSendEventType().contains(eventType) }

        // 如果找到了相应的监听器，则注册组件的事件回调
        listener?.registerWidget(widget)
    }

    // 取消注册组件的事件回调
    fun <T : Event> unregisterWidget(eventType: Class<T>, widget: Widget) {
        // 查找与事件类型匹配的第一个监听器
        val listener = listeners.values
            .flatten() // 将所有的监听器集合平铺
            .firstOrNull { it.getSendEventType().contains(eventType) }

        // 如果找到了相应的监听器，则取消注册组件的事件回调
        listener?.unregisterWidget(widget)
    }
}
