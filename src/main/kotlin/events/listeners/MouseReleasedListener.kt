package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.MouseReleasedEvent
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions


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