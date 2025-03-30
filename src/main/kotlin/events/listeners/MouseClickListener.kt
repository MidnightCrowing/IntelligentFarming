package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseClickEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions


class MouseClickListener(
    window: Window,
    eventManager: EventManager,
) : BaseMouseListener<MouseButtonEvent>(window, eventManager) {
    private val clickableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<MouseButtonEvent> = MouseButtonEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(MouseClickEvent::class)

    override fun eventFilter(event: MouseButtonEvent) {
        if (event.button == GLFW_MOUSE_BUTTON_LEFT && event.action == GLFW_PRESS) {
            triggerEvent(event)
        }
    }

    override fun triggerEvent(event: MouseButtonEvent) {
        val (x, y) = window.getCursorPos()
        val highestZWidget = getHighestZWidget(clickableWidgets, x, y, MouseClickEvent::class)
        highestZWidget?.onClick(MouseClickEvent(x, y))
    }

    override fun registerWidget(widget: Widget) {
        // 使用反射检查 widget 是否覆盖了 onClick 方法
        val onClickMethod = widget::class.declaredFunctions.find { it.name == "onClick" }

        // 判断该方法是否被实现（覆盖了父类的实现）
        if (onClickMethod != null && !onClickMethod.isAbstract) {
            clickableWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget) {
        clickableWidgets.remove(widget)
    }
}