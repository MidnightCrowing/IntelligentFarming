package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems.CabbageItem
import com.midnightcrowing.farmings.FarmItems.CabbageSeedItem
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.resource.TextureResourcesEnum

class Cabbage(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.CABBAGE_GROW_0.texture,
        1 to TextureResourcesEnum.CABBAGE_GROW_1.texture,
        2 to TextureResourcesEnum.CABBAGE_GROW_2.texture,
        3 to TextureResourcesEnum.CABBAGE_GROW_3.texture,
        4 to TextureResourcesEnum.CABBAGE_GROW_4.texture,
        5 to TextureResourcesEnum.CABBAGE_GROW_5.texture,
        6 to TextureResourcesEnum.CABBAGE_GROW_6.texture,
        7 to TextureResourcesEnum.CABBAGE_GROW_7.texture
    )

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(CabbageItem.id, 1) else ItemStack(CabbageSeedItem.id, 1)

    override fun getDrops(): Array<ItemStack> {
        return if (isFullyGrown) {
            arrayOf(
                ItemStack(CabbageSeedItem.id, 1 + generateDropCount(n = 3, p = 8.0 / 15)),
                ItemStack(CabbageItem.id, 1),
            )
        } else {
            arrayOf(ItemStack(CabbageSeedItem.id, 1))
        }
    }

    override fun toString(): String = "卷心菜"

    override fun copy(): Cabbage {
        val newCabbage = Cabbage(farmArea)
        newCabbage.place(this.widgetBounds)
        newCabbage.nowTextures = newCabbage.nowTextures
        return newCabbage
    }
}