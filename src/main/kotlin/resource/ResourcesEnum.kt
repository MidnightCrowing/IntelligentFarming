package com.midnightcrowing.resource

import java.io.InputStream

enum class ResourcesEnum(private val resourcePath: String) {
    /* region GUI */
    INVENTORY("/assets/textures/gui/inventory.png"),
    TOAST("/assets/textures/gui/toast.png"),

    // hot bar
    HOT_BAR("/assets/textures/gui/hot_bar/hot_bar.png"),
    CHECK_BOX("/assets/textures/gui/hot_bar/check_box.png"),

    // trade
    TRADE("/assets/textures/gui/trade/trade.png"),
    TRADE_BUTTON_ABLE_TO_TRADE("/assets/textures/gui/trade/button_able_to_trade.png"),
    TRADE_BUTTON_UNABLE_TO_TRADE("/assets/textures/gui/trade/button_unable_to_trade.png"),
    TRADE_SCROLL_ACTIVE("/assets/textures/gui/trade/scroll_active.png"),
    TRADE_SCROLL_DISABLED("/assets/textures/gui/trade/scroll_disabled.png"),
    TRADE_UNABLE_TO_TRADE("/assets/textures/gui/trade/unable_to_trade.png"),

    // compost
    COMPOST("/assets/textures/gui/compost/compost.png"),
    COMPOST_ARROW("/assets/textures/gui/compost/arrow.png"),

    // button
    BUTTON_DEFAULT("/assets/textures/gui/button/button_default.png"),
    BUTTON_HOVER("/assets/textures/gui/button/button_hover.png"),
    BUTTON_DISABLED("/assets/textures/gui/button/button_disabled.png"),
    /* endregion */

    /* region Font */
    FONT_DEFAULT("/assets/textures/font/unifont-16.0.02.otf"),
    /* endregion */

    /* region Background */
    MAIN_MENU_BACKGROUND("/assets/textures/background/main_menu.jpg"),
    FARM_BACKGROUND("/assets/textures/background/farm.png"),
    /* endregion */

    /* region Farming */
    // Cabbage
    CABBAGE_GROW_0("/assets/textures/farming/cabbage/cabbages0.png"),
    CABBAGE_GROW_1("/assets/textures/farming/cabbage/cabbages1.png"),
    CABBAGE_GROW_2("/assets/textures/farming/cabbage/cabbages2.png"),
    CABBAGE_GROW_3("/assets/textures/farming/cabbage/cabbages3.png"),
    CABBAGE_GROW_4("/assets/textures/farming/cabbage/cabbages4.png"),
    CABBAGE_GROW_5("/assets/textures/farming/cabbage/cabbages5.png"),
    CABBAGE_GROW_6("/assets/textures/farming/cabbage/cabbages6.png"),
    CABBAGE_GROW_7("/assets/textures/farming/cabbage/cabbages7.png"),

    // Carrot
    CARROT_GROW_0("/assets/textures/farming/carrot/carrots0.png"),
    CARROT_GROW_2("/assets/textures/farming/carrot/carrots2.png"),
    CARROT_GROW_4("/assets/textures/farming/carrot/carrots4.png"),
    CARROT_GROW_7("/assets/textures/farming/carrot/carrots7.png"),

    // Corn
    CORN_GROW_0("/assets/textures/farming/corn/corns0.png"),
    CORN_GROW_1("/assets/textures/farming/corn/corns1.png"),
    CORN_GROW_2("/assets/textures/farming/corn/corns2.png"),
    CORN_GROW_3("/assets/textures/farming/corn/corns3.png"),
    CORN_GROW_4("/assets/textures/farming/corn/corns4.png"),
    CORN_GROW_5("/assets/textures/farming/corn/corns5.png"),
    CORN_GROW_6("/assets/textures/farming/corn/corns6.png"),
    CORN_GROW_7("/assets/textures/farming/corn/corns7.png"),

    // Cotton
    COTTON_GROW_0("/assets/textures/farming/cotton/cottons0.png"),
    COTTON_GROW_4("/assets/textures/farming/cotton/cottons4.png"),
    COTTON_GROW_7("/assets/textures/farming/cotton/cottons7.png"),

    // Onion
    ONION_GROW_0("/assets/textures/farming/onion/onions0.png"),
    ONION_GROW_2("/assets/textures/farming/onion/onions2.png"),
    ONION_GROW_4("/assets/textures/farming/onion/onions4.png"),
    ONION_GROW_7("/assets/textures/farming/onion/onions7.png"),

