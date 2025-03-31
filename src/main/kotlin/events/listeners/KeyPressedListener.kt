package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.KeyPressedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.KeyEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions

class KeyPressedListener(
    val window: Window,
    eventManager: EventManager,
) : BaseEventListener<KeyEvent>(eventManager) {
    private val pressableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<KeyEvent> = KeyEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(KeyPressedEvent::class)

    override fun eventFilter(event: KeyEvent) {
        if (event.action == GLFW_PRESS) {
            triggerEvent(event)
        }
    }

    override fun triggerEvent(event: KeyEvent) {
        pressableWidgets
            .sortedByDescending { it.z }
            .forEach { widget ->
                if (!widget.onKeyPress(KeyPressedEvent(event.key))) {
                    return
                }
            }
    }

    override fun registerWidget(widget: Widget) {
        // Use reflection to check if the widget overrides the onKeyPress method
        val onKeyPressMethod = widget::class.declaredFunctions.find { it.name == "onKeyPress" }

        // Only register if the Widget overrides the onKeyPress method
        if (onKeyPressMethod != null && !onKeyPressMethod.isAbstract) {
            pressableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget) {
        pressableWidgets.remove(widget)
    }
}