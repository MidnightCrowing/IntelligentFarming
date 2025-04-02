package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.CursorMoveEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.events.annotations.MouseEnterEventHandler
import com.midnightcrowing.events.annotations.MouseLeaveEventHandler
import com.midnightcrowing.events.annotations.MouseMoveEventHandler
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions


class MouseMoveListener(
    window: Window,
    eventManager: EventManager,
) : BaseMouseListener<CursorMoveEvent>(window, eventManager) {
    private val enterListeners = mutableMapOf<Widget, Boolean>()
    private val leaveListeners = mutableMapOf<Widget, Boolean>()
    private val moveListeners = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<CursorMoveEvent> = CursorMoveEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> =
        arrayOf(MouseEnterEvent::class, MouseLeaveEvent::class, MouseMoveEvent::class)

    override fun eventFilter(event: CursorMoveEvent) = triggerEvent(event)

    override fun triggerEvent(event: CursorMoveEvent) {
        val (mouseX, mouseY) = event

        // 处理 MouseEnterEvent 和 MouseLeaveEvent
        handleMouseEnterAndLeave(mouseX, mouseY)

        // 处理 MouseMoveEvent
        val highestZWidget = getHighestZWidget(moveListeners, mouseX, mouseY, MouseMoveEvent::class)

        highestZWidget?.onMouseMove(MouseMoveEvent(mouseX, mouseY))
    }

    private fun handleMouseEnterAndLeave(mouseX: Double, mouseY: Double) {
        // 处理 MouseEnterEvent
        enterListeners
            .filter { it.key.isVisible }
            .forEach { (widget, hasEntered) ->
                val isInside = widget.containsPoint(mouseX, mouseY, event = MouseEnterEvent::class)
                if (isInside && !hasEntered) {
                    widget.onMouseEnter()
                    enterListeners[widget] = true
                } else if (!isInside && hasEntered) {
                    enterListeners[widget] = false
                }
            }

        // 处理 MouseLeaveEvent
        leaveListeners
            .filter { it.key.isVisible }
            .forEach { (widget, wasInside) ->
                val isVisible = widget.isVisible
                val isInside = widget.containsPoint(mouseX, mouseY, event = MouseLeaveEvent::class)
                if (isVisible && !isInside && wasInside) {
                    widget.onMouseLeave()
                    leaveListeners[widget] = false
                } else if (isInside && !wasInside) {
                    leaveListeners[widget] = true
                }
            }
    }

    override fun registerWidget(widget: Widget, event: KClass<out Event>) {
        when (event) {
            MouseEnterEvent::class -> registerEnterListener(widget)
            MouseLeaveEvent::class -> registerLeaveListener(widget)
            MouseMoveEvent::class -> registerMoveListener(widget)
        }
    }

    private fun registerEnterListener(widget: Widget) {
        // 查找 onMouseEnter 方法并判断注解是否存在
        val hasMouseEnterAnnotation = widget::class.memberFunctions
            .find { it.name == "onMouseEnter" }
            ?.annotations
            ?.any { it is MouseEnterEventHandler } != true

        if (hasMouseEnterAnnotation) {
            enterListeners[widget] = false
        }
    }

    private fun registerLeaveListener(widget: Widget) {
        // 查找 onMouseLeave 方法并判断注解是否存在
        val hasMouseLeaveAnnotation = widget::class.memberFunctions
            .find { it.name == "onMouseLeave" }
            ?.annotations
            ?.any { it is MouseLeaveEventHandler } != true

        if (hasMouseLeaveAnnotation) {
            leaveListeners[widget] = false
        }
    }

    private fun registerMoveListener(widget: Widget) {
        // 查找 onMouseMove 方法并判断注解是否存在
        val hasMouseMoveAnnotation = widget::class.memberFunctions
            .find { it.name == "onMouseMove" }
            ?.annotations
            ?.any { it is MouseMoveEventHandler } != true

        if (hasMouseMoveAnnotation) {
            moveListeners.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget, event: KClass<out Event>) {
        when (event) {
            MouseEnterEvent::class -> enterListeners.remove(widget)
            MouseLeaveEvent::class -> leaveListeners.remove(widget)
            MouseMoveEvent::class -> moveListeners.remove(widget)
        }
    }
}
