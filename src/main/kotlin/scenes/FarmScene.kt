package com.midnightcrowing.scenes

import com.midnightcrowing.controllers.GameController
import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.CropInfoDisplay
import com.midnightcrowing.gui.HotBar
import com.midnightcrowing.gui.Inventory
import com.midnightcrowing.gui.base.Screen
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.model.Point
import com.midnightcrowing.render.TextureRenderer
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
    val gameController: GameController = GameController()

    // UI
    val cropInfoDisplay: CropInfoDisplay = CropInfoDisplay(this, gameController.cropInfo)
    val farmArea: FarmArea = FarmArea(window, gameController.farmArea, farmlandBoard = FARMLAND_BOARD)
    val inventory: Inventory = Inventory(this, gameController.inventory)
    val hotBar: HotBar = HotBar(this, gameController.hotBar)

    init {
        inventory.setHidden(true)
    }

    override fun place(w: Int, h: Int) {
        // hotBar
        hotBar.place(
            (w - HotBar.SCALED_WIDTH) / 2, h - HotBar.SCALED_HEIGHT,
            (w + HotBar.SCALED_WIDTH) / 2, h.toDouble()
        )

        // inventory
        inventory.place(
            (w - Inventory.SCALED_WIDTH) / 2, (h - Inventory.SCALED_HEIGHT) / 2 + Inventory.OFFSET_Y,
            (w + Inventory.SCALED_WIDTH) / 2, (h + Inventory.SCALED_HEIGHT) / 2 + Inventory.OFFSET_Y
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
    }

    override fun update() {
        gameController.update()
        farmArea.update()
        inventory.update()
    }

    override fun doRender() {
        farmArea.render()
        cropInfoDisplay.render()
        hotBar.render()
        inventory.render()
    }

    override fun cleanup() {
        super.cleanup()
        bgRenderer.texture?.cleanup()
        bgRenderer.cleanup()
        farmArea.cleanup()
        cropInfoDisplay.cleanup()
        hotBar.cleanup()
        inventory.cleanup()
    }
}