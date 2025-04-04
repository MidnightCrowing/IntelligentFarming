package com.midnightcrowing.model.item

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.crops.*
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.LineRenderer
import com.midnightcrowing.renderer.RectangleRenderer
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import org.lwjgl.nanovg.NanoVG
import org.lwjgl.system.MemoryStack

/**
 * 物品类
 *
 * @param parent 父组件
 * @param textureEnum 纹理资源枚举
 */
sealed class Item(
    parent: Widget,
    val name: String,
    textureEnum: TextureResourcesEnum,
) : Widget(parent) {
    companion object {
        fun registerAll() {
            ItemRegistry.register(CabbageItem.id) { CabbageItem(it) }
            ItemRegistry.register(CabbageSeedItem.id) { CabbageSeedItem(it) }
            ItemRegistry.register(CarrotItem.id) { CarrotItem(it) }
            ItemRegistry.register(CornItem.id) { CornItem(it) }
            ItemRegistry.register(CornSeedItem.id) { CornSeedItem(it) }
            ItemRegistry.register(CottonItem.id) { CottonItem(it) }
            ItemRegistry.register(CottonSeedItem.id) { CottonSeedItem(it) }
            ItemRegistry.register(OnionItem.id) { OnionItem(it) }
            ItemRegistry.register(PotatoItem.id) { PotatoItem(it) }
            ItemRegistry.register(TomatoItem.id) { TomatoItem(it) }
            ItemRegistry.register(TomatoSeedItem.id) { TomatoSeedItem(it) }
            ItemRegistry.register(WheatItem.id) { WheatItem(it) }
            ItemRegistry.register(WheatSeedItem.id) { WheatSeedItem(it) }
            ItemRegistry.register(Chest.id) { Chest(it) }
            ItemRegistry.register(Emerald.id) { Emerald(it) }
            ItemRegistry.register(VillagerSpawnEgg.id) { VillagerSpawnEgg(it) }
            ItemRegistry.register(GoldenCarrot.id) { GoldenCarrot(it) }
        }
    }

    // region 渲染器
    override val renderer: TextureRenderer = TextureRenderer(textureEnum.texture)
    val numTextRenderer: TextRenderer = TextRenderer(parent.window.nvg).apply {
        fontSize = 32.0
        textAlign = NanoVG.NVG_ALIGN_RIGHT or NanoVG.NVG_ALIGN_MIDDLE

        shadowOffsetX = 3.0
        shadowOffsetY = 3.0
    }

    // 物品名称渲染器
    private val nameTextRenderer = TextRenderer(window.nvg).apply {
        textAlign = NanoVG.NVG_ALIGN_LEFT or NanoVG.NVG_ALIGN_MIDDLE
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

    // region 其他方法
    open fun getCrop(farmArea: FarmArea): FarmCropBase? = null

    override fun toString(): String = name

    // endregion

    // region 位置计算 & 渲染方法
    override fun place(bounds: ScreenBounds) {
        super.place(bounds)
        numTextRenderer.x = bounds.x2 + 3
        numTextRenderer.y = bounds.y2 - 10
    }

    fun render(num: Int) {
        super.render()
        if (num != 1) {
            numTextRenderer.render(num.toString())
        }
        if (num <= 0) {
            System.err.println("$this 警告: num值: $num 不在预期范围内")
            numTextRenderer.textColor = doubleArrayOf(1.0, 0.0, 0.0, 1.0)
        }
    }

    /**
     * 渲染物品名称提示框
     *
     * @param mouseX 鼠标X坐标
     * @param mouseY 鼠标Y坐标
     * @param position 提示框位置 （`center`, `above`, `after-top`, `after`, `after-bottom`, `below`, `before-top`, `before`, `before-bottom`）
     * @param fontSize 字体大小
     * @param padding 提示框内边距
     * @param borderWidth 边框宽度
     * @param opacity 提示框透明度
     * @param bgColor 提示框背景颜色
     * @param borderColor 边框颜色
     * @param itemName 物品名称
     */
    fun renderTooltip(
        mouseX: Double,
        mouseY: Double,
        position: String = "center",
        fontSize: Double = 25.0,
        padding: Int = 6,
        borderWidth: Double = 4.0,
        opacity: Double = 0.95,
        bgColor: FloatArray = floatArrayOf(22 / 255f, 8 / 255f, 22 / 255f, opacity.toFloat()),
        borderColor: FloatArray = floatArrayOf(37 / 255f, 4 / 255f, 92 / 255f, opacity.toFloat()),
        itemName: String? = null,
    ) {
        val itemName = itemName ?: this.toString()

        MemoryStack.stackPush().use { stack ->
            // 1. 计算文本边界
            val textBounds = stack.mallocFloat(4)
            NanoVG.nvgFontSize(window.nvg, fontSize.toFloat())
            NanoVG.nvgTextBounds(window.nvg, 0f, 0f, itemName, textBounds)

            // 2. 计算基础尺寸
            val textWidth = textBounds[2] - textBounds[0]
            val textHeight = textBounds[3] - textBounds[1]

            // 3. 计算起始坐标
            val (startX, startY) = when (position) {
                "center" -> mouseX to mouseY
                "above" -> mouseX - textWidth / 2 to mouseY - 40
                "after-top" -> mouseX + 30 to mouseY - 25
                "after" -> mouseX + 30 to mouseY
                "after-bottom" -> mouseX + 30 to mouseY + 35
                "below" -> mouseX - textWidth / 2 to mouseY + 50
                "before-top" -> mouseX - textWidth - 30 to mouseY - 25
                "before" -> mouseX - textWidth - 30 to mouseY
                "before-bottom" -> mouseX - textWidth - 30 to mouseY + 35
                else -> {
                    System.err.println("$position 位置不在预期范围内")
                    mouseX to mouseY
                }
            }

            // 4. 计算背景框坐标
            val bgLeft = startX - padding
            val bgRight = startX + textWidth + padding
            val bgTop = startY - (textHeight / 2) - padding
            val bgBottom = startY + (textHeight / 2) + padding

            // 5. 渲染背景
            rectangleRenderer.apply {
                x1 = bgLeft
                y1 = bgTop
                x2 = bgRight
                y2 = bgBottom
                color = bgColor
            }.render()

            // 6. 定义所有边框配置
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

            // 7. 批量渲染边框
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

            // 8. 渲染文本
            nameTextRenderer.apply {
                this.x = startX
                this.y = startY
                this.fontSize = fontSize
                this.text = itemName
            }.render()
        }
    }

    // endregion

    // region 农作物物品类
    // 这些类用于表示农场中的物品，包括种子和成熟的作物。
    // 每个子类代表一种特定的农作物或种子。

    class CabbageItem(parent: Widget) : Item(
        parent,
        "卷心菜",
        TextureResourcesEnum.CABBAGE
    ) {
        companion object {
            const val id = "minecraft:cabbage"
        }
    }

    class CabbageSeedItem(parent: Widget) : Item(
        parent,
        "卷心菜种子",
        TextureResourcesEnum.CABBAGE_SEED
    ) {
        companion object {
            const val id = "minecraft:cabbage_seed"
        }

        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Cabbage(farmArea)
    }

    class CarrotItem(parent: Widget) : Item(
        parent,
        "胡萝卜",
        TextureResourcesEnum.CARROT
    ) {
        companion object {
            const val id = "minecraft:carrot"
        }

        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Carrot(farmArea)
    }

    class CornItem(parent: Widget) : Item(
        parent,
        "玉米",
        TextureResourcesEnum.CORN
    ) {
        companion object {
            const val id = "minecraft:corn"
        }
    }

    class CornSeedItem(parent: Widget) : Item(
        parent,
        "玉米种子",
        TextureResourcesEnum.CORN_SEED
    ) {
        companion object {
            const val id = "minecraft:corn_seed"
        }

        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Corn(farmArea)
    }

    class CottonItem(parent: Widget) : Item(
        parent,
        "棉花",
        TextureResourcesEnum.COTTON
    ) {
        companion object {
            const val id = "minecraft:cotton"
        }
    }

    class CottonSeedItem(parent: Widget) : Item(
        parent,
        "棉花种子",
        TextureResourcesEnum.COTTON_SEED
    ) {
        companion object {
            const val id = "minecraft:cotton_seed"
        }

        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Cotton(farmArea)
    }

    class OnionItem(parent: Widget) : Item(
        parent,
        "洋葱",
        TextureResourcesEnum.ONION
    ) {
        companion object {
            const val id = "minecraft:onion"
        }

        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Onion(farmArea)
    }

    class PotatoItem(parent: Widget) : Item(
        parent,
        "土豆",
        TextureResourcesEnum.POTATO
    ) {
        companion object {
            const val id = "minecraft:potato"
        }

        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Potato(farmArea)
    }

    class TomatoItem(parent: Widget) : Item(
        parent,
        "西红柿",
        TextureResourcesEnum.TOMATO
    ) {
        companion object {
            const val id = "minecraft:tomato"
        }
    }

    class TomatoSeedItem(parent: Widget) : Item(
        parent,
        "西红柿种子",
        TextureResourcesEnum.TOMATO_SEED
    ) {
        companion object {
            const val id = "minecraft:tomato_seed"
        }

        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Tomato(farmArea)
    }

    class WheatItem(parent: Widget) : Item(
        parent,
        "小麦",
        TextureResourcesEnum.WHEAT
    ) {
        companion object {
            const val id = "minecraft:wheat"
        }
    }

    class WheatSeedItem(parent: Widget) : Item(
        parent,
        "小麦种子",
        TextureResourcesEnum.WHEAT_SEED
    ) {
        companion object {
            const val id = "minecraft:wheat_seed"
        }

        override fun getCrop(farmArea: FarmArea): FarmCropBase? = Wheat(farmArea)
    }

    // endregion

    // region 其他物品类
    class Chest(parent: Widget) : Item(
        parent,
        "箱子",
        TextureResourcesEnum.CHEST
    ) {
        companion object {
            const val id = "minecraft:chest"
        }
    }

    class Emerald(parent: Widget) : Item(
        parent,
        "绿宝石",
        TextureResourcesEnum.EMERALD
    ) {
        companion object {
            const val id = "minecraft:emerald"
        }
    }

    class VillagerSpawnEgg(parent: Widget) : Item(
        parent,
        "村民刷怪蛋",
        TextureResourcesEnum.VILLAGER_SPAWN_EGG
    ) {
        companion object {
            const val id = "minecraft:villager_spawn_egg"
        }
    }

    class GoldenCarrot(parent: Widget) : Item(
        parent,
        "金胡萝卜",
        TextureResourcesEnum.GOLDEN_CARROT
    ) {
        companion object {
            const val id = "minecraft:golden_carrot"
        }
    }

    // endregion
}