package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.gui.bases.Button
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum

class VillagerButton(parent: Widget) : Button(parent) {
    private val villagerRenderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.VILLAGER_SPAWN_EGG.texture)
    private var villagerBounds = ScreenBounds.EMPTY

    override fun place(bounds: ScreenBounds) {
        super.place(bounds)
        villagerBounds = ScreenBounds(
            x1 = bounds.x1 + 6,
            y1 = bounds.y1 + 6,
            x2 = bounds.x2 - 6,
            y2 = bounds.y2 - 6
        )
    }

    override fun doRender() {
        super.doRender()
        villagerRenderer.render(villagerBounds)
    }

    override fun doCleanup() {
        super.doCleanup()
        villagerRenderer.cleanup()
    }
}