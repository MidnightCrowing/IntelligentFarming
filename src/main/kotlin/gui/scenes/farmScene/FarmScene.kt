package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.controllers.FarmController
import com.midnightcrowing.events.CustomEvent.KeyPressedEvent
import com.midnightcrowing.events.CustomEvent.MouseMoveEvent
import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.bases.Screen
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.publics.CropInfoDisplay
import com.midnightcrowing.model.Point
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import org.lwjgl.glfw.GLFW.*

class FarmScene(window: Window) : Screen(window) {
    private companion object {
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
    val controller: FarmController = FarmController(this)

    // UI
    val cropInfoDisplay: CropInfoDisplay = CropInfoDisplay(this, controller.cropInfo)
    val farmArea: FarmArea = FarmArea(this, controller.farmArea, farmlandBoard = FARMLAND_BOARD)
    val inventory: Inventory = Inventory(this, controller.inventory, z = 4)
    val hotBar: HotBar = HotBar(this, controller.hotBar)
    val trade: Trade = Trade(this, controller.trade, z = 4)
    val escMenus: EscMenus = EscMenus(this, controller, z = 3)

    var activeWidget: Widget? = null

    init {
        inventory.setHidden(true)
        trade.setHidden(true)
        escMenus.setHidden(true)
    }

    override fun place(w: Int, h: Int) {
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

        // trade
        trade.place(
            (w - Trade.SCALED_WIDTH) / 2,
            (h - Trade.SCALED_HEIGHT) / 2 + Trade.OFFSET_Y,
            (w + Trade.SCALED_WIDTH) / 2,
            (h + Trade.SCALED_HEIGHT) / 2 + Trade.OFFSET_Y
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

        // escMenus
        escMenus.place(w, h)
    }

    override fun update() {
        controller.update()
        farmArea.update()
        inventory.update()
        trade.update()
    }

    override fun onKeyPress(e: KeyPressedEvent): Boolean {
        // TODO
        when (e.key) {
            GLFW_KEY_ESCAPE -> {
                if (activeWidget != null) {
                    activeWidget!!.setHidden(true)
                    hotBar.setVisible(true)
                    controller.hotBar.update()
                    activeWidget = null
                } else {
                    escMenus.toggleVisible()
                }
            }

            GLFW_KEY_E -> {
                if (!escMenus.isVisible) {

                    if (activeWidget == null) {
                        inventory.setVisible(true)
                        hotBar.setHidden(true)
                        controller.hotBar.update()
                        activeWidget = inventory
                    } else if (activeWidget == inventory) {
                        inventory.setHidden(true)
                        hotBar.setVisible(true)
                        controller.hotBar.update()
                        activeWidget = null
                    }

                    val (x, y) = window.getCursorPos()
                    inventory.onMouseMove(MouseMoveEvent(x, y))
                }
            }

            GLFW_KEY_A -> {
                if (!escMenus.isVisible) {

                    if (activeWidget == null) {
                        trade.setVisible(true)
                        hotBar.setHidden(true)
                        controller.hotBar.update()
                        activeWidget = trade
                    } else if (activeWidget == trade) {
                        trade.setHidden(true)
                        hotBar.setVisible(true)
                        controller.hotBar.update()
                        activeWidget = null
                    }

                    val (x, y) = window.getCursorPos()
                    trade.onMouseMove(MouseMoveEvent(x, y))
                }
            }
        }
        return true
    }

    override fun doRender() {
        farmArea.render()
        cropInfoDisplay.render()
        hotBar.render()
        inventory.render()
        trade.render()
        escMenus.render()
    }

    override fun doCleanup() {
        bgRenderer.cleanup()
        farmArea.cleanup()
        cropInfoDisplay.cleanup()
        hotBar.cleanup()
        inventory.cleanup()
        trade.cleanup()
        escMenus.cleanup()
    }
}