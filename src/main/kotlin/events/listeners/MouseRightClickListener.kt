package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseRightClickEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.events.annotations.MouseRightClickEventHandler
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

class MouseRightClickListener(
    window: Window,
    eventManager: EventManager,
) : BaseMouseListener<MouseButtonEvent>(window, eventManager) {
    private val clickableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<MouseButtonEvent> = MouseButtonEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(MouseRightClickEvent::class)

    override fun eventFilter(event: MouseButtonEvent) {
        if (event.button == GLFW_MOUSE_BUTTON_RIGHT && event.action == GLFW_PRESS) {
            triggerEvent(event)
        }
    }

    override fun triggerEvent(event: MouseButtonEvent) {
        val (x, y) = window.getCursorPos()
        val highestZWidget = getHighestZWidget(clickableWidgets, x, y, MouseRightClickEvent::class)
        highestZWidget?.onRightClick(MouseRightClickEvent(x, y))
    }

    override fun registerWidget(widget: Widget, event: KClass<out Event>) {
        // 查找 onRightClick 方法并判断注解是否存在
        val hasRightClickAnnotation = widget::class.memberFunctions
            .find { it.name == "onRightClick" }
            ?.annotations
            ?.any { it is MouseRightClickEventHandler } != true

        if (hasRightClickAnnotation) {
            clickableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget, event: KClass<out Event>) {
        clickableWidgets.remove(widget)
    }
}