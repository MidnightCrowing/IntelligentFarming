package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.Event
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.components.base.Widget

// T 是接收事件类型
abstract class EventListener<T : Event>(eventManager: EventManager) {
    init {
        eventManager.registerListener(this)
    }

    // 返回接收类型的 Class
    abstract fun getReceiveEventType(): Class<T>

    // 返回发送类型的 Class
    abstract fun getSendEventType(): Array<Class<out Event>>

    // 事件过滤
    abstract fun eventFilter(event: T)

    // 激活事件
    abstract fun triggerEvent(event: T)

    // 注册组件
    abstract fun registerWidget(widget: Widget)

    // 取消注册组件
    abstract fun unregisterWidget(widget: Widget)
}
