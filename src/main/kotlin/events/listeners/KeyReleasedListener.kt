package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.KeyReleasedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.KeyEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.events.annotations.KeyReleaseEventHandler
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

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
            .filter { it.isVisible }
            .sortedByDescending { it.z }
            .forEach { widget ->
                if (!widget.onKeyReleased(KeyReleasedEvent(event.key))) {
                    return
                }
            }
    }

    override fun registerWidget(widget: Widget, event: KClass<out Event>) {
        // 查找 onKeyReleased 方法并判断注解是否存在
        val hasKeyReleasedAnnotation = widget::class.memberFunctions
            .find { it.name == "onKeyReleased" }
            ?.annotations
            ?.any { it is KeyReleaseEventHandler } != true

        if (hasKeyReleasedAnnotation) {
            releasedableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget, event: KClass<out Event>) {
        releasedableWidgets.remove(widget)
    }
}