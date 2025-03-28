package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseRightClickEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions

class MouseRightClickListener(
    val window: Window,
    eventManager: EventManager,
) : EventListener<MouseButtonEvent>(eventManager) {
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

        val widgetsCopy = clickableWidgets.toList()
        val highestZWidget = widgetsCopy
            .filter { it.isVisible }
            .filter { it.containsPoint(x, y, event = MouseRightClickEvent::class) }
            .maxByOrNull { it.z }

        highestZWidget?.onRightClick(MouseRightClickEvent(x, y))
    }

    override fun registerWidget(widget: Widget) {
        // 使用反射检查 widget 是否覆盖了 onRightClick 方法
        val onRightClickMethod = widget::class.declaredFunctions.find { it.name == "onRightClick" }

        // 判断该方法是否被实现（覆盖了父类的实现）
        if (onRightClickMethod != null && !onRightClickMethod.isAbstract) {
            clickableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget) {
        clickableWidgets.remove(widget)
    }
}