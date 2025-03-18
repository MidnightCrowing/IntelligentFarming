package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseReleasedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import org.lwjgl.glfw.GLFW.*
import kotlin.reflect.full.declaredFunctions


class MouseReleasedListener(
    val window: Window,
    eventManager: EventManager,
) : EventListener<MouseButtonEvent>(eventManager) {
    private val releaseableWidgets = mutableListOf<Widget>()
    private var pressedWidget: Widget? = null

    override fun getReceiveEventType(): Class<MouseButtonEvent> = MouseButtonEvent::class.java

    override fun getSendEventType(): Array<Class<out Event>> = arrayOf(MouseReleasedEvent::class.java)

    override fun eventFilter(event: MouseButtonEvent) {
        if (event.button == GLFW_MOUSE_BUTTON_LEFT) {
            if (event.action == GLFW_PRESS) {
                recordPressedWidget()
            } else if (event.action == GLFW_RELEASE) {
                triggerEvent(event)
            }
        }
    }

    private fun recordPressedWidget() {
        val (x, y) = DoubleArray(1).let { xPos ->
            DoubleArray(1).let { yPos ->
                glfwGetCursorPos(window.handle, xPos, yPos)
                xPos[0].toFloat() to yPos[0].toFloat()
            }
        }

        pressedWidget = releaseableWidgets.find { it.containsPoint(x, y) }
    }

    override fun triggerEvent(event: MouseButtonEvent) {
        val (x, y) = DoubleArray(1).let { xPos ->
            DoubleArray(1).let { yPos ->
                glfwGetCursorPos(window.handle, xPos, yPos)
                xPos[0].toFloat() to yPos[0].toFloat()
            }
        }

        pressedWidget?.onMouseRelease(MouseReleasedEvent(x, y))
        pressedWidget = null
    }

    override fun registerWidget(widget: Widget) {
        // 使用反射检查 widget 是否覆盖了 onMouseRelease 方法
        val onMouseReleaseMethod = widget::class.declaredFunctions.find { it.name == "onMouseRelease" }

        // 判断该方法是否被实现（覆盖了父类的实现）
        if (onMouseReleaseMethod != null && !onMouseReleaseMethod.isAbstract) {
            releaseableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget) {
        releaseableWidgets.remove(widget)
    }
}