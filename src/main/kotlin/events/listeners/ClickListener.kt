package com.midnightcrowing.events.listeners

import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.MouseButtonEvent
import com.midnightcrowing.events.EventManager
import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Widget
import com.midnightcrowing.utils.WindowUtils.convertNdcToScreen
import org.lwjgl.glfw.GLFW.*
import kotlin.reflect.full.declaredFunctions

/**
 * 鼠标点击事件，包含点击的具体位置
 * @param x 点击的 X 坐标
 * @param y 点击的 Y 坐标
 */
data class ClickEvent(val x: Float, val y: Float) : Event()


class ClickListener(
    val window: Window,
    eventManager: EventManager,
) : EventListener<ClickEvent, MouseButtonEvent>(eventManager) {
    private val clickableWidgets = mutableListOf<Widget>()

    override fun getReceiveEventType(): Class<MouseButtonEvent> = MouseButtonEvent::class.java

    override fun getSendEventType(): Class<ClickEvent> = ClickEvent::class.java

    override fun eventFilter(event: MouseButtonEvent) {
        val button = event.button
        val action = event.action

        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            val xPos = DoubleArray(1)
            val yPos = DoubleArray(1)
            glfwGetCursorPos(window.handle, xPos, yPos)
            triggerEvent(ClickEvent(xPos[0].toFloat(), yPos[0].toFloat()))
        }
    }

    override fun triggerEvent(event: ClickEvent) {
        clickableWidgets.forEach { widget ->
            val (xCheck, yCheck) = convertNdcToScreen(window, widget.left, widget.top)
            val (xCheckRight, yCheckBottom) = convertNdcToScreen(window, widget.right, widget.bottom)

            if (event.x in xCheck..xCheckRight && event.y in yCheck..yCheckBottom) {
                widget.onClick(event)
            }
        }
    }

    override fun registerWidget(widget: Widget) {
        // 使用反射检查 widget 是否覆盖了 onClick 方法
        val onClickMethod = widget::class.declaredFunctions.find { it.name == "onClick" }

        // 判断该方法是否被实现（覆盖了父类的实现）
        if (onClickMethod != null && !onClickMethod.isAbstract) {
            clickableWidgets.add(widget)
        }
    }
}