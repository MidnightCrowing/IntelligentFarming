package com.midnightcrowing.scenes

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.gui.HotBar
import com.midnightcrowing.gui.Inventory
import com.midnightcrowing.gui.base.Screen
import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.model.Point
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.gui.HotBar.Companion.SCALED_HEIGHT as HotBar_SCALED_HEIGHT
import com.midnightcrowing.gui.HotBar.Companion.SCALED_WIDTH as HotBar_SCALED_WIDTH
import com.midnightcrowing.gui.Inventory.Companion.OFFSET_Y as Inventory_OFFSET_Y
import com.midnightcrowing.gui.Inventory.Companion.SCALED_HEIGHT as Inventory_SCALED_HEIGHT
import com.midnightcrowing.gui.Inventory.Companion.SCALED_WIDTH as Inventory_SCALED_WIDTH

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
    val inventory: Inventory = Inventory(this)

    override fun place() {
        // hotBar
        val hotBarPoint1 = Point(
            (window.width - HotBar_SCALED_WIDTH) / 2,
            window.height - HotBar_SCALED_HEIGHT
        )
        val hotBarPoint2 = Point(
            hotBarPoint1.x + HotBar_SCALED_WIDTH,
            window.height.toDouble()
        )
        hotBar.place(hotBarPoint1.x, hotBarPoint1.y, hotBarPoint2.x, hotBarPoint2.y)

        // inventory
        val inventoryPoint1 = Point(
            (window.width - Inventory_SCALED_WIDTH) / 2,
            (window.height - Inventory_SCALED_HEIGHT) / 2 + Inventory_OFFSET_Y
        )
        val inventoryPoint2 = Point(
            inventoryPoint1.x + Inventory_SCALED_WIDTH,
            inventoryPoint1.y + Inventory_SCALED_HEIGHT
        )
        inventory.place(inventoryPoint1.x, inventoryPoint1.y, inventoryPoint2.x, inventoryPoint2.y)

        // farmArea
        val blockDeep: Double = BASE_FARMLAND_BLACK_DEEP / BASE_BG_HEIGHT * window.height
        val blockHeight: Double = BASE_BLOCK_HEIGHT / BASE_BG_HEIGHT * window.height
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

    override fun update() = farmArea.update()

    override fun render() {
        super.render()
        farmArea.render()
        hotBar.render()
        inventory.render()
    }

    override fun cleanup() {
        bgRenderer.cleanup()
        farmArea.cleanup()
        hotBar.cleanup()
        inventory.cleanup()
    }
}