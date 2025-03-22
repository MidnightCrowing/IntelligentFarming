package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.CursorMoveEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions


class MouseMoveListener(
    val window: Window,
    eventManager: EventManager,
) : EventListener<CursorMoveEvent>(eventManager) {
    private val enterListeners = mutableMapOf<Widget, Boolean>()
    private val leaveListeners = mutableMapOf<Widget, Boolean>()
    private val moveListeners = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<CursorMoveEvent> = CursorMoveEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> =
        arrayOf(MouseEnterEvent::class, MouseLeaveEvent::class, MouseMoveEvent::class)

    override fun eventFilter(event: CursorMoveEvent) = triggerEvent(event)

    override fun triggerEvent(event: CursorMoveEvent) {
        val mouseX = event.x
        val mouseY = event.y

        // 处理 MouseEnterEvent 和 MouseLeaveEvent
        handleMouseEnterAndLeave(mouseX, mouseY)

        // 处理 MouseMoveEvent
        val widgetsCopy = moveListeners.toList()
        val highestZWidget = widgetsCopy
            .filter { it.isVisible }
            .filter { it.containsPoint(mouseX, mouseY) }
            .maxByOrNull { it.z }

        highestZWidget?.onMouseMove(MouseMoveEvent(mouseX, mouseY))
    }

    private fun handleMouseEnterAndLeave(mouseX: Double, mouseY: Double) {
        // 处理 MouseEnterEvent
        enterListeners.forEach { (widget, hasEntered) ->
            val isInside = widget.containsPoint(mouseX, mouseY)
            if (isInside && !hasEntered) {
                widget.onMouseEnter()
                enterListeners[widget] = true
            } else if (!isInside && hasEntered) {
                enterListeners[widget] = false
            }
        }

        // 处理 MouseLeaveEvent
        leaveListeners.forEach { (widget, wasInside) ->
            val isInside = widget.containsPoint(mouseX, mouseY)
            if (!isInside && wasInside) {
                widget.onMouseLeave()
                leaveListeners[widget] = false
            } else if (isInside && !wasInside) {
                leaveListeners[widget] = true
            }
        }
    }

    override fun registerWidget(widget: Widget) {
        // 使用一个辅助函数来减少反射操作
        checkAndRegisterMethod(widget, "onMouseEnter")?.let { enterListeners[widget] = false }
        checkAndRegisterMethod(widget, "onMouseLeave")?.let { leaveListeners[widget] = false }
        checkAndRegisterMethod(widget, "onMouseMove")?.let { moveListeners.add(widget) }
    }

    private fun checkAndRegisterMethod(widget: Widget, methodName: String): (() -> Unit)? {
        val method = widget::class.declaredFunctions.find { it.name == methodName }
        return if (method != null && !method.isAbstract) {
            { method.call(widget) }
        } else {
            null
        }
    }

    override fun unregisterWidget(widget: Widget) {
        enterListeners.remove(widget)
        leaveListeners.remove(widget)
        moveListeners.remove(widget)
    }
}
