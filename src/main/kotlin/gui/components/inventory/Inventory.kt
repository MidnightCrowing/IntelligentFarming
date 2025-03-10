package org.intelligentfarming.gui.components.inventory

import org.intelligentfarming.gui.GuiBase
import org.intelligentfarming.gui.Window
import org.intelligentfarming.render.Renderer
import org.intelligentfarming.render.Texture
import org.intelligentfarming.resource.Resources

class Inventory(window: Window) : GuiBase(window) {
    override val renderer: Renderer

    // 计算宽高的一半
    private val halfBaseWidth = 352
    private val halfBaseHeight = 198

    // 向下偏移的距离
    private val offsetY = 0.1f

    // 预计算缩放因子，避免重复计算
    private val scaleFactorX get() = halfBaseWidth / scaleX
    private val scaleFactorY get() = halfBaseHeight / scaleY

    // 实现left，right，top，bottom属性
    override val left get() = -scaleFactorX
    override val right get() = scaleFactorX
    override val top get() = scaleFactorY - offsetY
    override val bottom get() = -scaleFactorY - offsetY

    // 初始化时加载纹理并创建渲染器
    init {
        val texture = Texture(Resources.INVENTORY.path).apply { load() }
        renderer = Renderer(texture) // 子类实现了renderer的赋值
    }
}