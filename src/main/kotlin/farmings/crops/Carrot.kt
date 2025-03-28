package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems.CarrotItem
import com.midnightcrowing.farmings.FarmItems.GoldenCarrot
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
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

    override fun getItemStack(): ItemStack = ItemStack(CarrotItem.id, 1)

    override fun getDrops(): Array<ItemStack> = arrayOf(
        ItemStack(CarrotItem.id, if (isFullyGrown) 1 + generateDropCount(n = 3, p = 8.0 / 15) else 1),
        ItemStack(GoldenCarrot.id, if (isFullyGrown) generateDropCount(n = 1, p = 1.0 / 64) else 0)
    )

    override fun toString(): String = "胡萝卜"

    override fun copy(): Carrot {
        val newCarrot = Carrot(farmArea)
        newCarrot.place(this.widgetBounds)
        newCarrot.nowTextures = newCarrot.nowTextures
        return newCarrot
    }
}