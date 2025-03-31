package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.controllers.FarmController
import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.bases.Screen
import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.publics.CropInfoDisplay
import com.midnightcrowing.model.Point
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum

class FarmScene(window: Window) : Screen(window) {
    private companion object {
        // Farm area
        const val BASE_BG_WIDTH: Double = 5397.0
        const val BASE_BG_HEIGHT: Double = 3036.0
        const val BASE_BLOCK_HEIGHT: Int = 285
        const val BASE_FARMLAND_BLACK_DEEP: Int = 17
        val BASE_LEFT_POINT: Point = Point(1364.0, (1564 - 37).toDouble())
        val BASE_MIDDLE_POINT: Point = Point(3140.0, (2242 + 17).toDouble())
        val BASE_RIGHT_POINT: Point = Point(4160.0, 1612.0)
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
    val inventory: Inventory = Inventory(this, controller.inventory)
    val hotBar: HotBar = HotBar(this, controller.hotBar)
    val escMenus: EscMenus = EscMenus(this, controller)

    init {
        inventory.setHidden(true)
        escMenus.setHidden(true)
    }

    override fun place(w: Int, h: Int) {
        // hotBar
        hotBar.place(
            (w - HotBar.Companion.SCALED_WIDTH) / 2, h - HotBar.Companion.SCALED_HEIGHT,
            (w + HotBar.Companion.SCALED_WIDTH) / 2, h.toDouble()
        )

        // inventory
        inventory.place(
            (w - Inventory.Companion.SCALED_WIDTH) / 2,
            (h - Inventory.Companion.SCALED_HEIGHT) / 2 + Inventory.Companion.OFFSET_Y,
            (w + Inventory.Companion.SCALED_WIDTH) / 2,
            (h + Inventory.Companion.SCALED_HEIGHT) / 2 + Inventory.Companion.OFFSET_Y
        )

        // cropInfoDisplay
        cropInfoDisplay.place(
            x1 = w * 0.4, y1 = 0.0,
            x2 = w * 0.6, y2 = w / 16.0
        )

        // farmArea
        val blkDeep = BASE_FARMLAND_BLACK_DEEP / BASE_BG_HEIGHT * h
        val blkH = BASE_BLOCK_HEIGHT / BASE_BG_HEIGHT * h
        val lPt = Point(BASE_LEFT_POINT.x / BASE_BG_WIDTH * w, BASE_LEFT_POINT.y / BASE_BG_HEIGHT * h)
        val mPt = Point(BASE_MIDDLE_POINT.x / BASE_BG_WIDTH * w, BASE_MIDDLE_POINT.y / BASE_BG_HEIGHT * h)
        val rPt = Point(BASE_RIGHT_POINT.x / BASE_BG_WIDTH * w, BASE_RIGHT_POINT.y / BASE_BG_HEIGHT * h)
        farmArea.place(blkDeep, blkH, lPt, mPt, rPt)

        // escMenus
        escMenus.place(w, h)
    }

    override fun update() {
        controller.update()
        farmArea.update()
        inventory.update()
    }

    override fun doRender() {
        farmArea.render()
        cropInfoDisplay.render()
        hotBar.render()
        inventory.render()
        escMenus.render()
    }

    override fun cleanup() {
        super.cleanup()
        bgRenderer.cleanup()
        farmArea.cleanup()
        cropInfoDisplay.cleanup()
        hotBar.cleanup()
        inventory.cleanup()
        escMenus.cleanup()
    }
}