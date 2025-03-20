package com.midnightcrowing.scenes

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.HotBar
import com.midnightcrowing.gui.HotBar.Companion.SCALED_HEIGHT
import com.midnightcrowing.gui.HotBar.Companion.SCALED_WIDTH
import com.midnightcrowing.gui.ItemCheckBox
import com.midnightcrowing.gui.base.Screen
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.model.Point
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum

class FarmScene(window: Window) : Screen(window) {
    private companion object {
        // Farm area
        const val BASE_BG_WIDTH: Float = 5397f
        const val BASE_BG_HEIGHT: Float = 3036f
        const val BASE_BLOCK_HEIGHT: Int = 285
        const val BASE_FARMLAND_BLACK_DEEP: Int = 17
        val BASE_LEFT_POINT: Point = Point(1364f, (1564 - 37).toFloat())
        val BASE_MIDDLE_POINT: Point = Point(3140f, (2242 + 17).toFloat())
        val BASE_RIGHT_POINT: Point = Point(4160f, 1612f)
        val FARMLAND_BOARD = listOf(
            0b1111111,
            0b1111111,
            0b0000000,
            0b1111111,
            0b1111110,
        )
    }

    override val bgRenderer: ImageRenderer = createImageRenderer(ResourcesEnum.FARM_BACKGROUND.inputStream)

    // UI
    val farmArea: FarmArea = FarmArea(window, farmlandBoard = FARMLAND_BOARD)
    val hotBar: HotBar = HotBar(this)
    val itemCheckBox: ItemCheckBox = ItemCheckBox(this)

    override fun place() {
        val hotBarPoint1 = Point((window.width - SCALED_WIDTH) / 2, window.height - SCALED_HEIGHT)
        val hotBarPoint2 = Point(hotBarPoint1.x + SCALED_WIDTH, window.height.toFloat())
        hotBar.place(hotBarPoint1.x, hotBarPoint1.y, hotBarPoint2.x, hotBarPoint2.y)

        val blockDeep: Float = BASE_FARMLAND_BLACK_DEEP / BASE_BG_HEIGHT * window.height
        val blockHeight: Float = BASE_BLOCK_HEIGHT / BASE_BG_HEIGHT * window.height
        val leftPoint = Point(
            BASE_LEFT_POINT.x / BASE_BG_WIDTH * window.width,
            BASE_LEFT_POINT.y / BASE_BG_HEIGHT * window.height
        )
        val middlePoint = Point(
            BASE_MIDDLE_POINT.x / BASE_BG_WIDTH * window.width,
            BASE_MIDDLE_POINT.y / BASE_BG_HEIGHT * window.height
        )
        val rightPoint = Point(
            BASE_RIGHT_POINT.x / BASE_BG_WIDTH * window.width,
            BASE_RIGHT_POINT.y / BASE_BG_HEIGHT * window.height
        )
        farmArea.place(blockDeep, blockHeight, leftPoint, middlePoint, rightPoint)
    }

    override fun update() {
        farmArea.update()
    }

    override fun render() {
        super.render()

        hotBar.render()
        itemCheckBox.render()
        farmArea.render()
    }

    override fun cleanup() {
        bgRenderer.cleanup()
        hotBar.cleanup()
        itemCheckBox.cleanup()
        farmArea.cleanup()
    }
}