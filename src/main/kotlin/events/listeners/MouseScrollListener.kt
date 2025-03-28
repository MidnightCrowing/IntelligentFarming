package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseScrollEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.ScrollEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions

class MouseScrollListener(
    val window: Window,
    eventManager: EventManager,
) : EventListener<ScrollEvent>(eventManager) {
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

        highestZWidget?.onScroll(MouseScrollEvent(event.offsetX, event.offsetY))
    }

    override fun registerWidget(widget: Widget) {
        // 使用反射检查 widget 是否覆盖了 onScroll 方法
        val onScrollMethod = widget::class.declaredFunctions.find { it.name == "onScroll" }

        // 判断该方法是否被实现（覆盖了父类的实现）
        if (onScrollMethod != null && !onScrollMethod.isAbstract) {
            scrollWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget) {
        scrollWidgets.remove(widget)
    }
}