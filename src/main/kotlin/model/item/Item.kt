package com.midnightcrowing.model.item

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.resource.TextureResourcesEnum

/**
 * 物品类
 *
 * @param id 物品ID
 * @param name 物品名称
 * @param textureEnum 纹理资源枚举
 * @param block 物品对应的作物块，默认为null
 * @param maxCount 物品最大堆叠数量，默认为64
 */
data class Item(
    val id: String,
    val name: String,
    val textureEnum: TextureResourcesEnum,
    private val block: (FarmArea) -> FarmCropBase? = { null },
    val maxCount: Int = 64,

    // 附魔属性
    var fortune: Int = 0,  // 时运
) {
    fun getBlock(farmArea: FarmArea): FarmCropBase? {
        return block(farmArea)
    }
}