package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.CursorMoveEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import kotlin.reflect.full.declaredFunctions

/**
 * 鼠标移入事件
 */
object MouseEnterEvent : Event()

/**
 * 鼠标移出事件
 */
object MouseLeaveEvent : Event()

/**
 * 鼠标移动事件
 */
data class MouseMoveEvent(val x: Float, val y: Float) : Event()


class MouseMoveListener(
    val window: Window,
    eventManager: EventManager,
) : EventListener<CursorMoveEvent>(eventManager) {
    private val enterListeners = mutableMapOf<Widget, Boolean>()
    private val leaveListeners = mutableMapOf<Widget, Boolean>()
    private val moveListeners = mutableListOf<Widget>()

    override fun getReceiveEventType(): Class<CursorMoveEvent> = CursorMoveEvent::class.java

    override fun getSendEventType(): Array<Class<out Event>> =
        arrayOf(MouseEnterEvent::class.java, MouseLeaveEvent::class.java, MouseMoveEvent::class.java)

    override fun eventFilter(event: CursorMoveEvent) = triggerEvent(event)

    override fun triggerEvent(event: CursorMoveEvent) {
        val mouseX = event.x.toFloat()
        val mouseY = event.y.toFloat()

        // 处理 MouseEnterEvent 和 MouseLeaveEvent
        handleMouseEnterAndLeave(mouseX, mouseY)

        // 处理 MouseMoveEvent
        for (widget in moveListeners) {
            if (widget.containsPoint(mouseX, mouseY)) {
                widget.onMouseMove(MouseMoveEvent(mouseX, mouseY))
            }
        }
    }

    private fun handleMouseEnterAndLeave(mouseX: Float, mouseY: Float) {
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
}
