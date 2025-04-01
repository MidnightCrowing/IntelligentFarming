package com.midnightcrowing.resource

import java.io.InputStream

enum class ResourcesEnum(private val resourcePath: String) {
    /* region GUI */
    INVENTORY("/assets/gui/inventory.png"),
    TOAST("/assets/gui/toast.png"),

    // hot bar
    HOT_BAR("/assets/gui/hot_bar/hot_bar.png"),
    CHECK_BOX("/assets/gui/hot_bar/check_box.png"),

    // trade
    TRADE("/assets/gui/trade/trade.png"),
    TRADE_BUTTON_ABLE_TO_TRADE("/assets/gui/trade/button_able_to_trade.png"),
    TRADE_BUTTON_UNABLE_TO_TRADE("/assets/gui/trade/button_unable_to_trade.png"),
    TRADE_SCROLL_ACTIVE("/assets/gui/trade/scroll_active.png"),
    TRADE_SCROLL_DISABLED("/assets/gui/trade/scroll_disabled.png"),
    TRADE_UNABLE_TO_TRADE("/assets/gui/trade/unable_to_trade.png"),

    // button
    BUTTON_DEFAULT("/assets/gui/button/button_default.png"),
    BUTTON_HOVER("/assets/gui/button/button_hover.png"),
    BUTTON_DISABLED("/assets/gui/button/button_disabled.png"),
    /* endregion */

    /* region Font */
    FONT_DEFAULT("/assets/font/unifont-16.0.02.otf"),
    /* endregion */

    /* region Background */
    MAIN_MENU_BACKGROUND("/assets/background/main_menu.jpg"),
    FARM_BACKGROUND("/assets/background/farm.png"),
    /* endregion */

    /* region Farming */
    // Cabbage
    CABBAGE("/assets/farming/cabbage/cabbage.png"),
    CABBAGE_SEED("/assets/farming/cabbage/cabbage_seed.png"),
    CABBAGE_GROW_0("/assets/farming/cabbage/cabbages0.png"),
    CABBAGE_GROW_1("/assets/farming/cabbage/cabbages1.png"),
    CABBAGE_GROW_2("/assets/farming/cabbage/cabbages2.png"),
    CABBAGE_GROW_3("/assets/farming/cabbage/cabbages3.png"),
    CABBAGE_GROW_4("/assets/farming/cabbage/cabbages4.png"),
    CABBAGE_GROW_5("/assets/farming/cabbage/cabbages5.png"),
    CABBAGE_GROW_6("/assets/farming/cabbage/cabbages6.png"),
    CABBAGE_GROW_7("/assets/farming/cabbage/cabbages7.png"),

    // Carrot
    CARROT("/assets/farming/carrot/carrot.png"),
    CARROT_GROW_0("/assets/farming/carrot/carrots0.png"),
    CARROT_GROW_2("/assets/farming/carrot/carrots2.png"),
    CARROT_GROW_4("/assets/farming/carrot/carrots4.png"),
    CARROT_GROW_7("/assets/farming/carrot/carrots7.png"),
    GOLDEN_CARROT("/assets/farming/carrot/golden_carrot.png"),

    // Corn
    CORN("/assets/farming/corn/corn.png"),
    CORN_SEED("/assets/farming/corn/corn_seed.png"),
    CORN_GROW_0("/assets/farming/corn/corns0.png"),
    CORN_GROW_1("/assets/farming/corn/corns1.png"),
    CORN_GROW_2("/assets/farming/corn/corns2.png"),
    CORN_GROW_3("/assets/farming/corn/corns3.png"),
    CORN_GROW_4("/assets/farming/corn/corns4.png"),
    CORN_GROW_5("/assets/farming/corn/corns5.png"),
    CORN_GROW_6("/assets/farming/corn/corns6.png"),
    CORN_GROW_7("/assets/farming/corn/corns7.png"),

    // Cotton
    COTTON("/assets/farming/cotton/cotton.png"),
    COTTON_SEED("/assets/farming/cotton/cotton_seed.png"),
    COTTON_GROW_0("/assets/farming/cotton/cottons0.png"),
    COTTON_GROW_4("/assets/farming/cotton/cottons4.png"),
    COTTON_GROW_7("/assets/farming/cotton/cottons7.png"),

    // Onion
    ONION("/assets/farming/onion/onion.png"),
    ONION_GROW_0("/assets/farming/onion/onions0.png"),
    ONION_GROW_2("/assets/farming/onion/onions2.png"),
    ONION_GROW_4("/assets/farming/onion/onions4.png"),
    ONION_GROW_7("/assets/farming/onion/onions7.png"),

    // Potato
    POTATO("/assets/farming/potato/potato.png"),
    POTATO_GROW_0("/assets/farming/potato/potatoes0.png"),
    POTATO_GROW_2("/assets/farming/potato/potatoes2.png"),
    POTATO_GROW_4("/assets/farming/potato/potatoes4.png"),
    POTATO_GROW_7("/assets/farming/potato/potatoes7.png"),

    // Tomato
    TOMATO("/assets/farming/tomato/tomato.png"),
    TOMATO_SEED("/assets/farming/tomato/tomato_seed.png"),
    BUDDING_TOMATO_GROW_0("/assets/farming/tomato/budding_tomatoes0.png"),
    BUDDING_TOMATO_GROW_1("/assets/farming/tomato/budding_tomatoes1.png"),
    BUDDING_TOMATO_GROW_2("/assets/farming/tomato/budding_tomatoes2.png"),
    BUDDING_TOMATO_GROW_3("/assets/farming/tomato/budding_tomatoes3.png"),
    BUDDING_TOMATO_GROW_4("/assets/farming/tomato/budding_tomatoes4.png"),
    TOMATO_GROW_0("/assets/farming/tomato/tomatoes0.png"),
    TOMATO_GROW_1("/assets/farming/tomato/tomatoes1.png"),
    TOMATO_GROW_2("/assets/farming/tomato/tomatoes2.png"),
    TOMATO_GROW_3("/assets/farming/tomato/tomatoes3.png"),

    // Wheat
    WHEAT("/assets/farming/wheat/wheat.png"),
    WHEAT_SEED("/assets/farming/wheat/wheat_seed.png"),
    WHEAT_GROW_0("/assets/farming/wheat/wheat0.png"),
    WHEAT_GROW_1("/assets/farming/wheat/wheat1.png"),
    WHEAT_GROW_2("/assets/farming/wheat/wheat2.png"),
    WHEAT_GROW_3("/assets/farming/wheat/wheat3.png"),
    WHEAT_GROW_4("/assets/farming/wheat/wheat4.png"),
    WHEAT_GROW_5("/assets/farming/wheat/wheat5.png"),
    WHEAT_GROW_6("/assets/farming/wheat/wheat6.png"),
    WHEAT_GROW_7("/assets/farming/wheat/wheat7.png");
    /* endregion */

    val inputStream: InputStream?
        get() = this::class.java.getResourceAsStream(resourcePath)

    val path: String?
        get() = this::class.java.getResource(resourcePath)?.path
}