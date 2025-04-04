package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.Item.CornItem
import com.midnightcrowing.model.item.Item.CornSeedItem
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.resource.TextureResourcesEnum

class Corn(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.CORN_GROW_0.texture,
        1 to TextureResourcesEnum.CORN_GROW_1.texture,
        2 to TextureResourcesEnum.CORN_GROW_2.texture,
        3 to TextureResourcesEnum.CORN_GROW_3.texture,
        4 to TextureResourcesEnum.CORN_GROW_4.texture,
        5 to TextureResourcesEnum.CORN_GROW_5.texture,
        6 to TextureResourcesEnum.CORN_GROW_6.texture,
        7 to TextureResourcesEnum.CORN_GROW_7.texture
    )

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(CornItem.id, 1) else ItemStack(CornSeedItem.id, 1)

    override fun getDrops(): Array<ItemStack> {
        return if (isFullyGrown) {
            arrayOf(
                ItemStack(CornSeedItem.id, 1 + generateDropCount(n = 4, p = 8.0 / 15)),
                ItemStack(CornItem.id, 1),
            )
        } else {
            arrayOf(ItemStack(CornSeedItem.id, 1))
        }
    }

    override fun toString(): String = "玉米"

    override fun copy(): Corn {
        val newCorn = Corn(farmArea)
        newCorn.place(this.widgetBounds)
        newCorn.nowTextures = newCorn.nowTextures
        return newCorn
    }
}