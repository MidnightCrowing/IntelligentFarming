package com.midnightcrowing.model.block

import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.resource.ResourceType

open class Block(val parent: Widget) {
    protected open val renderer: TextureRenderer = TextureRenderer()
    private var visible: Boolean = true
    var bounds: ScreenBounds = ScreenBounds.EMPTY
        private set

    val isVisible: Boolean get() = visible && parent.isVisible

    open fun update() {}

    open fun setHidden(value: Boolean) {
        visible = !value
    }

    open fun place(bounds: ScreenBounds) {
        this.bounds = bounds
    }

    /**
     * 渲染组件
     */
    open fun render() {
        if (!isVisible) return
        renderer.render(bounds)
    }

    /**
     * 根据传入的索引列表生成 `<Int, ResourceLocation>` 映射，适用于阶段性资源加载，如作物生长贴图等。
     *
     * @param path 资源路径前缀（不包含文件扩展名和阶段编号），如 "carrot/carrots"
     * @param stages 阶段编号列表，每个元素会用于构造贴图路径
     * @param extension 文件扩展名，默认为 ".png"
     * @param namespace 命名空间，默认为 "minecraft"
     * @param type 资源类型，例如 [ResourceType.TE_BLOCK] 或 [ResourceType.TE_ITEM]
     *
     * @return 一个以阶段索引为键、[ResourceLocation] 为值的映射，用于贴图加载
     *
     * 示例用法：
     * ```kotlin
     * val textures = indexedResourceMap(
     *     path = "carrot/carrots",
     *     stages = listOf(0, 0, 2, 2, 4, 4, 4, 7)
     * )
     * // 结果为：
     * // 0 -> carrot/carrots0.png
     * // 1 -> carrot/carrots0.png
     * // 2 -> carrot/carrots2.png
     * // ...
     * ```
     */
    protected fun indexedResourceMap(
        path: String,
        stages: List<Int>,
        extension: String = ".png",
        namespace: String = "minecraft",
        type: ResourceType = ResourceType.TE_BLOCK,
    ): Map<Int, ResourceLocation> =
        stages.mapIndexed { index, texIndex ->
            index to ResourceLocation(type, namespace, "$path$texIndex$extension")
        }.toMap()
}