package com.midnightcrowing.model.block

import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.resource.ResourceLocation

class Composter(parent: Widget) : Block(parent) {
    private val levelList: Map<Int, ResourceLocation> = indexedResourceMap(
        "composter/composter",
        listOf(0, 0, 0, 0, 4, 5, 6, 7, 8),
    )
    var compostLevel: Int = 0
        set(value) {
            field = value % 9
            renderer.location = levelList.getValue(field)
        }

    init {
        compostLevel = 0
    }
}