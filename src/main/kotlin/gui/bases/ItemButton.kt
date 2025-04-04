package com.midnightcrowing.gui.bases

import com.midnightcrowing.events.CustomEvent.MouseMoveEvent
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.item.Item
import com.midnightcrowing.model.item.ItemRegistry

class ItemButton(
    parent: Widget,
    itemId: String,
    private val itemName: String? = null,
    private val tooltipPosition: String = "above",
) : Button(parent) {
    private val padding: Double = 9.0
    private val item: Item? = ItemRegistry.createItem(itemId, this)
    private var mousePos: Point = Point.EMPTY

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
        item?.place(x1 + padding, y1 + padding, x2 - padding, y2 - padding)
    }

    override fun doRender() {
        super.doRender()
        item?.render()
    }

    fun renderItemName() {
        if (isVisible && isHover) {
            item?.renderTooltip(mousePos.x, mousePos.y, position = tooltipPosition, itemName = this.itemName)
        }
    }

    override fun doCleanup() {
        super.doCleanup()
        item?.cleanup()
    }
}