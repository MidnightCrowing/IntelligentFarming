package com.midnightcrowing.gui.publics.toolTrade

import com.midnightcrowing.gui.bases.Screen
import com.midnightcrowing.gui.publics.trade.Trade
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.resource.ResourceType

class ToolTrade(
    parent: Screen,
    controller: ToolTradeController,
    z: Int? = null,
) : Trade(parent, controller, z) {
    override val titleTextRenderer: TextRenderer = TextRenderer.createTextRendererForGUI(window.nvg)
        .apply { text = "工具商" }

    private var tradeEmptySlot0: TextureRenderer = TextureRenderer(
        ResourceLocation(ResourceType.TE_ITEM, "minecraft", "empty_slot_hoe.png")
    )
    private var tradeEmptySlot1: TextureRenderer = TextureRenderer(
        ResourceLocation(ResourceType.TE_ITEM, "minecraft", "empty_slot_emerald.png")
    )

    override fun doRender() {
        if (tradeSlot0Item.isEmpty()) {
            calculateTradeBounds(0)?.let { tradeEmptySlot0.render(it) }
        }
        if (tradeSlot1Item.isEmpty()) {
            calculateTradeBounds(1)?.let { tradeEmptySlot1.render(it) }
        }
        super.doRender()
    }
}