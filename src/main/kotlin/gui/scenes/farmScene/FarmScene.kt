package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.controllers.FarmController
import com.midnightcrowing.events.CustomEvent.KeyPressedEvent
import com.midnightcrowing.events.CustomEvent.MouseMoveEvent
import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.bases.ItemButton
import com.midnightcrowing.gui.bases.Screen
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.publics.*
import com.midnightcrowing.model.Point
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import org.lwjgl.glfw.GLFW.GLFW_KEY_E
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE

typealias FloatingWidget = List<Widget>

class FarmScene(window: Window) : Screen(window) {
    companion object {
        // Farm area
        const val FARM_BG_WIDTH: Double = 5397.0
        const val FARM_BG_HEIGHT: Double = 3036.0
        const val FARM_BLOCK_HEIGHT: Int = 285
        const val FARM_FARMLAND_BLACK_DEEP: Int = 17
        val FARM_LEFT_POINT: Point = Point(1364.0, (1564 - 37).toDouble())
        val FARM_MIDDLE_POINT: Point = Point(3140.0, (2242 + 17).toDouble())
        val FARM_RIGHT_POINT: Point = Point(4160.0, 1612.0)
        val FARMLAND_BOARD: List<Int> = listOf(
            0b1111111,
            0b1111111,
            0b0000000,
            0b1111111,
            0b1111110,
        )
    }

