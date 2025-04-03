package com.midnightcrowing.gui.publics

import com.midnightcrowing.events.CustomEvent.MouseMoveEvent
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack

class DraggingItem(parent: Widget) : Widget(parent) {
    // 物品
    var item: ItemStack = ItemStack.EMPTY
        set(value) {
            field = value
            itemWidget = ItemRegistry.createItem(value.id, this)
        }
    private var itemWidget: FarmItems? = null

    var width: Double = 65.0
    var height: Double = 65.0

    fun onParentMouseMove(e: MouseMoveEvent) {
        val bounds = ScreenBounds(e.x - width / 2, e.y - height / 2, e.x + width / 2, e.y + height / 2)
        this.place(bounds)
        itemWidget?.place(bounds)
    }

    fun onParentMouseMove(pos: Pair<Double, Double>) = onParentMouseMove(MouseMoveEvent(pos.first, pos.second))

    override fun render() {
        if (!isVisible || item.isEmpty()) {
            return
        }
        itemWidget?.render(item.count)
    }
}