package com.midnightcrowing.controllers

import com.midnightcrowing.farmings.FarmItems
import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.scenes.farmScene.FarmScene
import com.midnightcrowing.model.item.ItemRegistry


class MainMenuController(private val window: Window) {
    init {
        // 游戏初始化方法放在这里
        registerItems()
    }

    private fun registerItems() {
        ItemRegistry.register(FarmItems.CabbageItem.id) { parent -> FarmItems.CabbageItem(parent) }
        ItemRegistry.register(FarmItems.CabbageSeedItem.id) { parent -> FarmItems.CabbageSeedItem(parent) }
        ItemRegistry.register(FarmItems.CarrotItem.id) { parent -> FarmItems.CarrotItem(parent) }
        ItemRegistry.register(FarmItems.GoldenCarrot.id) { parent -> FarmItems.GoldenCarrot(parent) }
        ItemRegistry.register(FarmItems.CornItem.id) { parent -> FarmItems.CornItem(parent) }
        ItemRegistry.register(FarmItems.CornSeedItem.id) { parent -> FarmItems.CornSeedItem(parent) }
        ItemRegistry.register(FarmItems.CottonItem.id) { parent -> FarmItems.CottonItem(parent) }
        ItemRegistry.register(FarmItems.CottonSeedItem.id) { parent -> FarmItems.CottonSeedItem(parent) }
        ItemRegistry.register(FarmItems.OnionItem.id) { parent -> FarmItems.OnionItem(parent) }
        ItemRegistry.register(FarmItems.PotatoItem.id) { parent -> FarmItems.PotatoItem(parent) }
        ItemRegistry.register(FarmItems.TomatoItem.id) { parent -> FarmItems.TomatoItem(parent) }
        ItemRegistry.register(FarmItems.TomatoSeedItem.id) { parent -> FarmItems.TomatoSeedItem(parent) }
        ItemRegistry.register(FarmItems.WheatItem.id) { parent -> FarmItems.WheatItem(parent) }
        ItemRegistry.register(FarmItems.WheatSeedItem.id) { parent -> FarmItems.WheatSeedItem(parent) }
        ItemRegistry.register(FarmItems.Emerald.id) { parent -> FarmItems.Emerald(parent) }
    }

    fun startGame() {
        window.screen = FarmScene(window)
    }

    fun openOptions() {
        println("打开设置界面")
    }

    fun exitGame() {
        window.exit()
    }
}