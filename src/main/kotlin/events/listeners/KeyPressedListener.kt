package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.KeyPressedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.KeyEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.events.annotations.KeyPressEventHandler
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

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
        window.handleKeyPress(event.key)

        pressableWidgets
            .filter { it.isVisible }
            .sortedByDescending { it.z }
            .forEach { widget ->
                if (!widget.onKeyPress(KeyPressedEvent(event.key))) {
                    return
                }
            }
    }

    override fun registerWidget(widget: Widget, event: KClass<out Event>) {
        // 查找 onKeyPress 方法并判断注解是否存在
        val hasKeyPressAnnotation = widget::class.memberFunctions
            .find { it.name == "onKeyPress" }
            ?.annotations
            ?.any { it is KeyPressEventHandler } != true

        if (hasKeyPressAnnotation) {
            pressableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget, event: KClass<out Event>) {
        pressableWidgets.remove(widget)
    }
}