    // Potato
    POTATO_GROW_0("/assets/textures/farming/potato/potatoes0.png"),
    POTATO_GROW_2("/assets/textures/farming/potato/potatoes2.png"),
    POTATO_GROW_4("/assets/textures/farming/potato/potatoes4.png"),
    POTATO_GROW_7("/assets/textures/farming/potato/potatoes7.png"),

    // Tomato
    BUDDING_TOMATO_GROW_0("/assets/textures/farming/tomato/budding_tomatoes0.png"),
    BUDDING_TOMATO_GROW_1("/assets/textures/farming/tomato/budding_tomatoes1.png"),
    BUDDING_TOMATO_GROW_2("/assets/textures/farming/tomato/budding_tomatoes2.png"),
    BUDDING_TOMATO_GROW_3("/assets/textures/farming/tomato/budding_tomatoes3.png"),
    BUDDING_TOMATO_GROW_4("/assets/textures/farming/tomato/budding_tomatoes4.png"),
    TOMATO_GROW_0("/assets/textures/farming/tomato/tomatoes0.png"),
    TOMATO_GROW_1("/assets/textures/farming/tomato/tomatoes1.png"),
    TOMATO_GROW_2("/assets/textures/farming/tomato/tomatoes2.png"),
    TOMATO_GROW_3("/assets/textures/farming/tomato/tomatoes3.png"),

    // Wheat
    WHEAT_GROW_0("/assets/textures/farming/wheat/wheat0.png"),
    WHEAT_GROW_1("/assets/textures/farming/wheat/wheat1.png"),
    WHEAT_GROW_2("/assets/textures/farming/wheat/wheat2.png"),
    WHEAT_GROW_3("/assets/textures/farming/wheat/wheat3.png"),
    WHEAT_GROW_4("/assets/textures/farming/wheat/wheat4.png"),
    WHEAT_GROW_5("/assets/textures/farming/wheat/wheat5.png"),
    WHEAT_GROW_6("/assets/textures/farming/wheat/wheat6.png"),
    WHEAT_GROW_7("/assets/textures/farming/wheat/wheat7.png"),
    /* endregion */

    /* region Item */
    ANVIL("/assets/textures/item/anvil.png"),
    BONE_MEAL("/assets/textures/item/bone_meal.png"),
    CABBAGE("/assets/textures/item/cabbage.png"),
    CABBAGE_SEED("/assets/textures/item/cabbage_seed.png"),
    CARROT("/assets/textures/item/carrot.png"),
    CHEST("/assets/textures/item/chest.png"),
    COMPOSTER("/assets/textures/item/composter.png"),
    CORN("/assets/textures/item/corn.png"),
    CORN_SEED("/assets/textures/item/corn_seed.png"),
    COTTON("/assets/textures/item/cotton.png"),
    COTTON_SEED("/assets/textures/item/cotton_seed.png"),
    DIAMOND_HOE("/assets/textures/item/diamond_hoe.png"),
    EMERALD("/assets/textures/item/emerald.png"),
    EMPTY_SLOT_EMERALD("/assets/textures/item/empty_slot_emerald.png"),
    EMPTY_SLOT_HOE("/assets/textures/item/empty_slot_hoe.png"),
    GOLDEN_CARROT("/assets/textures/item/golden_carrot.png"),
    GOLDEN_HOE("/assets/textures/item/golden_hoe.png"),
    IRON_HOE("/assets/textures/item/iron_hoe.png"),
    NETHERITE_HOE("/assets/textures/item/netherite_hoe.png"),
    ONION("/assets/textures/item/onion.png"),
    POTATO("/assets/textures/item/potato.png"),
    TOMATO("/assets/textures/item/tomato.png"),
    TOMATO_SEED("/assets/textures/item/tomato_seed.png"),
    VILLAGER_SPAWN_EGG("/assets/textures/item/villager_spawn_egg.png"),
    WHEAT("/assets/textures/item/wheat.png"),
    WHEAT_SEED("/assets/textures/item/wheat_seed.png"),
    WRITABLE_BOOK("/assets/textures/item/writable_book.png"),
    /* endregion */

    ;

    val inputStream: InputStream?
        get() = this::class.java.getResourceAsStream(resourcePath)

    val path: String?
        get() = this::class.java.getResource(resourcePath)?.path
}