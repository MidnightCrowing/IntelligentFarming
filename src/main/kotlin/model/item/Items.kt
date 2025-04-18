package com.midnightcrowing.model.item

import com.midnightcrowing.farmings.crops.*

// 集中管理所有物品
object Items {
    // ===== 作物类 =====
    val CABBAGE = Item(
        namespace = "farmersdelight",
        id = "cabbage",
        displayName = "卷心菜",
        texturePath = "cabbage.png"
    )
    val CARROT = Item(
        namespace = "minecraft",
        id = "carrot",
        displayName = "胡萝卜",
        texturePath = "carrot.png",
        block = { Carrot(it) }
    )
    val CORN = Item(
        namespace = "corn_delight",
        id = "corn",
        displayName = "玉米",
        texturePath = "corn.png"
    )
    val COTTON = Item(
        namespace = "cotton",
        id = "cotton",
        displayName = "棉花",
        texturePath = "cotton.png"
    )
    val ONION = Item(
        namespace = "farmersdelight",
        id = "onion",
        displayName = "洋葱",
        texturePath = "onion.png",
        block = { Onion(it) })
    val POTATO = Item(
        namespace = "minecraft",
        id = "potato",
        displayName = "土豆",
        texturePath = "potato.png",
        block = { Potato(it) })
    val TOMATO = Item(
        namespace = "farmersdelight",
        id = "tomato",
        displayName = "西红柿",
        texturePath = "tomato.png"
    )
    val WHEAT = Item(
        namespace = "minecraft",
        id = "wheat",
        displayName = "小麦",
        texturePath = "wheat.png"
    )

    // ===== 种子类 =====
    val CABBAGE_SEED = Item(
        namespace = "farmersdelight",
        id = "cabbage_seed",
        displayName = "卷心菜种子",
        texturePath = "cabbage_seed.png",
        block = { Cabbage(it) })
    val CORN_SEED = Item(
        namespace = "corn_delight",
        id = "corn_seed",
        displayName = "玉米种子",
        texturePath = "corn_seed.png",
        block = { Corn(it) })
    val COTTON_SEED = Item(
        namespace = "cotton",
        id = "cotton_seed",
        displayName = "棉花种子",
        texturePath = "cotton_seed.png",
        block = { Cotton(it) })
    val TOMATO_SEED = Item(
        namespace = "farmersdelight",
        id = "tomato_seed",
        displayName = "西红柿种子",
        texturePath = "tomato_seed.png",
        block = { Tomato(it) })
    val WHEAT_SEED = Item(
        namespace = "minecraft",
        id = "wheat_seed",
        displayName = "小麦种子",
        texturePath = "wheat_seed.png",
        block = { Wheat(it) })

    // ===== 工具类 =====
    val IRON_HOE = Item(
        namespace = "minecraft",
        id = "iron_hoe",
        displayName = "铁锄",
        texturePath = "iron_hoe.png",
        maxCount = 1,
        fortune = 0
    )
    val GOLDEN_HOE = Item(
        namespace = "minecraft",
        id = "golden_hoe",
        displayName = "金锄",
        texturePath = "golden_hoe.png",
        maxCount = 1,
        fortune = 1
    )
    val DIAMOND_HOE = Item(
        namespace = "minecraft",
        id = "diamond_hoe",
        displayName = "钻石锄",
        texturePath = "diamond_hoe.png",
        maxCount = 1,
        fortune = 2
    )
    val NETHERITE_HOE = Item(
        namespace = "minecraft",
        id = "netherite_hoe",
        displayName = "下界合金锄",
        texturePath = "netherite_hoe.png",
        maxCount = 1,
        fortune = 3
    )

    // ===== 容器类 =====
    val ANVIL = Item(
        namespace = "minecraft",
        id = "anvil",
        displayName = "铁砧",
        texturePath = "anvil.png"
    )
    val CHEST = Item(
        namespace = "minecraft",
        id = "chest",
        displayName = "箱子",
        texturePath = "chest.png"
    )
    val COMPOSTER = Item(
        namespace = "minecraft",
        id = "composter",
        displayName = "堆肥桶",
        texturePath = "composter.png"
    )

    // ===== 材料类 =====
    val EMERALD = Item(
        namespace = "minecraft",
        id = "emerald",
        displayName = "绿宝石",
        texturePath = "emerald.png"
    )
    val BONE_MEAL = Item(
        namespace = "minecraft",
        id = "bone_meal",
        displayName = "骨粉",
        texturePath = "bone_meal.png"
    )
    val GOLDEN_CARROT = Item(
        namespace = "minecraft",
        id = "golden_carrot",
        displayName = "金胡萝卜",
        texturePath = "golden_carrot.png"
    )

    // ===== 特殊类 =====
    val WRITABLE_BOOK = Item(
        namespace = "minecraft",
        id = "writable_book",
        displayName = "书与笔",
        texturePath = "writable_book.png"
    )
    val VILLAGER_SPAWN_EGG = Item(
        namespace = "minecraft",
        id = "villager_spawn_egg",
        displayName = "村民刷怪蛋",
        texturePath = "villager_spawn_egg.png"
    )


    private val all: List<Item> = listOf(
        CABBAGE,
        CABBAGE_SEED,
        CARROT,
        CORN,
        CORN_SEED,
        COTTON,
        COTTON_SEED,
        ONION,
        POTATO,
        TOMATO,
        TOMATO_SEED,
        WHEAT,
        WHEAT_SEED,
        ANVIL,
        BONE_MEAL,
        CHEST,
        COMPOSTER,
        DIAMOND_HOE,
        EMERALD,
        GOLDEN_CARROT,
        GOLDEN_HOE,
        IRON_HOE,
        NETHERITE_HOE,
        VILLAGER_SPAWN_EGG,
        WRITABLE_BOOK
    )

    fun registerAll() {
        all.forEach { ItemRegistry.register(it) }
    }
}