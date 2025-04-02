package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseReleasedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.events.annotations.MouseReleaseEventHandler
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions


class MouseReleasedListener(
    window: Window,
    eventManager: EventManager,
) : BaseMouseListener<MouseButtonEvent>(window, eventManager) {
    private val releaseableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<MouseButtonEvent> = MouseButtonEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(MouseReleasedEvent::class)

    override fun eventFilter(event: MouseButtonEvent) {
        if (event.action == GLFW_RELEASE) {
            triggerEvent(event)
        }
    }

    override fun triggerEvent(event: MouseButtonEvent) {
        val (x, y) = window.getCursorPos()
        val highestZWidget = getHighestZWidget(releaseableWidgets, x, y, MouseReleasedEvent::class)
        highestZWidget?.onMouseRelease(MouseReleasedEvent(x, y, event.button))
    }

    override fun registerWidget(widget: Widget, event: KClass<out Event>) {
        // 查找 onMouseRelease 方法并判断注解是否存在
        val hasMouseReleaseAnnotation = widget::class.memberFunctions
            .find { it.name == "onMouseRelease" }
            ?.annotations
            ?.any { it is MouseReleaseEventHandler } != true

        if (hasMouseReleaseAnnotation) {
            releaseableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget, event: KClass<out Event>) {
        releaseableWidgets.remove(widget)
    }
}