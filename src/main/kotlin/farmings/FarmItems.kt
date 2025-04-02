package com.midnightcrowing.farmings

import com.midnightcrowing.farmings.crops.*
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.LineRenderer
import com.midnightcrowing.renderer.RectangleRenderer
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.system.MemoryStack


/**
 * 农场物品类
 * 该类用于表示农场中的物品，包括种子和成熟的作物。
 * 每个子类代表一种特定的农作物或种子。
 *
 * @param parent 父组件
 * @param textureEnum 纹理资源枚举
 */
sealed class FarmItems(parent: Widget, textureEnum: TextureResourcesEnum) : Widget(parent) {
    // region 渲染器
    override val renderer: TextureRenderer = TextureRenderer(textureEnum.texture)
    val numTextRenderer: TextRenderer = TextRenderer(parent.window.nvg).apply {
        fontSize = 32.0
        textAlign = NVG_ALIGN_RIGHT or NVG_ALIGN_MIDDLE

        shadowOffsetX = 3.0
        shadowOffsetY = 3.0
    }

    // 物品名称渲染器
    private val nameTextRenderer = TextRenderer(window.nvg).apply {
        textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }
    private val rectangleRenderer = RectangleRenderer()
    private val lineRenderers = LineRenderer() // 边框线

    // endregion

    // region 辅助数据类
    // 用于存储边框参数的数据类
    private data class ItemNameBorderSegment(
        val x1: Double,
        val y1: Double,
        val x2: Double,
        val y2: Double,
        val color: FloatArray,
        val width: Double,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ItemNameBorderSegment

            if (x1 != other.x1) return false
            if (y1 != other.y1) return false
            if (x2 != other.x2) return false
            if (y2 != other.y2) return false
            if (width != other.width) return false
            if (!color.contentEquals(other.color)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x1.hashCode()
            result = 31 * result + y1.hashCode()
            result = 31 * result + x2.hashCode()
            result = 31 * result + y2.hashCode()
            result = 31 * result + width.hashCode()
            result = 31 * result + color.contentHashCode()
            return result
        }
    }

    // endregion

    // region 农作物方法
    open fun getCrop(farmArea: FarmArea): FarmCropBase? = null

    // endregion

    // region 位置计算 & 渲染方法
    override fun place(bounds: ScreenBounds) {
        super.place(bounds)
        numTextRenderer.x = bounds.x2 + 3
        numTextRenderer.y = bounds.y2 - 10
    }

    fun render(num: Int) {
        super.render()
        if (num > 1) {
            numTextRenderer.render(num.toString())
        }
    }

    fun renderItemName(
        startX: Double,
        startY: Double,
        fontSize: Double = 25.0,
        padding: Int = 6,
        borderWidth: Double = 4.0,
        opacity: Double = 0.95,
        bgColor: FloatArray = floatArrayOf(22 / 255f, 8 / 255f, 22 / 255f, opacity.toFloat()),
        borderColor: FloatArray = floatArrayOf(37 / 255f, 4 / 255f, 92 / 255f, opacity.toFloat()),
    ) {
        MemoryStack.stackPush().use { stack ->
            // 1. 计算文本边界
            val itemName = this.toString()
            val textBounds = stack.mallocFloat(4)
            nvgFontSize(window.nvg, fontSize.toFloat())
            nvgTextBounds(window.nvg, 0f, 0f, itemName, textBounds)

            // 2. 计算基础尺寸
            val textWidth = textBounds[2] - textBounds[0]
            val textHeight = textBounds[3] - textBounds[1]

            // 3. 计算背景框坐标
            val bgLeft = startX - padding
            val bgRight = startX + textWidth + padding
            val bgTop = startY - (textHeight / 2) - padding
            val bgBottom = startY + (textHeight / 2) + padding

            // 4. 渲染背景
            rectangleRenderer.apply {
                x1 = bgLeft
                y1 = bgTop
                x2 = bgRight
                y2 = bgBottom
                color = bgColor
            }.render()

            // 5. 定义所有边框配置
            val borders = listOf(
                // 顶部双边框
                ItemNameBorderSegment(
                    bgLeft - borderWidth, bgTop - borderWidth / 2,
                    bgRight + borderWidth, bgTop - borderWidth / 2,
                    borderColor, borderWidth
                ),
                ItemNameBorderSegment(
                    bgLeft - borderWidth, bgTop - borderWidth * 3 / 2,
                    bgRight + borderWidth, bgTop - borderWidth * 3 / 2,
                    bgColor, borderWidth
                ),
                // 底部双边框
                ItemNameBorderSegment(
                    bgLeft - borderWidth, bgBottom + borderWidth / 2,
                    bgRight + borderWidth, bgBottom + borderWidth / 2,
                    borderColor, borderWidth
                ),
                ItemNameBorderSegment(
                    bgLeft - borderWidth, bgBottom + borderWidth * 3 / 2,
                    bgRight + borderWidth, bgBottom + borderWidth * 3 / 2,
                    bgColor, borderWidth
                ),
                // 左侧双边框
                ItemNameBorderSegment(
                    bgLeft - borderWidth / 2, bgTop - borderWidth,
                    bgLeft - borderWidth / 2, bgBottom + borderWidth,
                    borderColor, borderWidth
                ),
                ItemNameBorderSegment(
                    bgLeft - borderWidth * 3 / 2, bgTop - borderWidth,
                    bgLeft - borderWidth * 3 / 2, bgBottom + borderWidth,
                    bgColor, borderWidth
                ),
                // 右侧双边框
                ItemNameBorderSegment(
                    bgRight + borderWidth / 2, bgTop - borderWidth,
                    bgRight + borderWidth / 2, bgBottom + borderWidth,
                    borderColor, borderWidth
                ),
                ItemNameBorderSegment(
                    bgRight + borderWidth * 3 / 2, bgTop - borderWidth,
                    bgRight + borderWidth * 3 / 2, bgBottom + borderWidth,
                    bgColor, borderWidth
                )
            )

            // 6. 批量渲染边框
            borders.forEachIndexed { index, config ->
                lineRenderers.apply {
                    x1 = config.x1
                    y1 = config.y1
                    x2 = config.x2
                    y2 = config.y2
                    width = config.width
                    color = config.color
                    render()
                }
            }

            // 7. 渲染文本
            nameTextRenderer.apply {
                this.x = startX
                this.y = startY
                this.fontSize = fontSize
                this.text = itemName
            }.render()
        }
    }

