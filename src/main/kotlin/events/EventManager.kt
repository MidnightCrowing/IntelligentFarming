package com.midnightcrowing.events

import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.listeners.ClickListener
import com.midnightcrowing.events.listeners.EventListener
import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback


// 事件管理器
class EventManager(window: Window) {
    // 使用 Set 来确保每个事件类型的监听器是唯一的
    private val listeners = mutableMapOf<Class<out Event>, MutableSet<EventListener<*, *>>>()

    init {
        // 注入子监听器
        ClickListener(window, this)

        // 设置GLFW事件回调
        glfwSetMouseButtonCallback(window.handle) { _, button, action, mods ->
            listeners[MouseButtonEvent::class.java]?.forEach { listener ->
                @Suppress("UNCHECKED_CAST") // 告诉编译器忽略类型转换的警告，因类型转换在此时是安全的
                (listener as EventListener<*, MouseButtonEvent>).eventFilter(
                    MouseButtonEvent(button, action, mods)
                )
            }
        }
    }

    // 注册监听器
    fun <T : Event, E : Event> registerListener(listener: EventListener<T, E>) =
        listeners.computeIfAbsent(listener.getReceiveEventType()) { mutableSetOf() }.add(listener)


    // 注册组件的事件回调
    fun <T : Event> registerWidget(eventType: Class<T>, widget: Widget) =
        listeners.values.flatten().firstOrNull { it.getSendEventType() == eventType }?.registerWidget(widget)
}
