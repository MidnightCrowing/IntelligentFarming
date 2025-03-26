package com.midnightcrowing.gui

import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.crops.FarmCropBase
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.NineSliceRenderer
import com.midnightcrowing.render.TextRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.scenes.FarmScene
import com.midnightcrowing.utils.LayoutScaler.scaleValue
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE


class CropInfoDisplay(val screen: FarmScene) : Widget(screen.window, z = 1) {
    companion object {
        val COLOR_NORMAL = doubleArrayOf(1.0, 1.0, 1.0, 1.0)
        val COLOR_GREEN = doubleArrayOf(84 / 255.0, 252 / 255.0, 84 / 255.0, 1.0)
    }

    private val bgRenderer = NineSliceRenderer.createNineSliceRenderer(
        ResourcesEnum.TOAST.inputStream, textureBorder = 4f, vertexBorder = 10f
    )

    private var crop: FarmCropBase? = null
    private var item: FarmItems? = null
    private var itemBounds = ScreenBounds.EMPTY

    private val titleText = TextRenderer(window.nvg).apply {
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

    init {
        bgRenderer.alpha = 0.8
    }

    fun setFarmCrop(newCrop: FarmCropBase?) {
        if (crop != newCrop) {
            crop = newCrop
            titleText.text = newCrop?.toString() ?: ""
        }
    }

    private fun updateItem(newItem: FarmItems?) {
        if (item?.let { it::class } != newItem?.let { it::class }) {  // 仅在 item 变化时才替换
            item?.cleanup()
            item = newItem
            item?.place(itemBounds)
        }
    }

    fun clear() {
        item?.cleanup()
        item = null
        crop = null
        titleText.text = ""
        valueText.text = ""
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
        item?.place(itemBounds)

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
        updateItem(crop?.getFarmItem(this))
        valueText.apply {
            text = if (crop?.isFullyGrown == true) "成熟" else "${(crop?.growthProgress?.times(100)?.toInt())}%"
            textColor = if (crop?.isFullyGrown == true) COLOR_GREEN else COLOR_NORMAL
        }
    }

    override fun render() {
        if (!isVisible) return
        bgRenderer.render(widgetBounds)
        item?.render()
        titleText.render()
        descText.render()
        valueText.render()
    }

    override fun cleanup() {
        super.cleanup()
        bgRenderer.cleanup()
        item?.cleanup()
    }
}
