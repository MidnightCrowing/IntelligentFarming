package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.WindowResizeEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.events.annotations.WindowResizeEventHandler
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

class WindowResizeEventListener(
    val window: Window,
    eventManager: EventManager,
) : BaseEventListener<WindowResizeEvent>(eventManager) {
    private val registerWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<WindowResizeEvent> = WindowResizeEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(WindowResizeEvent::class)

    override fun eventFilter(event: WindowResizeEvent) = triggerEvent(event)

    override fun triggerEvent(event: WindowResizeEvent) {
        // 调用 Window 和 screen 的回调方法
        window.handleResize(event.width, event.height)
        window.screen.place(event.width, event.height)

        // 触发事件
        val widgetsCopy = registerWidgets.toList()
        widgetsCopy.forEach { widget ->
            widget.onWindowResize(WindowResizeEvent(event.width, event.height))
        }
    }

    override fun registerWidget(widget: Widget, event: KClass<out Event>) {
        // 查找 onWindowResize 方法并判断注解是否存在
        val hasWindowResizeAnnotation = widget::class.memberFunctions
            .find { it.name == "onWindowResize" }
            ?.annotations
            ?.any { it is WindowResizeEventHandler } != true

        if (hasWindowResizeAnnotation) {
            registerWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget, event: KClass<out Event>) {
        registerWidgets.remove(widget)
    }
}