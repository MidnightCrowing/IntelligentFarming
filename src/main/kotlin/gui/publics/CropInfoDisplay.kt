package com.midnightcrowing.gui.publics

import com.midnightcrowing.controllers.CropInfoDisplayControllers
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.NineSliceRenderer
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.utils.LayoutScaler.scaleValue
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE


class CropInfoDisplay(
    parent: Widget,
    private val controller: CropInfoDisplayControllers,
) : Widget(parent) {
    companion object {
        val COLOR_NORMAL = doubleArrayOf(1.0, 1.0, 1.0, 1.0)
        val COLOR_GREEN = doubleArrayOf(84 / 255.0, 252 / 255.0, 84 / 255.0, 1.0)
    }

    init {
        controller.init(this)
    }

    private val bgRenderer = NineSliceRenderer(
        TextureResourcesEnum.TOAST.texture, textureBorder = 4f, vertexBorder = 10f
    ).apply { alpha = 0.6 }

    var itemBounds: ScreenBounds = ScreenBounds.EMPTY

    val titleText = TextRenderer(window.nvg).apply {
        textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
        textColor = doubleArrayOf(1.0, 1.0, 1.0, 1.0)
    }
    private val descText = TextRenderer(window.nvg).apply {
        text = "生长进度："
        textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
        textColor = doubleArrayOf(0.63, 0.63, 0.63, 1.0)
    }
    private val valueText = TextRenderer(window.nvg).apply {
        textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }

    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        super.place(x1, y1, x2, y2)

        // 计算基本尺寸
        val itemSize = widgetBounds.height / 1.75
        val itemY = y1 + widgetBounds.height / 5
        val itemX = widgetBounds.x1 + widgetBounds.width / 15.36
        val textOffsetX = widgetBounds.width / 19.2
        val textOffsetY1 = widgetBounds.height / 60 * 19
        val textOffsetY2 = widgetBounds.height / 60 * 40

        // 计算物品区域
        itemBounds = ScreenBounds(itemX, itemY, itemX + itemSize, itemY + itemSize)
        controller.itemWidget?.place(itemBounds)

        // 计算字体大小和文本偏移
        val titleSize = scaleValue(window.width, 21.0, 31.0)
        val descSize = scaleValue(window.width, 20.0, 30.0)
        val valueOffsetX = scaleValue(window.width, 100.0, 150.0)

        // 设置标题文本
        titleText.apply {
            x = itemBounds.x2 + textOffsetX
            y = widgetBounds.y1 + textOffsetY1
            fontSize = titleSize
        }

        // 设置描述文本
        descText.apply {
            x = itemBounds.x2 + textOffsetX
            y = widgetBounds.y1 + textOffsetY2
            fontSize = descSize
        }

        // 设置描述值文本
        valueText.apply {
            x = descText.x + valueOffsetX
            y = descText.y
            fontSize = descSize
        }
    }

    fun update() {
        controller.updateItem(controller.crop?.getItemStack())
        valueText.apply {
            text =
                if (controller.crop?.isFullyGrown == true) "成熟"
                else "${(controller.crop?.growthProgress?.times(100)?.toInt())}%"
            textColor = if (controller.crop?.isFullyGrown == true) COLOR_GREEN else COLOR_NORMAL
        }
    }

    override fun render() {
        if (!isVisible || controller.item.isEmpty()) {
            return
        }

        bgRenderer.render(widgetBounds)
        controller.itemWidget?.render()
        titleText.render()
        descText.render()
        valueText.render()
    }

    override fun doCleanup() {
        controller.itemWidget?.cleanup()
    }
}
