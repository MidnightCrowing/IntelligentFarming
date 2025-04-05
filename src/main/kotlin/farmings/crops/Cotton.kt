package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items.COTTON
import com.midnightcrowing.model.item.Items.COTTON_SEED
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
        if (isFullyGrown) ItemStack(COTTON.id, 1) else ItemStack(COTTON_SEED.id, 1)

    override fun getDrops(): Array<ItemStack> {
        return if (isFullyGrown) {
            arrayOf(
                ItemStack(COTTON_SEED.id, 1 + generateDropCount(n = 4, p = 4.0 / 7)),
                ItemStack(COTTON.id, 1),
            )
        } else {
            arrayOf(ItemStack(COTTON_SEED.id, 1))
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