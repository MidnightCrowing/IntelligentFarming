package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import org.lwjgl.glfw.GLFW.*
import kotlin.reflect.full.declaredFunctions


class MouseClickListener(
    val window: Window,
    eventManager: EventManager,
) : EventListener<MouseButtonEvent>(eventManager) {
    private val clickableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): Class<MouseButtonEvent> = MouseButtonEvent::class.java

    override fun getSendEventType(): Array<Class<out Event>> = arrayOf(MouseClickEvent::class.java)

    override fun eventFilter(event: MouseButtonEvent) {
        if (event.button == GLFW_MOUSE_BUTTON_LEFT && event.action == GLFW_PRESS) {
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

        clickableWidgets.forEach { widget ->
            if (widget.containsPoint(x, y)) {
                widget.onClick(MouseClickEvent(x, y))
            }
        }
    }

    override fun registerWidget(widget: Widget) {
        // 使用反射检查 widget 是否覆盖了 onClick 方法
        val onClickMethod = widget::class.declaredFunctions.find { it.name == "onClick" }

        // 判断该方法是否被实现（覆盖了父类的实现）
        if (onClickMethod != null && !onClickMethod.isAbstract) {
            clickableWidgets.add(widget)
        }
    }
}