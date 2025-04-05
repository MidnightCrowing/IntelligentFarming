package com.midnightcrowing.model.item

import com.midnightcrowing.farmings.crops.*
import com.midnightcrowing.resource.TextureResourcesEnum as Enum

// 集中管理所有物品
object Items {
    val CABBAGE = Item("minecraft:cabbage", "卷心菜", Enum.CABBAGE)
    val CABBAGE_SEED = Item("minecraft:cabbage_seed", "卷心菜种子", Enum.CABBAGE_SEED, block = { Cabbage(it) })
    val CARROT = Item("minecraft:carrot", "胡萝卜", Enum.CARROT, block = { Carrot(it) })
    val CORN = Item("minecraft:corn", "玉米", Enum.CORN)
    val CORN_SEED = Item("minecraft:corn_seed", "玉米种子", Enum.CORN_SEED, block = { Corn(it) })
    val COTTON = Item("minecraft:cotton", "棉花", Enum.COTTON)
    val COTTON_SEED = Item("minecraft:cotton_seed", "棉花种子", Enum.COTTON_SEED, block = { Cotton(it) })
    val ONION = Item("minecraft:onion", "洋葱", Enum.ONION, block = { Onion(it) })
    val POTATO = Item("minecraft:potato", "土豆", Enum.POTATO, block = { Potato(it) })
    val TOMATO = Item("minecraft:tomato", "西红柿", Enum.TOMATO)
    val TOMATO_SEED = Item("minecraft:tomato_seed", "西红柿种子", Enum.TOMATO_SEED, block = { Tomato(it) })
    val WHEAT = Item("minecraft:wheat", "小麦", Enum.WHEAT)
    val WHEAT_SEED = Item("minecraft:wheat_seed", "小麦种子", Enum.WHEAT_SEED, block = { Wheat(it) })
    val ANVIL = Item("minecraft:anvil", "铁砧", Enum.ANVIL)
    val BONE_MEAL = Item("minecraft:bone_meal", "骨粉", Enum.BONE_MEAL)
    val CHEST = Item("minecraft:chest", "箱子", Enum.CHEST)
    val COMPOSTER = Item("minecraft:composter", "堆肥桶", Enum.COMPOSTER)
    val DIAMOND_HOE = Item("minecraft:diamond_hoe", "钻石锄", Enum.DIAMOND_HOE, maxCount = 1)
    val EMERALD = Item("minecraft:emerald", "绿宝石", Enum.EMERALD)
    val GOLDEN_CARROT = Item("minecraft:golden_carrot", "金胡萝卜", Enum.GOLDEN_CARROT)
    val GOLDEN_HOE = Item("minecraft:golden_hoe", "金锄", Enum.GOLDEN_HOE, maxCount = 1)
    val IRON_HOE = Item("minecraft:iron_hoe", "铁锄", Enum.IRON_HOE, maxCount = 1)
    val NETHERITE_HOE = Item("minecraft:netherite_hoe", "下界合金锄", Enum.NETHERITE_HOE, maxCount = 1)
    val VILLAGER_SPAWN_EGG = Item("minecraft:villager_spawn_egg", "村民刷怪蛋", Enum.VILLAGER_SPAWN_EGG)
    val WRITABLE_BOOK = Item("minecraft:writable_book", "书与笔", Enum.WRITABLE_BOOK)

    val all: List<Item> = listOf(
        CABBAGE, CABBAGE_SEED, CARROT, CORN, CORN_SEED, COTTON, COTTON_SEED, ONION, POTATO, TOMATO, TOMATO_SEED,
        WHEAT, WHEAT_SEED, ANVIL, BONE_MEAL, CHEST, COMPOSTER, DIAMOND_HOE, EMERALD, GOLDEN_CARROT, GOLDEN_HOE,
        IRON_HOE, NETHERITE_HOE, VILLAGER_SPAWN_EGG, WRITABLE_BOOK
    )

    fun registerAll() {
        all.forEach { ItemRegistry.register(it) }
    }
}