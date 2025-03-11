package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.Event
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.components.base.Widget

// T 是发送事件类型, E 是接收事件类型
abstract class EventListener<T : Event, E : Event>(val eventManager: EventManager) {
    init {
        eventManager.registerListener(this)
    }

    // 返回接收类型的 Class
    abstract fun getReceiveEventType(): Class<E>

    // 返回发送类型的 Class
    abstract fun getSendEventType(): Class<T>

    // 事件过滤
    abstract fun eventFilter(event: E)

    // 激活事件
    abstract fun triggerEvent(event: T)

    // 注册组件
    abstract fun registerWidget(widget: Widget)
}
