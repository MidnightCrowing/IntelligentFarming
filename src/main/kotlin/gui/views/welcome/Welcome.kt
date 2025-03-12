package com.midnightcrowing.gui.views.welcome;

import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.AbstractWidget
import com.midnightcrowing.gui.components.base.Button
import com.midnightcrowing.gui.components.hotbar.HotBar
import com.midnightcrowing.gui.components.inventory.Inventory
import com.midnightcrowing.render.Renderer
import com.midnightcrowing.render.createRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.utils.CoordinateConversionUtils.convertNdcToScreenBounds
import com.midnightcrowing.utils.CoordinateConversionUtils.convertScreenToNdcBounds


class Welcome(window: Window) : AbstractWidget(window) {
    private val bgRenderer: Renderer = createRenderer(ResourcesEnum.WELCOME_BACKGROUND.path)
    private val hotBar: HotBar = HotBar(window)
    private val inventory: Inventory = Inventory(window)
    private val button: Button = Button(window)

    private val wheatRenderer: Renderer = createRenderer(ResourcesEnum.WHEAT.path)
    private val cabbageRenderer: Renderer = createRenderer(ResourcesEnum.CABBAGE.path)
    private val carrotRenderer: Renderer = createRenderer(ResourcesEnum.CARROT.path)
    private val potatoRenderer: Renderer = createRenderer(ResourcesEnum.POTATO.path)
    private val tomatoRenderer: Renderer = createRenderer(ResourcesEnum.TOMATO.path)

    override fun render() {
        bgRenderer.render(-1f, 1f, 1f, -1f)
        hotBar.render()
//        inventory.render()
        button.render(convertNdcToScreenBounds(window, -0.25f, 0.05f, 0.25f, -0.05f))

        wheatRenderer.render(convertScreenToNdcBounds(window, hotBar.getGridBounds(5)))
        cabbageRenderer.render(convertScreenToNdcBounds(window, hotBar.getGridBounds(6)))
        carrotRenderer.render(convertScreenToNdcBounds(window, hotBar.getGridBounds(7)))
        potatoRenderer.render(convertScreenToNdcBounds(window, hotBar.getGridBounds(8)))
        tomatoRenderer.render(convertScreenToNdcBounds(window, hotBar.getGridBounds(9)))
    }

    override fun cleanup() {
        bgRenderer.cleanup()
        hotBar.cleanup()
        inventory.cleanup()
        button.cleanup()

        wheatRenderer.cleanup()
        cabbageRenderer.cleanup()
        carrotRenderer.cleanup()
        potatoRenderer.cleanup()
        tomatoRenderer.cleanup()
    }
}
