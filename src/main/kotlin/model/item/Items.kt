package com.midnightcrowing.model.item

import com.midnightcrowing.farmings.crops.*
import com.midnightcrowing.resource.TextureResourcesEnum as Enum

// 集中管理所有物品
object Items {
    // @formatter:off

    // ===== 作物类 =====
    val CABBAGE      = Item("minecraft:cabbage",      "卷心菜",   Enum.ITEM_CABBAGE)
    val CARROT       = Item("minecraft:carrot",       "胡萝卜",   Enum.ITEM_CARROT,  block = { Carrot(it) })
    val CORN         = Item("minecraft:corn",         "玉米",     Enum.ITEM_CORN)
    val COTTON       = Item("minecraft:cotton",       "棉花",     Enum.ITEM_COTTON)
    val ONION        = Item("minecraft:onion",        "洋葱",     Enum.ITEM_ONION,   block = { Onion(it) })
    val POTATO       = Item("minecraft:potato",       "土豆",     Enum.ITEM_POTATO,  block = { Potato(it) })
    val TOMATO       = Item("minecraft:tomato",       "西红柿",   Enum.ITEM_TOMATO)
    val WHEAT        = Item("minecraft:wheat",        "小麦",     Enum.ITEM_WHEAT)

    // ===== 种子类 =====
    val CABBAGE_SEED = Item("minecraft:cabbage_seed", "卷心菜种子", Enum.ITEM_CABBAGE_SEED,  block = { Cabbage(it) })
    val CORN_SEED    = Item("minecraft:corn_seed",    "玉米种子",   Enum.ITEM_CORN_SEED,     block = { Corn(it) })
    val COTTON_SEED  = Item("minecraft:cotton_seed",  "棉花种子",   Enum.ITEM_COTTON_SEED,   block = { Cotton(it) })
    val TOMATO_SEED  = Item("minecraft:tomato_seed",  "西红柿种子", Enum.ITEM_TOMATO_SEED,   block = { Tomato(it) })
    val WHEAT_SEED   = Item("minecraft:wheat_seed",   "小麦种子",   Enum.ITEM_WHEAT_SEED,    block = { Wheat(it) })

    // ===== 工具类 =====
    val IRON_HOE      = Item("minecraft:iron_hoe",      "铁锄",     Enum.ITEM_IRON_HOE, maxCount = 1, fortune = 0)
    val GOLDEN_HOE    = Item("minecraft:golden_hoe",    "金锄",     Enum.ITEM_GOLDEN_HOE, maxCount = 1, fortune = 1)
    val DIAMOND_HOE   = Item("minecraft:diamond_hoe",   "钻石锄",    Enum.ITEM_DIAMOND_HOE, maxCount = 1, fortune = 2)
    val NETHERITE_HOE = Item("minecraft:netherite_hoe", "下界合金锄", Enum.ITEM_NETHERITE_HOE, maxCount = 1, fortune = 3)

    // ===== 容器类 =====
    val ANVIL     = Item("minecraft:anvil",     "铁砧",   Enum.ITEM_ANVIL)
    val CHEST     = Item("minecraft:chest",     "箱子",   Enum.ITEM_CHEST)
    val COMPOSTER = Item("minecraft:composter", "堆肥桶", Enum.ITEM_COMPOSTER)

    // ===== 材料类 =====
    val EMERALD       = Item("minecraft:emerald",       "绿宝石",  Enum.ITEM_EMERALD)
    val BONE_MEAL     = Item("minecraft:bone_meal",     "骨粉",    Enum.ITEM_BONE_MEAL)
    val GOLDEN_CARROT = Item("minecraft:golden_carrot", "金胡萝卜", Enum.ITEM_GOLDEN_CARROT)

    // ===== 特殊类 =====
    val WRITABLE_BOOK       = Item("minecraft:writable_book",       "书与笔",      Enum.ITEM_WRITABLE_BOOK)
    val VILLAGER_SPAWN_EGG  = Item("minecraft:villager_spawn_egg",  "村民刷怪蛋",   Enum.ITEM_VILLAGER_SPAWN_EGG)

    // @formatter:on

    private val all: List<Item> = listOf(
        CABBAGE, CABBAGE_SEED, CARROT, CORN, CORN_SEED, COTTON, COTTON_SEED, ONION, POTATO, TOMATO, TOMATO_SEED,
        WHEAT, WHEAT_SEED, ANVIL, BONE_MEAL, CHEST, COMPOSTER, DIAMOND_HOE, EMERALD, GOLDEN_CARROT, GOLDEN_HOE,
        IRON_HOE, NETHERITE_HOE, VILLAGER_SPAWN_EGG, WRITABLE_BOOK
    )

    fun registerAll() {
        all.forEach { ItemRegistry.register(it) }
    }
}