package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.KeyReleasedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.KeyEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions

class KeyReleasedListener(
    val window: Window,
    eventManager: EventManager,
) : BaseEventListener<KeyEvent>(eventManager) {
    private val releasedableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<KeyEvent> = KeyEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(KeyReleasedEvent::class)

    override fun eventFilter(event: KeyEvent) {
        if (event.action == GLFW_RELEASE) {
            triggerEvent(event)
        }
    }

    override fun triggerEvent(event: KeyEvent) {
        releasedableWidgets
            .sortedByDescending { it.z }
            .forEach { widget ->
                if (!widget.onKeyReleased(KeyReleasedEvent(event.key))) {
                    return
                }
            }
    }

    override fun registerWidget(widget: Widget) {
        // Use reflection to check if the widget overrides the onKeyReleased method
        val onKeyReleasedMethod = widget::class.declaredFunctions.find { it.name == "onKeyReleased" }

        // Only register if the Widget overrides the onKeyReleased method
        if (onKeyReleasedMethod != null && !onKeyReleasedMethod.isAbstract) {
            releasedableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget) {
        releasedableWidgets.remove(widget)
    }
}