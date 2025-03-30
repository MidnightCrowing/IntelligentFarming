package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.Event
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.gui.base.Window
import kotlin.reflect.KClass

abstract class BaseMouseListener<T : Event>(
    val window: Window,
    eventManager: EventManager,
) : BaseEventListener<T>(eventManager) {
    protected fun getHighestZWidget(widgets: List<Widget>, x: Double, y: Double, event: KClass<out Event>): Widget? {
        return widgets
            .filter { it.isVisible }
            .filter { it.containsPoint(x, y, event = event) }
            .maxByOrNull { it.z }
    }
}