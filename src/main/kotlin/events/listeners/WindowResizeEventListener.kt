package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.WindowResizeEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions

class WindowResizeEventListener(
    val window: Window,
    eventManager: EventManager,
) : BaseEventListener<WindowResizeEvent>(eventManager) {
    private val registerWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): KClass<WindowResizeEvent> = WindowResizeEvent::class

    override fun getSendEventType(): Array<KClass<out Event>> = arrayOf(WindowResizeEvent::class)

    override fun eventFilter(event: WindowResizeEvent) = triggerEvent(event)

    override fun triggerEvent(event: WindowResizeEvent) {
        // 调用 Window 和 screen 的回调方法
        window.handleResize(event.width, event.height)
        window.screen.place(event.width, event.height)

        // 触发事件
        val widgetsCopy = registerWidgets.toList()
        widgetsCopy.forEach { widget ->
            widget.onWindowResize(WindowResizeEvent(event.width, event.height))
        }
    }

    override fun registerWidget(widget: Widget) {
        // 使用反射检查 widget 是否覆盖了 onWindowResize 方法
        val onResizeMethod = widget::class.declaredFunctions.find { it.name == "onWindowResize" }

        // 判断该方法是否被实现（覆盖了父类的实现）
        if (onResizeMethod != null && !onResizeMethod.isAbstract) {
            registerWidgets.add(widget)
        }
    }

    override fun unregisterWidget(widget: Widget) {
        registerWidgets.remove(widget)
    }
}