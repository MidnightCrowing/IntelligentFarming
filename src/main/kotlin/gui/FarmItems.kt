package com.midnightcrowing.gui

import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum

sealed class FarmItems(parent: Widget, itemEnum: ResourcesEnum) : Widget(parent) {
    override val renderer: ImageRenderer = createImageRenderer(itemEnum.inputStream)

    class CabbageItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CABBAGE) {
        override fun toString(): String = "卷心菜"
    }

    class CabbageSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CABBAGE_SEED) {
        override fun toString(): String = "卷心菜种子"
    }

    class CarrotItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CARROT) {
        override fun toString(): String = "胡萝卜"
    }

    class CornItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CORN) {
        override fun toString(): String = "玉米"
    }

    class CornSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.CORN_SEED) {
        override fun toString(): String = "玉米种子"
    }

    class CottonItem(parent: Widget) : FarmItems(parent, ResourcesEnum.COTTON) {
        override fun toString(): String = "棉花"
    }

    class CottonSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.COTTON_SEED) {
        override fun toString(): String = "棉花种子"
    }

    class OnionItem(parent: Widget) : FarmItems(parent, ResourcesEnum.ONION) {
        override fun toString(): String = "洋葱"
    }

    class PotatoItem(parent: Widget) : FarmItems(parent, ResourcesEnum.POTATO) {
        override fun toString(): String = "土豆"
    }

    class TomatoItem(parent: Widget) : FarmItems(parent, ResourcesEnum.TOMATO) {
        override fun toString(): String = "西红柿"
    }

    class TomatoSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.TOMATO_SEED) {
        override fun toString(): String = "西红柿种子"
    }

    class WheatItem(parent: Widget) : FarmItems(parent, ResourcesEnum.WHEAT) {
        override fun toString(): String = "小麦"
    }

    class WheatSeedItem(parent: Widget) : FarmItems(parent, ResourcesEnum.WHEAT_SEED) {
        override fun toString(): String = "小麦种子"
    }
}