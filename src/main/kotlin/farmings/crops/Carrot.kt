package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.farmings.FarmItems.CarrotItem
import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.Texture
import com.midnightcrowing.resource.TextureResourcesEnum

class Carrot(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.CARROT_GROW_0.texture,
        1 to TextureResourcesEnum.CARROT_GROW_0.texture,
        2 to TextureResourcesEnum.CARROT_GROW_2.texture,
        3 to TextureResourcesEnum.CARROT_GROW_2.texture,
        4 to TextureResourcesEnum.CARROT_GROW_4.texture,
        5 to TextureResourcesEnum.CARROT_GROW_4.texture,
        6 to TextureResourcesEnum.CARROT_GROW_4.texture,
        7 to TextureResourcesEnum.CARROT_GROW_7.texture
    )

    override fun getFarmItem(parent: Widget): FarmItems = CarrotItem(parent)

    override fun toString(): String = "胡萝卜"

    override fun copy(): Carrot {
        val newCarrot = Carrot(farmArea)
        newCarrot.place(this.widgetBounds)
        newCarrot.nowTextures = newCarrot.nowTextures
        return newCarrot
    }
}