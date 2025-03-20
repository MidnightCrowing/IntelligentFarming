package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseRightClickEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import org.lwjgl.glfw.GLFW.*
import kotlin.reflect.full.declaredFunctions

class MouseRightClickListener(
    val window: Window,
    eventManager: EventManager,
) : EventListener<MouseButtonEvent>(eventManager) {
    private val clickableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): Class<MouseButtonEvent> = MouseButtonEvent::class.java

    override fun getSendEventType(): Array<Class<out Event>> = arrayOf(MouseRightClickEvent::class.java)

    override fun eventFilter(event: MouseButtonEvent) {
        if (event.button == GLFW_MOUSE_BUTTON_RIGHT && event.action == GLFW_PRESS) {
            triggerEvent(event)
        }
    }

    override fun triggerEvent(event: MouseButtonEvent) {
        val (x, y) = DoubleArray(1).let { xPos ->
            DoubleArray(1).let { yPos ->
                glfwGetCursorPos(window.handle, xPos, yPos)
                xPos[0].toFloat() to yPos[0].toFloat()
            }
        }

        val widgetsCopy = clickableWidgets.toList()
        widgetsCopy.forEach { widget ->
            if (widget.containsPoint(x, y)) {
                widget.onRightClick(MouseRightClickEvent(x, y))
            }
        }
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