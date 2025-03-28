package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems.CottonItem
import com.midnightcrowing.farmings.FarmItems.CottonSeedItem
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.resource.TextureResourcesEnum

class Cotton(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.COTTON_GROW_0.texture,
        1 to TextureResourcesEnum.COTTON_GROW_0.texture,
        2 to TextureResourcesEnum.COTTON_GROW_0.texture,
        3 to TextureResourcesEnum.COTTON_GROW_0.texture,
        4 to TextureResourcesEnum.COTTON_GROW_4.texture,
        5 to TextureResourcesEnum.COTTON_GROW_4.texture,
        6 to TextureResourcesEnum.COTTON_GROW_4.texture,
        7 to TextureResourcesEnum.COTTON_GROW_7.texture
    )

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(CottonItem.id, 1) else ItemStack(CottonSeedItem.id, 1)

    override fun getDrops(): Array<ItemStack> {
        return if (isFullyGrown) {
            arrayOf(
                ItemStack(CottonSeedItem.id, 1 + generateDropCount(n = 4, p = 4.0 / 7)),
                ItemStack(CottonItem.id, 1),
            )
        } else {
            arrayOf(ItemStack(CottonSeedItem.id, 1))
        }
    }

    override fun toString(): String = "棉花"

    override fun copy(): Cotton {
        val newCotton = Cotton(farmArea)
        newCotton.place(this.widgetBounds)
        newCotton.nowTextures = newCotton.nowTextures
        return newCotton
    }
}