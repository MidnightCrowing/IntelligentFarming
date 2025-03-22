package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MousePressedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import org.lwjgl.glfw.GLFW.*
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions


class MousePressedListener(
    val window: Window,
    eventManager: EventManager,
) : EventListener<MouseButtonEvent>(eventManager) {
    private val pressableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<MouseButtonEvent> = MouseButtonEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(MousePressedEvent::class)

    override fun eventFilter(event: MouseButtonEvent) {
        if (event.button == GLFW_MOUSE_BUTTON_LEFT && event.action == GLFW_PRESS) {
            triggerEvent(event)
        }
    }

    override fun triggerEvent(event: MouseButtonEvent) {
        val (x, y) = DoubleArray(1).let { xPos ->
            DoubleArray(1).let { yPos ->
                glfwGetCursorPos(window.handle, xPos, yPos)
                xPos[0] to yPos[0]
            }
        }

        val widgetsCopy = pressableWidgets.toList()
        val highestZWidget = widgetsCopy
            .filter { it.isVisible }
            .filter { it.containsPoint(x, y) }
            .maxByOrNull { it.z }

        highestZWidget?.onMousePress(MousePressedEvent(x, y))
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