package com.midnightcrowing.gui.views.welcome;

import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.AbstractWidget
import com.midnightcrowing.gui.components.hotbar.HotBar
import com.midnightcrowing.gui.components.inventory.Inventory
import com.midnightcrowing.render.Renderer
import com.midnightcrowing.render.Texture
import com.midnightcrowing.resource.ResourcesEnum


class Welcome(window: Window) : AbstractWidget(window) {
    private val bgRenderer: Renderer
    private val hotBar: HotBar
    private val inventory: Inventory

    private val wheatRenderer: Renderer
    private val cabbageRenderer: Renderer
    private val carrotRenderer: Renderer
    private val potatoRenderer: Renderer
    private val tomatoRenderer: Renderer

    init {
        val bgTexture = Texture(ResourcesEnum.WELCOME_BACKGROUND.path).apply { load() }
        bgRenderer = Renderer(bgTexture)
        hotBar = HotBar(window)
        inventory = Inventory(window)

        val cabbage = Texture(ResourcesEnum.CABBAGE.path).apply { load() }
        cabbageRenderer = Renderer(cabbage)
        val carrot = Texture(ResourcesEnum.CARROT.path).apply { load() }
        carrotRenderer = Renderer(carrot)
        val potato = Texture(ResourcesEnum.POTATO.path).apply { load() }
        potatoRenderer = Renderer(potato)
        val tomato = Texture(ResourcesEnum.TOMATO.path).apply { load() }
        tomatoRenderer = Renderer(tomato)
        val wheat = Texture(ResourcesEnum.WHEAT.path).apply { load() }
        wheatRenderer = Renderer(wheat)

    }

    override fun render() {
        bgRenderer.render(-1f, 1f, 1f, -1f)
        hotBar.render()
        inventory.render()

        val wheatGridPosition = hotBar.getGridBounds(1)
        wheatRenderer.render(
            wheatGridPosition.left,
            wheatGridPosition.top,
            wheatGridPosition.right,
            wheatGridPosition.bottom
        )
        val cabbageGridPosition = hotBar.getGridBounds(2)
        cabbageRenderer.render(
            cabbageGridPosition.left,
            cabbageGridPosition.top,
            cabbageGridPosition.right,
            cabbageGridPosition.bottom
        )
        val carrotGridPosition = hotBar.getGridBounds(3)
        carrotRenderer.render(
            carrotGridPosition.left,
            carrotGridPosition.top,
            carrotGridPosition.right,
            carrotGridPosition.bottom
        )
        val potatoGridPosition = hotBar.getGridBounds(4)
        potatoRenderer.render(
            potatoGridPosition.left,
            potatoGridPosition.top,
            potatoGridPosition.right,
            potatoGridPosition.bottom
        )
        val tomatoGridPosition = hotBar.getGridBounds(5)
        tomatoRenderer.render(
            tomatoGridPosition.left,
            tomatoGridPosition.top,
            tomatoGridPosition.right,
            tomatoGridPosition.bottom
        )


    }

    override fun cleanup() {
        bgRenderer.cleanup()
        hotBar.cleanup()
        inventory.cleanup()

        wheatRenderer.cleanup()
        cabbageRenderer.cleanup()
        carrotRenderer.cleanup()
        potatoRenderer.cleanup()
        tomatoRenderer.cleanup()
    }
}
