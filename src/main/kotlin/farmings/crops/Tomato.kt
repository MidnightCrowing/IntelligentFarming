package com.midnightcrowing.farmings.crops

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.farmings.FarmItems.TomatoItem
import com.midnightcrowing.farmings.FarmItems.TomatoSeedItem
import com.midnightcrowing.model.Texture
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.utils.GameTick

class Tomato(farmArea: FarmArea) : FarmCropBase(farmArea) {
    override val growDuringTextures: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.BUDDING_TOMATO_GROW_0.texture,
        1 to TextureResourcesEnum.BUDDING_TOMATO_GROW_1.texture,
        2 to TextureResourcesEnum.BUDDING_TOMATO_GROW_2.texture,
        3 to TextureResourcesEnum.BUDDING_TOMATO_GROW_3.texture,
        4 to TextureResourcesEnum.BUDDING_TOMATO_GROW_4.texture,
        5 to TextureResourcesEnum.TOMATO_GROW_0.texture,
        6 to TextureResourcesEnum.TOMATO_GROW_1.texture,
        7 to TextureResourcesEnum.TOMATO_GROW_2.texture,
        8 to TextureResourcesEnum.TOMATO_GROW_3.texture
    )

    override fun onFarmRightClick(): Array<ItemStack>? {
        if (isFullyGrown) {
            val drops = getDrops()
            growthDuration = triangularRandom(90000.0, 800000.0, 120000.0)
            plantedTick = (GameTick.tick - growthDuration * 0.56).toLong()
            return drops
        }
        return null
    }

    override fun getItemStack(): ItemStack =
        if (isFullyGrown) ItemStack(TomatoItem.id, 1) else ItemStack(TomatoSeedItem.id, 1)

    override fun getDrops(): Array<ItemStack> = if (isFullyGrown) {
        arrayOf(
            ItemStack(TomatoSeedItem.id, 1),
            ItemStack(TomatoItem.id, 1 + generateDropCount(n = 2, p = 8.0 / 15)),
        )
    } else {
        arrayOf(ItemStack(TomatoSeedItem.id, 1))
    }

    override fun toString(): String = "番茄"

    override fun copy(): Tomato {
        val newTomato = Tomato(farmArea)
        newTomato.place(this.widgetBounds)
        newTomato.nowTextures = newTomato.nowTextures
        return newTomato
    }
}