package com.midnightcrowing.scenes

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.Window
import com.midnightcrowing.gui.components.base.Screen
import com.midnightcrowing.gui.components.hotbar.HotBar
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum

class FarmScene(window: Window) : Screen(window) {
    private val bgRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.FARM_BACKGROUND.inputStream)

    private companion object {
        const val BASE_BG_WIDTH: Int = 5397
        const val BASE_BG_HEIGHT: Int = 3036
        const val BASE_LEFT_POINT_X: Int = 1364
        const val BASE_LEFT_POINT_Y: Int = 1564 - 37
        const val BASE_MIDDLE_POINT_X: Int = 3140
        const val BASE_MIDDLE_POINT_Y: Int = 2242 + 17
        const val BASE_RIGHT_POINT_X: Int = 4160
        const val BASE_RIGHT_POINT_Y: Int = 1612
        const val BASE_BLOCK_HEIGHT: Int = 285
        const val BASE_FARMLAND_BLACK_DEEP: Int = 17
        val FARMLAND_BOARD = listOf(
            0b1111111,
            0b1111111,
            0b0000000,
            0b1111111,
            0b1111110,
        )
    }

    private val blockHeight: Float get() = BASE_BLOCK_HEIGHT.toFloat() / BASE_BG_HEIGHT * window.height
    private val blockDeep: Float get() = BASE_FARMLAND_BLACK_DEEP.toFloat() / BASE_BG_HEIGHT * window.height
    private val leftPointX: Float get() = BASE_LEFT_POINT_X.toFloat() / BASE_BG_WIDTH * window.width
    private val leftPointY: Float get() = BASE_LEFT_POINT_Y.toFloat() / BASE_BG_HEIGHT * window.height
    private val middlePointX: Float get() = BASE_MIDDLE_POINT_X.toFloat() / BASE_BG_WIDTH * window.width
    private val middlePointY: Float get() = BASE_MIDDLE_POINT_Y.toFloat() / BASE_BG_HEIGHT * window.height
    private val rightPointX: Float get() = BASE_RIGHT_POINT_X.toFloat() / BASE_BG_WIDTH * window.width
    private val rightPointY: Float get() = BASE_RIGHT_POINT_Y.toFloat() / BASE_BG_HEIGHT * window.height

    private val farmArea: FarmArea = FarmArea(window, farmlandBoard = FARMLAND_BOARD)

    // UI
    private val hotBar: HotBar = HotBar(window)

    private val wheatRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.WHEAT.inputStream)
    private val wheatSeedRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.WHEAT_SEED.inputStream)
    private val cabbageRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.CABBAGE.inputStream)
    private val carrotRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.CARROT.inputStream)
    private val potatoRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.POTATO.inputStream)
    private val tomatoRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.TOMATO.inputStream)

    override fun render() {
        bgRenderer.render(-1f, 1f, 1f, -1f)
        hotBar.render()
        farmArea.render(
            blockHeight, blockDeep, leftPointX, leftPointY, middlePointX, middlePointY, rightPointX, rightPointY,
        )

        wheatSeedRenderer.render(hotBar.getGridBounds(1).toNdcBounds(window))
        wheatRenderer.render(hotBar.getGridBounds(5).toNdcBounds(window))
        cabbageRenderer.render(hotBar.getGridBounds(6).toNdcBounds(window))
        carrotRenderer.render(hotBar.getGridBounds(7).toNdcBounds(window))
        potatoRenderer.render(hotBar.getGridBounds(8).toNdcBounds(window))
        tomatoRenderer.render(hotBar.getGridBounds(9).toNdcBounds(window))
    }

    override fun cleanup() {
        bgRenderer.cleanup()
        hotBar.cleanup()
        farmArea.cleanup()

        wheatSeedRenderer.cleanup()
        wheatRenderer.cleanup()
        cabbageRenderer.cleanup()
        carrotRenderer.cleanup()
        potatoRenderer.cleanup()
        tomatoRenderer.cleanup()
    }
}