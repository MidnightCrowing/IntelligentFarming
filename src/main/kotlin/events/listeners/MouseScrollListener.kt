package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseScrollEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.ScrollEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.events.annotations.MouseScrollEventHandler
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

class MouseScrollListener(
    val window: Window,
    eventManager: EventManager,
) : BaseEventListener<ScrollEvent>(eventManager) {
    private val scrollWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<ScrollEvent> = ScrollEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(MouseScrollEvent::class)

    override fun eventFilter(event: ScrollEvent) = triggerEvent(event)

    override fun triggerEvent(event: ScrollEvent) {
        val (x, y) = window.getCursorPos()

        val widgetsCopy = scrollWidgets.toList()
        val highestZWidget = widgetsCopy
            .filter { it.isVisible }
            .filter { it.containsPoint(x, y, event = MouseScrollEvent::class) }
            .maxByOrNull { it.z }

        highestZWidget?.onMouseScroll(MouseScrollEvent(event.offsetX, event.offsetY))
    }

    override fun registerWidget(widget: Widget, event: KClass<out Event>) {
        // 查找 onMouseScroll 方法并判断注解是否存在
        val hasMouseScrollAnnotation = widget::class.memberFunctions
            .find { it.name == "onMouseScroll" }
            ?.annotations
            ?.any { it is MouseScrollEventHandler } != true

        if (hasMouseScrollAnnotation) {
            scrollWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget, event: KClass<out Event>) {
        scrollWidgets.remove(widget)
    }
}