    // endregion

    class CabbageItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.CABBAGE) {
        companion object {
            const val id: String = "minecraft:cabbage"
        }

        override fun toString(): String = "卷心菜"
    }

    class CabbageSeedItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.CABBAGE_SEED) {
        companion object {
            const val id: String = "minecraft:cabbage_seed"
        }

        override fun toString(): String = "卷心菜种子"
        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Cabbage(farmArea)
    }

    class CarrotItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.CARROT) {
        companion object {
            const val id: String = "minecraft:carrot"
        }

        override fun toString(): String = "胡萝卜"
        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Carrot(farmArea)
    }

    class GoldenCarrot(parent: Widget) : FarmItems(parent, TextureResourcesEnum.GOLDEN_CARROT) {
        companion object {
            const val id: String = "minecraft:golden_carrot"
        }

        override fun toString(): String = "金胡萝卜"
    }

    class CornItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.CORN) {
        companion object {
            const val id: String = "minecraft:corn"
        }

        override fun toString(): String = "玉米"
    }

    class CornSeedItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.CORN_SEED) {
        companion object {
            const val id: String = "minecraft:corn_seed"
        }

        override fun toString(): String = "玉米种子"
        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Corn(farmArea)
    }

    class CottonItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.COTTON) {
        companion object {
            const val id: String = "minecraft:cotton"
        }

        override fun toString(): String = "棉花"
    }

    class CottonSeedItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.COTTON_SEED) {
        companion object {
            const val id: String = "minecraft:cotton_seed"
        }

        override fun toString(): String = "棉花种子"
        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Cotton(farmArea)
    }

    class OnionItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.ONION) {
        companion object {
            const val id: String = "minecraft:onion"
        }

        override fun toString(): String = "洋葱"
        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Onion(farmArea)
    }

    class PotatoItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.POTATO) {
        companion object {
            const val id: String = "minecraft:potato"
        }

        override fun toString(): String = "土豆"
        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Potato(farmArea)
    }

    class TomatoItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.TOMATO) {
        companion object {
            const val id: String = "minecraft:tomato"
        }

        override fun toString(): String = "西红柿"
    }

    class TomatoSeedItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.TOMATO_SEED) {
        companion object {
            const val id: String = "minecraft:tomato_seed"
        }

        override fun toString(): String = "西红柿种子"
        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Tomato(farmArea)
    }

    class WheatItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.WHEAT) {
        companion object {
            const val id: String = "minecraft:wheat"
        }

        override fun toString(): String = "小麦"
    }

    class WheatSeedItem(parent: Widget) : FarmItems(parent, TextureResourcesEnum.WHEAT_SEED) {
        companion object {
            const val id: String = "minecraft:wheat_seed"
        }

        override fun toString(): String = "小麦种子"
        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Wheat(farmArea)
    }

    class Emerald(parent: Widget) : FarmItems(parent, TextureResourcesEnum.EMERALD) {
        companion object {
            const val id: String = "minecraft:emerald"
        }

        override fun toString(): String = "绿宝石"
    }
}