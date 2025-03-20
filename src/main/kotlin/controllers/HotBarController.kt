package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.HotBar
import com.midnightcrowing.scenes.FarmScene
import kotlin.reflect.full.primaryConstructor

class HotBarController(private val hotBar: HotBar) {
    companion object {
        const val DEFAULT_SELECT_ID = 0 // 默认选中项的ID
    }

    private val screen: FarmScene = hotBar.screen

    var itemsList: MutableMap<Int, FarmItems?> = mutableMapOf<Int, FarmItems?>().apply {
        for (i in 0..8) {
            val item = when (i) {
                0 -> FarmItems.CabbageSeedItem(hotBar)
                1 -> FarmItems.CarrotItem(hotBar)
                2 -> FarmItems.CornSeedItem(hotBar)
                3 -> FarmItems.CottonSeedItem(hotBar)
                4 -> FarmItems.OnionItem(hotBar)
                5 -> FarmItems.PotatoItem(hotBar)
                6 -> FarmItems.WheatSeedItem(hotBar)
                7 -> null
                8 -> FarmItems.TomatoSeedItem(hotBar)
                else -> null
            }
            this[i] = item
        }
    }

    init {
        changeActiveItem(DEFAULT_SELECT_ID)
    }

    fun changeActiveItem(id: Int) {
        val item = itemsList[id]
        hotBar.setItemLabelText(item?.toString())

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