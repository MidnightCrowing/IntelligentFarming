package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.Item.WheatItem
import com.midnightcrowing.model.item.Item.WheatSeedItem
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.resource.TextureResourcesEnum

class Wheat(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.WHEAT_GROW_0.texture,
        1 to TextureResourcesEnum.WHEAT_GROW_1.texture,
        2 to TextureResourcesEnum.WHEAT_GROW_2.texture,
        3 to TextureResourcesEnum.WHEAT_GROW_3.texture,
        4 to TextureResourcesEnum.WHEAT_GROW_4.texture,
        5 to TextureResourcesEnum.WHEAT_GROW_5.texture,
        6 to TextureResourcesEnum.WHEAT_GROW_6.texture,
        7 to TextureResourcesEnum.WHEAT_GROW_7.texture
    )

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(WheatItem.id, 1) else ItemStack(WheatSeedItem.id, 1)

    override fun getDrops(): Array<ItemStack> {
        return if (isFullyGrown) {
            arrayOf(
                ItemStack(WheatSeedItem.id, 1 + generateDropCount(n = 3, p = 4.0 / 7)),
                ItemStack(WheatItem.id, 1),
            )
        } else {
            arrayOf(ItemStack(WheatSeedItem.id, 1))
        }
    }

    override fun toString(): String = "小麦"

    override fun copy(): Wheat {
        val newWheat = Wheat(farmArea)
        newWheat.place(this.widgetBounds)
        newWheat.nowTextures = newWheat.nowTextures
        return newWheat
    }
}