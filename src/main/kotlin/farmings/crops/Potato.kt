package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.PotatoItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.render.Texture
import com.midnightcrowing.resource.ResourcesEnum

class Potato(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to Texture.createImageTexture(ResourcesEnum.POTATO_GROW_0.inputStream),
        1 to Texture.createImageTexture(ResourcesEnum.POTATO_GROW_0.inputStream),
        2 to Texture.createImageTexture(ResourcesEnum.POTATO_GROW_2.inputStream),
        3 to Texture.createImageTexture(ResourcesEnum.POTATO_GROW_2.inputStream),
        4 to Texture.createImageTexture(ResourcesEnum.POTATO_GROW_4.inputStream),
        5 to Texture.createImageTexture(ResourcesEnum.POTATO_GROW_4.inputStream),
        6 to Texture.createImageTexture(ResourcesEnum.POTATO_GROW_4.inputStream),
        7 to Texture.createImageTexture(ResourcesEnum.POTATO_GROW_7.inputStream)
    )

    override fun getFarmItem(parent: Widget): FarmItems = PotatoItem(parent)

    override fun toString(): String = "马铃薯"

    override fun copy(): Potato {
        val newPotato = Potato(farmArea)
        newPotato.place(this.widgetBounds)
        newPotato.renderer.texture = newPotato.growInitTexture
        return newPotato
    }
}