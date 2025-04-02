package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MousePressedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.events.annotations.MousePressEventHandler
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions


class MousePressedListener(
    window: Window,
    eventManager: EventManager,
) : BaseMouseListener<MouseButtonEvent>(window, eventManager) {
    private val pressableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<MouseButtonEvent> = MouseButtonEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(MousePressedEvent::class)

    override fun eventFilter(event: MouseButtonEvent) {
        if (event.action == GLFW_PRESS) {
            triggerEvent(event)
        }
    }

    override fun triggerEvent(event: MouseButtonEvent) {
        val (x, y) = window.getCursorPos()
        val highestZWidget = getHighestZWidget(pressableWidgets, x, y, MousePressedEvent::class)
        highestZWidget?.onMousePress(MousePressedEvent(x, y, event.button))
    }

    override fun registerWidget(widget: Widget, event: KClass<out Event>) {
        // 查找 onMousePress 方法并判断注解是否存在
        val hasMousePressAnnotation = widget::class.memberFunctions
            .find { it.name == "onMousePress" }
            ?.annotations
            ?.any { it is MousePressEventHandler } != true

        if (hasMousePressAnnotation) {
            pressableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget, event: KClass<out Event>) {
        pressableWidgets.remove(widget)
    }
}