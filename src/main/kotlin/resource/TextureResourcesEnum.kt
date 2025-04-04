package com.midnightcrowing.resource

import com.midnightcrowing.model.Texture

enum class TextureResourcesEnum(private val resource: ResourcesEnum) {
    /* region GUI */
    INVENTORY(ResourcesEnum.INVENTORY),
    TOAST(ResourcesEnum.TOAST),

    // hot bar
    HOT_BAR(ResourcesEnum.HOT_BAR),
    CHECK_BOX(ResourcesEnum.CHECK_BOX),

    // trade
    TRADE(ResourcesEnum.TRADE),
    TRADE_BUTTON_ABLE_TO_TRADE(ResourcesEnum.TRADE_BUTTON_ABLE_TO_TRADE),
    TRADE_BUTTON_UNABLE_TO_TRADE(ResourcesEnum.TRADE_BUTTON_UNABLE_TO_TRADE),
    TRADE_SCROLL_ACTIVE(ResourcesEnum.TRADE_SCROLL_ACTIVE),
    TRADE_SCROLL_DISABLED(ResourcesEnum.TRADE_SCROLL_DISABLED),
    TRADE_UNABLE_TO_TRADE(ResourcesEnum.TRADE_UNABLE_TO_TRADE),

    // compost
    COMPOST(ResourcesEnum.COMPOST),
    COMPOST_ARROW(ResourcesEnum.COMPOST_ARROW),

    // button
    BUTTON_DEFAULT(ResourcesEnum.BUTTON_DEFAULT),
    BUTTON_HOVER(ResourcesEnum.BUTTON_HOVER),
    BUTTON_DISABLED(ResourcesEnum.BUTTON_DISABLED),
    /* endregion */

    /* region Background */
    MAIN_MENU_BACKGROUND(ResourcesEnum.MAIN_MENU_BACKGROUND),
    FARM_BACKGROUND(ResourcesEnum.FARM_BACKGROUND),
    /* endregion */

    /* region Farming */
    // Cabbage
    CABBAGE_GROW_0(ResourcesEnum.CABBAGE_GROW_0),
    CABBAGE_GROW_1(ResourcesEnum.CABBAGE_GROW_1),
    CABBAGE_GROW_2(ResourcesEnum.CABBAGE_GROW_2),
    CABBAGE_GROW_3(ResourcesEnum.CABBAGE_GROW_3),
    CABBAGE_GROW_4(ResourcesEnum.CABBAGE_GROW_4),
    CABBAGE_GROW_5(ResourcesEnum.CABBAGE_GROW_5),
    CABBAGE_GROW_6(ResourcesEnum.CABBAGE_GROW_6),
    CABBAGE_GROW_7(ResourcesEnum.CABBAGE_GROW_7),

    // Carrot
    CARROT_GROW_0(ResourcesEnum.CARROT_GROW_0),
    CARROT_GROW_2(ResourcesEnum.CARROT_GROW_2),
    CARROT_GROW_4(ResourcesEnum.CARROT_GROW_4),
    CARROT_GROW_7(ResourcesEnum.CARROT_GROW_7),

    // Corn
    CORN_GROW_0(ResourcesEnum.CORN_GROW_0),
    CORN_GROW_1(ResourcesEnum.CORN_GROW_1),
    CORN_GROW_2(ResourcesEnum.CORN_GROW_2),
    CORN_GROW_3(ResourcesEnum.CORN_GROW_3),
    CORN_GROW_4(ResourcesEnum.CORN_GROW_4),
    CORN_GROW_5(ResourcesEnum.CORN_GROW_5),
    CORN_GROW_6(ResourcesEnum.CORN_GROW_6),
    CORN_GROW_7(ResourcesEnum.CORN_GROW_7),

    // Cotton
    COTTON_GROW_0(ResourcesEnum.COTTON_GROW_0),
    COTTON_GROW_4(ResourcesEnum.COTTON_GROW_4),
    COTTON_GROW_7(ResourcesEnum.COTTON_GROW_7),

    // Onion
    ONION_GROW_0(ResourcesEnum.ONION_GROW_0),
    ONION_GROW_2(ResourcesEnum.ONION_GROW_2),
    ONION_GROW_4(ResourcesEnum.ONION_GROW_4),
    ONION_GROW_7(ResourcesEnum.ONION_GROW_7),

