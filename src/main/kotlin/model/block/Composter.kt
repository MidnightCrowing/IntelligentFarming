package com.midnightcrowing.model.block

import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.Texture
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum

class Composter(parent: Widget) : Block(parent) {
    override val renderer: TextureRenderer = TextureRenderer()
    private val levelList: Map<Int, Texture> = mapOf(
        0 to TextureResourcesEnum.BLOCK_COMPOSTER_0.texture,
        1 to TextureResourcesEnum.BLOCK_COMPOSTER_0.texture,
        2 to TextureResourcesEnum.BLOCK_COMPOSTER_0.texture,
        3 to TextureResourcesEnum.BLOCK_COMPOSTER_0.texture,
        4 to TextureResourcesEnum.BLOCK_COMPOSTER_4.texture,
        5 to TextureResourcesEnum.BLOCK_COMPOSTER_5.texture,
        6 to TextureResourcesEnum.BLOCK_COMPOSTER_6.texture,
        7 to TextureResourcesEnum.BLOCK_COMPOSTER_7.texture,
        8 to TextureResourcesEnum.BLOCK_COMPOSTER_8.texture,
    )
    var compostLevel: Int = 0
        set(value) {
            field = value % 9
            renderer.texture = levelList.getValue(field)
        }

    init {
        compostLevel = 0
    }
}