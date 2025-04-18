package com.midnightcrowing.resource

data class ResourceLocation(val namespace: String, val path: String) {
    constructor(full: String) : this(
        full.substringBefore(":", "minecraft"),
        full.substringAfter(":", "")
    )

    constructor(type: ResourceType, namespace: String, relativePath: String) : this(
        namespace,
        "${type.folder}/$relativePath"
    )

    override fun toString(): String = "$namespace:$path"

    /**
     * 获取完整路径，例如：/assets/mygame/item/carrot.png
     */
    fun toAssetPath(): String = "/${ResourceType.ROOT.folder}/$namespace/$path"
}