    override val bgRenderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.FARM_BACKGROUND.texture)

    // controller
    private val controller: FarmController = FarmController(this)

    // UI
    val escMenus: EscMenus = EscMenus(this, controller, z = 3)
    private val cropInfoDisplay: CropInfoDisplay = CropInfoDisplay(this, controller.cropInfo)
    private val farmArea: FarmArea = FarmArea(this, controller.farmArea, farmlandBoard = FARMLAND_BOARD)
    private val inventory: Inventory = Inventory(this, controller.inventory, z = 4)
    private val hotBar: HotBar = HotBar(this, controller.hotBar)
    private val toolTrade: ToolTrade = ToolTrade(this, controller.toolTrade, z = 4)
    private val trade: Trade = Trade(this, controller.trade, z = 4)
    private val compost: Compost = Compost(this, controller.compost, z = 4)
    private val anvilButton: ItemButton = ItemButton(
        this, "minecraft:anvil", "工具", tooltipPosition = "after-top"
    )
    private val villagerButton: ItemButton = ItemButton(
        this, "minecraft:villager_spawn_egg", "商店", tooltipPosition = "after-top"
    )
    private val composterButton: ItemButton = ItemButton(
        this, "minecraft:composter", "堆肥", tooltipPosition = "after-top"
    )
    private val chestButton: ItemButton = ItemButton(
        this, "minecraft:chest", "背包", tooltipPosition = "before-top"
    )

    private val floatingWidgets: FloatingWidget = listOf(
        hotBar, anvilButton, villagerButton, composterButton, chestButton
    )

    private fun FloatingWidget.setHidden(hidden: Boolean) = forEach { it.setHidden(hidden) }

    private var activeWidget: Widget? = null

    init {
        inventory.setHidden(true)
        toolTrade.setHidden(true)
        trade.setHidden(true)
        compost.setHidden(true)
        escMenus.setHidden(true)

        // Register button clock event callbacks
        anvilButton.onClickCallback = { showContainer(toolTrade) }
        villagerButton.onClickCallback = { showContainer(trade) }
        composterButton.onClickCallback = { showContainer(compost) }
        chestButton.onClickCallback = { showContainer(inventory) }
    }

    private fun showContainer(widget: Widget) {
        if (escMenus.isVisible) {
            return
        }

        if (activeWidget == null) {
            widget.setHidden(false)
            floatingWidgets.setHidden(true)
            controller.hotBar.update()
            activeWidget = widget
        } else if (activeWidget == widget) {
            widget.setHidden(true)
            floatingWidgets.setHidden(false)
            controller.hotBar.update()
            activeWidget = null
        }

        val (x, y) = window.getCursorPos()
        widget.onMouseMove(MouseMoveEvent(x, y))
    }

    private fun closeActiveWidget() {
        activeWidget?.let {
            it.setHidden(true)
            floatingWidgets.setHidden(false)
            controller.hotBar.update()
            activeWidget = null
        }
    }

    override fun place(w: Int, h: Int) {
        super.place(w, h)

        // hotBar
        hotBar.place(
            (w - HotBar.SCALED_WIDTH) / 2, h - HotBar.SCALED_HEIGHT,
            (w + HotBar.SCALED_WIDTH) / 2, h.toDouble()
        )

        // inventory
        inventory.place(
            (w - Inventory.SCALED_WIDTH) / 2,
            (h - Inventory.SCALED_HEIGHT) / 2 + Inventory.OFFSET_Y,
            (w + Inventory.SCALED_WIDTH) / 2,
            (h + Inventory.SCALED_HEIGHT) / 2 + Inventory.OFFSET_Y
        )

        // trades
        toolTrade.place(
            (w - Trade.SCALED_WIDTH) / 2,
            (h - Trade.SCALED_HEIGHT) / 2 + Trade.OFFSET_Y,
            (w + Trade.SCALED_WIDTH) / 2,
            (h + Trade.SCALED_HEIGHT) / 2 + Trade.OFFSET_Y
        )
        trade.place(
            (w - Trade.SCALED_WIDTH) / 2,
            (h - Trade.SCALED_HEIGHT) / 2 + Trade.OFFSET_Y,
            (w + Trade.SCALED_WIDTH) / 2,
            (h + Trade.SCALED_HEIGHT) / 2 + Trade.OFFSET_Y
        )

        // compost
        compost.place(
            (w - Compost.SCALED_WIDTH) / 2,
            (h - Compost.SCALED_HEIGHT) / 2 + Compost.OFFSET_Y,
            (w + Compost.SCALED_WIDTH) / 2,
            (h + Compost.SCALED_HEIGHT) / 2 + Compost.OFFSET_Y
        )

        // cropInfoDisplay
        cropInfoDisplay.place(
            x1 = w * 0.4, y1 = 0.0,
            x2 = w * 0.6, y2 = w / 16.0
        )

        // farmArea
        val blkDeep = FARM_FARMLAND_BLACK_DEEP / FARM_BG_HEIGHT * h
        val blkH = FARM_BLOCK_HEIGHT / FARM_BG_HEIGHT * h
        val lPt = Point(FARM_LEFT_POINT.x / FARM_BG_WIDTH * w, FARM_LEFT_POINT.y / FARM_BG_HEIGHT * h)
        val mPt = Point(FARM_MIDDLE_POINT.x / FARM_BG_WIDTH * w, FARM_MIDDLE_POINT.y / FARM_BG_HEIGHT * h)
        val rPt = Point(FARM_RIGHT_POINT.x / FARM_BG_WIDTH * w, FARM_RIGHT_POINT.y / FARM_BG_HEIGHT * h)
        farmArea.place(blkDeep, blkH, lPt, mPt, rPt)

        // Left Buttons
        anvilButton.place(
            x1 = 0.0, y1 = h - 70.0,
            x2 = 70.0, y2 = h.toDouble()
        )
        villagerButton.place(
            x1 = 70.0, y1 = h - 70.0,
            x2 = 140.0, y2 = h.toDouble()
        )
        composterButton.place(
            x1 = 140.0, y1 = h - 70.0,
            x2 = 210.0, y2 = h.toDouble()
        )

        // Right Buttons
        chestButton.place(
            x1 = w - 70.0, y1 = h - 70.0,
            x2 = w.toDouble(), y2 = h.toDouble()
        )

        // escMenus
        escMenus.place(w, h)
    }

    override fun update() {
        controller.update()
        farmArea.update()
        inventory.update()
        toolTrade.update()
        trade.update()
        compost.update()
        anvilButton.update()
        villagerButton.update()
        composterButton.update()
        chestButton.update()
        escMenus.update()
    }

    override fun onKeyPress(e: KeyPressedEvent): Boolean {
        when (e.key) {
            GLFW_KEY_ESCAPE -> if (activeWidget != null) closeActiveWidget() else escMenus.toggleVisible()
            GLFW_KEY_E -> if (activeWidget != null) closeActiveWidget() else showContainer(inventory)
        }
        return true
    }

    override fun doRender() {
        farmArea.render()
        cropInfoDisplay.render()
        hotBar.render()
        inventory.render()
        toolTrade.render()
        trade.render()
        compost.render()

        // Buttons
        anvilButton.render()
        villagerButton.render()
        composterButton.render()
        chestButton.render()
        anvilButton.renderItemName()
        villagerButton.renderItemName()
        composterButton.renderItemName()
        chestButton.renderItemName()

        escMenus.render()
    }

    override fun doCleanup() {
        bgRenderer.cleanup()
        farmArea.cleanup()
        cropInfoDisplay.cleanup()
        hotBar.cleanup()
        inventory.cleanup()
        toolTrade.cleanup()
        trade.cleanup()
        compost.cleanup()
        anvilButton.cleanup()
        villagerButton.cleanup()
        composterButton.cleanup()
        chestButton.cleanup()
        escMenus.cleanup()
    }
}