    // Potato
    POTATO_GROW_0(ResourcesEnum.POTATO_GROW_0),
    POTATO_GROW_2(ResourcesEnum.POTATO_GROW_2),
    POTATO_GROW_4(ResourcesEnum.POTATO_GROW_4),
    POTATO_GROW_7(ResourcesEnum.POTATO_GROW_7),

    // Tomato
    BUDDING_TOMATO_GROW_0(ResourcesEnum.BUDDING_TOMATO_GROW_0),
    BUDDING_TOMATO_GROW_1(ResourcesEnum.BUDDING_TOMATO_GROW_1),
    BUDDING_TOMATO_GROW_2(ResourcesEnum.BUDDING_TOMATO_GROW_2),
    BUDDING_TOMATO_GROW_3(ResourcesEnum.BUDDING_TOMATO_GROW_3),
    BUDDING_TOMATO_GROW_4(ResourcesEnum.BUDDING_TOMATO_GROW_4),
    TOMATO_GROW_0(ResourcesEnum.TOMATO_GROW_0),
    TOMATO_GROW_1(ResourcesEnum.TOMATO_GROW_1),
    TOMATO_GROW_2(ResourcesEnum.TOMATO_GROW_2),
    TOMATO_GROW_3(ResourcesEnum.TOMATO_GROW_3),

    // Wheat
    WHEAT_GROW_0(ResourcesEnum.WHEAT_GROW_0),
    WHEAT_GROW_1(ResourcesEnum.WHEAT_GROW_1),
    WHEAT_GROW_2(ResourcesEnum.WHEAT_GROW_2),
    WHEAT_GROW_3(ResourcesEnum.WHEAT_GROW_3),
    WHEAT_GROW_4(ResourcesEnum.WHEAT_GROW_4),
    WHEAT_GROW_5(ResourcesEnum.WHEAT_GROW_5),
    WHEAT_GROW_6(ResourcesEnum.WHEAT_GROW_6),
    WHEAT_GROW_7(ResourcesEnum.WHEAT_GROW_7),
    /* endregion */

    /* region Item */
    ANVIL(ResourcesEnum.ANVIL),
    BONE_MEAL(ResourcesEnum.BONE_MEAL),
    CABBAGE(ResourcesEnum.CABBAGE),
    CABBAGE_SEED(ResourcesEnum.CABBAGE_SEED),
    CARROT(ResourcesEnum.CARROT),
    CHEST(ResourcesEnum.CHEST),
    COMPOSTER(ResourcesEnum.COMPOSTER),
    CORN(ResourcesEnum.CORN),
    CORN_SEED(ResourcesEnum.CORN_SEED),
    COTTON(ResourcesEnum.COTTON),
    COTTON_SEED(ResourcesEnum.COTTON_SEED),
    DIAMOND_HOE(ResourcesEnum.DIAMOND_HOE),
    EMERALD(ResourcesEnum.EMERALD),
    EMPTY_SLOT_EMERALD(ResourcesEnum.EMPTY_SLOT_EMERALD),
    EMPTY_SLOT_HOE(ResourcesEnum.EMPTY_SLOT_HOE),
    GOLDEN_CARROT(ResourcesEnum.GOLDEN_CARROT),
    GOLDEN_HOE(ResourcesEnum.GOLDEN_HOE),
    IRON_HOE(ResourcesEnum.IRON_HOE),
    NETHERITE_HOE(ResourcesEnum.NETHERITE_HOE),
    ONION(ResourcesEnum.ONION),
    POTATO(ResourcesEnum.POTATO),
    TOMATO(ResourcesEnum.TOMATO),
    TOMATO_SEED(ResourcesEnum.TOMATO_SEED),
    VILLAGER_SPAWN_EGG(ResourcesEnum.VILLAGER_SPAWN_EGG),
    WHEAT(ResourcesEnum.WHEAT),
    WHEAT_SEED(ResourcesEnum.WHEAT_SEED),
    WRITABLE_BOOK(ResourcesEnum.WRITABLE_BOOK),
    /* endregion */

    ;

    /** 延迟加载纹理，避免初始化时占用太多资源 */
    val texture: Texture by lazy { Texture(resource.inputStream!!).apply { load() } }
}
