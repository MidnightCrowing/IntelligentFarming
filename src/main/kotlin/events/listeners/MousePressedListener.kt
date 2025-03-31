package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MousePressedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions


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

    override fun registerWidget(widget: Widget) {
        // Use reflection to check if the widget overrides the onMousePress method
        val onMousePressMethod = widget::class.declaredFunctions.find { it.name == "onMousePress" }

        // Only register if the Widget overrides the onMousePress method
        if (onMousePressMethod != null && !onMousePressMethod.isAbstract) {
            pressableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget) {
        pressableWidgets.remove(widget)
    }
}