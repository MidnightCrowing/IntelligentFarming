package com.midnightcrowing.farmings

import com.midnightcrowing.farmings.crops.*
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.resource.ResourcesEnum
import kotlin.reflect.KClass


/**
 * 农场物品类
 * 该类用于表示农场中的物品，包括种子和成熟的作物。
 * 每个子类代表一种特定的农作物或种子。
 *
 * @param parent 父组件
 * @param itemEnum 资源枚举
 */
sealed class FarmItems(parent: Widget, itemEnum: ResourcesEnum) : Widget(parent) {
    override val renderer: ImageRenderer = ImageRenderer.createImageRenderer(itemEnum.inputStream)

    abstract val isSeed: Boolean

    open fun getCrop(): KClass<out FarmCropBase>? = null

    class CabbageItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CABBAGE) {
        override fun toString(): String = "卷心菜"
        override val isSeed: Boolean = false
    }

    class CabbageSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CABBAGE_SEED) {
        override fun toString(): String = "卷心菜种子"
        override val isSeed: Boolean = true
        override fun getCrop(): KClass<out FarmCropBase> = Cabbage::class
    }

    class CarrotItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CARROT) {
        override fun toString(): String = "胡萝卜"
        override val isSeed: Boolean = true
        override fun getCrop(): KClass<out FarmCropBase> = Carrot::class
    }

    class CornItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CORN) {
        override fun toString(): String = "玉米"
        override val isSeed: Boolean = false
    }

    class CornSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CORN_SEED) {
        override fun toString(): String = "玉米种子"
        override val isSeed: Boolean = true
        override fun getCrop(): KClass<out FarmCropBase> = Corn::class
    }

    class CottonItem(parent: Widget) : FarmItems(parent, ResourcesEnum.COTTON) {
        override fun toString(): String = "棉花"
        override val isSeed: Boolean = false
    }

    class CottonSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.COTTON_SEED) {
        override fun toString(): String = "棉花种子"
        override val isSeed: Boolean = true
        override fun getCrop(): KClass<out FarmCropBase> = Cotton::class
    }

    class OnionItem(parent: Widget) : FarmItems(parent, ResourcesEnum.ONION) {
        override fun toString(): String = "洋葱"
        override val isSeed: Boolean = true
        override fun getCrop(): KClass<out FarmCropBase> = Onion::class
    }

    class PotatoItem(parent: Widget) : FarmItems(parent, ResourcesEnum.POTATO) {
        override fun toString(): String = "土豆"
        override val isSeed: Boolean = true
        override fun getCrop(): KClass<out FarmCropBase> = Potato::class
    }

    class TomatoItem(parent: Widget) : FarmItems(parent, ResourcesEnum.TOMATO) {
        override fun toString(): String = "西红柿"
        override val isSeed: Boolean = false
    }

    class TomatoSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.TOMATO_SEED) {
        override fun toString(): String = "西红柿种子"
        override val isSeed: Boolean = true
        override fun getCrop(): KClass<out FarmCropBase> = Tomato::class
    }

    class WheatItem(parent: Widget) : FarmItems(parent, ResourcesEnum.WHEAT) {
        override fun toString(): String = "小麦"
        override val isSeed: Boolean = false
    }

    class WheatSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.WHEAT_SEED) {
        override fun toString(): String = "小麦种子"
        override val isSeed: Boolean = true
        override fun getCrop(): KClass<out FarmCropBase> = Wheat::class
    }
}