package com.midnightcrowing.model.item

import com.midnightcrowing.farmings.FarmArea
import com.midnightcrowing.farmings.FarmCropBase
import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.resource.ResourceType

/**
 * 物品类
 *
 * @param namespace 命名空间，如 "minecraft"
 * @param id 物品 ID，例如 "carrot"
 * @param displayName 鼠标悬停显示名称，默认自动推导
 * @param texturePath 自定义纹理路径，默认自动拼接
 * @param maxCount 最大堆叠数量，默认 64
 * @param block 可选：生成的作物块（用于种子等）
 */
data class Item(
    val namespace: String,
    val id: String,
    val displayName: String = generateDisplayName(id),
    val texturePath: String = "$id.png",
    val maxCount: Int = 64,
    val block: (FarmArea) -> FarmCropBase? = { null },

    // 附魔属性
    var fortune: Int = 0,  // 时运
) {
    companion object {
        private fun generateDisplayName(id: String): String {
            return id.split("_", "-").joinToString(" ") { it.replaceFirstChar { c -> c.uppercaseChar() } }
        }
    }

    val fullId: String get() = "$namespace:$id"

    val location: ResourceLocation = ResourceLocation(ResourceType.TE_ITEM, namespace, texturePath)

    fun getBlock(farmArea: FarmArea): FarmCropBase? {
        return block(farmArea)
    }
}