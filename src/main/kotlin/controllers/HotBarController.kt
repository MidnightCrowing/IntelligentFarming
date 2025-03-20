package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.HotBar
import com.midnightcrowing.scenes.FarmScene
import kotlin.reflect.full.primaryConstructor

class HotBarController(private val hotBar: HotBar) {
    private val screen: FarmScene = hotBar.screen

    var itemsList: MutableMap<Int, FarmItems?> = mutableMapOf<Int, FarmItems?>().apply {
        for (i in 0..8) {
            val item = when (i) {
                0 -> FarmItems.CabbageItem(hotBar)
                1 -> FarmItems.CabbageSeedItem(hotBar)
                2 -> FarmItems.CarrotItem(hotBar)
                3 -> FarmItems.CornItem(hotBar)
                4 -> FarmItems.CornSeedItem(hotBar)
                5 -> FarmItems.PotatoItem(hotBar)
                6 -> FarmItems.CottonSeedItem(hotBar)
                7 -> FarmItems.WheatSeedItem(hotBar)
                8 -> FarmItems.TomatoSeedItem(hotBar)
                else -> null
            }
            this[i] = item
        }
    }

    fun changeActiveItem(id: Int) {
        val item = itemsList[id]
        if (item?.isSeed == true) {
            val activeSeedCropClass = item.getCrop()
            if (activeSeedCropClass != null) {
                val constructor = activeSeedCropClass.primaryConstructor
                    ?: throw IllegalArgumentException("Class ${activeSeedCropClass.simpleName} has no primary constructor")
                val activeSeedCrop = constructor.call(screen.farmArea)
                screen.farmArea.activeSeedCrop = activeSeedCrop
                return
            }
        }
        screen.farmArea.activeSeedCrop = null
    }
}