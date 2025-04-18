package com.midnightcrowing.gui.bases.button

import com.midnightcrowing.events.CustomEvent.MouseMoveEvent
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.renderer.ItemRenderer

class ItemButton(
    parent: Widget,
    itemId: String,
    itemName: String? = null,
    private val tooltipPosition: String = "above",
) : Button(parent) {
    private val padding: Double = 6.0
    private val itemRender: ItemRenderer? = ItemRegistry.createItemRender(itemId, this)
    private var mousePos: Point = Point.EMPTY

    init {
        if (itemName != null) {
            itemRender?.tooltipRenderer?.text = itemName
        }
    }

    override fun setHidden(value: Boolean) {
        super.setHidden(value)
        mousePos = Point.EMPTY
    }

    override fun onMouseMove(e: MouseMoveEvent) {
        super.onMouseMove(e)
        mousePos.x = e.x
        mousePos.y = e.y
    }

    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        super.place(x1, y1, x2, y2)
        itemRender?.place(x1 + padding, y1 + padding, x2 - padding, y2 - padding)
    }

    override fun doRender() {
        super.doRender()
        itemRender?.render()
    }

    fun renderItemName() {
        if (isVisible && isHover) {
            itemRender?.renderTooltip(mousePos.x, mousePos.y, position = tooltipPosition)
        }
    }
}