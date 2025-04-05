package com.midnightcrowing.gui.publics

import com.midnightcrowing.controllers.ToolTradeController
import com.midnightcrowing.gui.bases.Screen
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum

class ToolTrade(
    parent: Screen,
    controller: ToolTradeController,
    z: Int? = null,
) : Trade(parent, controller, z) {
    override val titleTextRenderer: TextRenderer = TextRenderer.createTextRendererForGUI(window.nvg)
        .apply { text = "工具商" }

    private var tradeEmptySlot0: TextureRenderer = TextureRenderer(TextureResourcesEnum.EMPTY_SLOT_HOE.texture)
    private var tradeEmptySlot1: TextureRenderer = TextureRenderer(TextureResourcesEnum.EMPTY_SLOT_EMERALD.texture)

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