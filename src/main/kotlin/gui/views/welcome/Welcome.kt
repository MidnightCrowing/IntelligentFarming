package views.welcome;

import org.intelligentfarming.gui.Window
import org.intelligentfarming.gui.components.hotbar.HotBar
import org.intelligentfarming.gui.components.inventory.Inventory
import org.intelligentfarming.render.RenderableBase
import org.intelligentfarming.render.Renderer
import org.intelligentfarming.render.Texture
import org.intelligentfarming.resource.Resources


class Welcome(window: Window) : RenderableBase(window) {
    private val bgRenderer: Renderer
    private val hotBar: HotBar
    private val inventory: Inventory

    private val wheatRenderer: Renderer
    private val cabbageRenderer: Renderer
    private val carrotRenderer: Renderer
    private val potatoRenderer: Renderer
    private val tomatoRenderer: Renderer

    private val checkBoxRenderer: Renderer

    init {
        val bgTexture = Texture(Resources.WELCOME_BACKGROUND.path).apply { load() }
        bgRenderer = Renderer(bgTexture)
        hotBar = HotBar(window)
        inventory = Inventory(window)

        val cabbage = Texture(Resources.CABBAGE.path).apply { load() }
        cabbageRenderer = Renderer(cabbage)
        val carrot = Texture(Resources.CARROT.path).apply { load() }
        carrotRenderer = Renderer(carrot)
        val potato = Texture(Resources.POTATO.path).apply { load() }
        potatoRenderer = Renderer(potato)
        val tomato = Texture(Resources.TOMATO.path).apply { load() }
        tomatoRenderer = Renderer(tomato)
        val wheat = Texture(Resources.WHEAT.path).apply { load() }
        wheatRenderer = Renderer(wheat)

        val checkBox = Texture(Resources.CHECK_BOX.path).apply { load() }
        checkBoxRenderer = Renderer(checkBox)
    }

    override fun render() {
        bgRenderer.render(-1f, 1f, 1f, -1f)
        hotBar.render()
        inventory.render()

        val wheatGridPosition = hotBar.getGridPosition(1)
        wheatRenderer.render(
            wheatGridPosition.left,
            wheatGridPosition.top,
            wheatGridPosition.right,
            wheatGridPosition.bottom
        )
        val cabbageGridPosition = hotBar.getGridPosition(2)
        cabbageRenderer.render(
            cabbageGridPosition.left,
            cabbageGridPosition.top,
            cabbageGridPosition.right,
            cabbageGridPosition.bottom
        )
        val carrotGridPosition = hotBar.getGridPosition(3)
        carrotRenderer.render(
            carrotGridPosition.left,
            carrotGridPosition.top,
            carrotGridPosition.right,
            carrotGridPosition.bottom
        )
        val potatoGridPosition = hotBar.getGridPosition(4)
        potatoRenderer.render(
            potatoGridPosition.left,
            potatoGridPosition.top,
            potatoGridPosition.right,
            potatoGridPosition.bottom
        )
        val tomatoGridPosition = hotBar.getGridPosition(5)
        tomatoRenderer.render(
            tomatoGridPosition.left,
            tomatoGridPosition.top,
            tomatoGridPosition.right,
            tomatoGridPosition.bottom
        )

        val checkBoxGridPosition = hotBar.getGridBoxPosition(1)
        checkBoxRenderer.render(
            checkBoxGridPosition.left,
            checkBoxGridPosition.top,
            checkBoxGridPosition.right,
            checkBoxGridPosition.bottom
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

        checkBoxRenderer.cleanup()
    }
}
