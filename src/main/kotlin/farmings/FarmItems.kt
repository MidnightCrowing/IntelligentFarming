package com.midnightcrowing.farmings

import com.midnightcrowing.farmings.crops.*
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.TextRenderer
import com.midnightcrowing.render.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_RIGHT


/**
 * 农场物品类
 * 该类用于表示农场中的物品，包括种子和成熟的作物。
 * 每个子类代表一种特定的农作物或种子。
 *
 * @param parent 父组件
 * @param textureEnum 纹理资源枚举
 */
sealed class FarmItems(parent: Widget, textureEnum: TextureResourcesEnum) : Widget(parent) {
    override val renderer: TextureRenderer = TextureRenderer(textureEnum.texture)
    val numTextRenderer: TextRenderer = TextRenderer(parent.window.nvg).apply {
        fontSize = 32.0
        textAlign = NVG_ALIGN_RIGHT or NVG_ALIGN_MIDDLE

        shadowOffsetX = 3.0
        shadowOffsetY = 3.0
    }

    open fun getCrop(farmArea: FarmArea): FarmCropBase? = null

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
}