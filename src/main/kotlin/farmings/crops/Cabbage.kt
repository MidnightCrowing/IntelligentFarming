package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.CabbageItem
import com.midnightcrowing.farmings.FarmItems.CabbageSeedItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.Texture
import com.midnightcrowing.resource.ResourcesEnum

class Cabbage(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to Texture.createImageTexture(ResourcesEnum.CABBAGE_GROW_0.inputStream),
        1 to Texture.createImageTexture(ResourcesEnum.CABBAGE_GROW_1.inputStream),
        2 to Texture.createImageTexture(ResourcesEnum.CABBAGE_GROW_2.inputStream),
        3 to Texture.createImageTexture(ResourcesEnum.CABBAGE_GROW_3.inputStream),
        4 to Texture.createImageTexture(ResourcesEnum.CABBAGE_GROW_4.inputStream),
        5 to Texture.createImageTexture(ResourcesEnum.CABBAGE_GROW_5.inputStream),
        6 to Texture.createImageTexture(ResourcesEnum.CABBAGE_GROW_6.inputStream),
        7 to Texture.createImageTexture(ResourcesEnum.CABBAGE_GROW_7.inputStream)
    )

    override fun getFarmItem(parent: Widget): FarmItems =
        if (isFullyGrown) CabbageItem(parent) else CabbageSeedItem(parent)

    override fun toString(): String = "卷心菜"

    override fun copy(): Cabbage {
        val newCabbage = Cabbage(farmArea)
        newCabbage.place(this.widgetBounds)
        newCabbage.renderer.texture = newCabbage.growInitTexture
        return newCabbage
    }
}