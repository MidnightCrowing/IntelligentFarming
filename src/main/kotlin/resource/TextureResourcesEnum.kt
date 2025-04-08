package com.midnightcrowing.resource

import com.midnightcrowing.model.Texture
import java.io.InputStream

enum class TextureResourcesEnum(private val resourcePath: String) {
    TITLE_INTELLFARM("/assets/textures/title/intellfarm.png"),

    /* region GUI */
    GUI_INVENTORY("/assets/textures/gui/inventory.png"),
    GUI_TOAST("/assets/textures/gui/toast.png"),

    // hot bar
    GUI_HOT_BAR("/assets/textures/gui/hot_bar/hot_bar.png"),
    GUI_CHECK_BOX("/assets/textures/gui/hot_bar/check_box.png"),

    // trade
    GUI_TRADE("/assets/textures/gui/trade/trade.png"),
    GUI_TRADE_BUTTON_ABLE_TO_TRADE("/assets/textures/gui/trade/button_able_to_trade.png"),
    GUI_TRADE_BUTTON_UNABLE_TO_TRADE("/assets/textures/gui/trade/button_unable_to_trade.png"),
    GUI_TRADE_SCROLL_ACTIVE("/assets/textures/gui/trade/scroll_active.png"),
    GUI_TRADE_SCROLL_DISABLED("/assets/textures/gui/trade/scroll_disabled.png"),
    GUI_TRADE_UNABLE_TO_TRADE("/assets/textures/gui/trade/unable_to_trade.png"),

    // compost
    GUI_COMPOST("/assets/textures/gui/compost/compost.png"),
    GUI_COMPOST_ARROW("/assets/textures/gui/compost/arrow.png"),

    // button
    GUI_BUTTON_DEFAULT("/assets/textures/gui/button/button_default.png"),
    GUI_BUTTON_HOVER("/assets/textures/gui/button/button_hover.png"),
    GUI_BUTTON_DISABLED("/assets/textures/gui/button/button_disabled.png"),
    /* endregion */

    /* region Background */
    BG_MAIN_MENU_BACKGROUND("/assets/textures/background/main_menu.jpg"),
    BG_FARM_BACKGROUND("/assets/textures/background/farm.png"),
    /* endregion */

    /* region Block */
    // Cabbage
    BLOCK_CABBAGE_GROW_0("/assets/textures/farming/cabbage/cabbages0.png"),
    BLOCK_CABBAGE_GROW_1("/assets/textures/farming/cabbage/cabbages1.png"),
    BLOCK_CABBAGE_GROW_2("/assets/textures/farming/cabbage/cabbages2.png"),
    BLOCK_CABBAGE_GROW_3("/assets/textures/farming/cabbage/cabbages3.png"),
    BLOCK_CABBAGE_GROW_4("/assets/textures/farming/cabbage/cabbages4.png"),
    BLOCK_CABBAGE_GROW_5("/assets/textures/farming/cabbage/cabbages5.png"),
    BLOCK_CABBAGE_GROW_6("/assets/textures/farming/cabbage/cabbages6.png"),
    BLOCK_CABBAGE_GROW_7("/assets/textures/farming/cabbage/cabbages7.png"),

    // Carrot
    BLOCK_CARROT_GROW_0("/assets/textures/farming/carrot/carrots0.png"),
    BLOCK_CARROT_GROW_2("/assets/textures/farming/carrot/carrots2.png"),
    BLOCK_CARROT_GROW_4("/assets/textures/farming/carrot/carrots4.png"),
    BLOCK_CARROT_GROW_7("/assets/textures/farming/carrot/carrots7.png"),

    // Corn
    BLOCK_CORN_GROW_0("/assets/textures/farming/corn/corns0.png"),
    BLOCK_CORN_GROW_1("/assets/textures/farming/corn/corns1.png"),
    BLOCK_CORN_GROW_2("/assets/textures/farming/corn/corns2.png"),
    BLOCK_CORN_GROW_3("/assets/textures/farming/corn/corns3.png"),
    BLOCK_CORN_GROW_4("/assets/textures/farming/corn/corns4.png"),
    BLOCK_CORN_GROW_5("/assets/textures/farming/corn/corns5.png"),
    BLOCK_CORN_GROW_6("/assets/textures/farming/corn/corns6.png"),
    BLOCK_CORN_GROW_7("/assets/textures/farming/corn/corns7.png"),

    // Cotton
    BLOCK_COTTON_GROW_0("/assets/textures/farming/cotton/cottons0.png"),
    BLOCK_COTTON_GROW_4("/assets/textures/farming/cotton/cottons4.png"),
    BLOCK_COTTON_GROW_7("/assets/textures/farming/cotton/cottons7.png"),

    // Onion
    BLOCK_ONION_GROW_0("/assets/textures/farming/onion/onions0.png"),
    BLOCK_ONION_GROW_2("/assets/textures/farming/onion/onions2.png"),
    BLOCK_ONION_GROW_4("/assets/textures/farming/onion/onions4.png"),
    BLOCK_ONION_GROW_7("/assets/textures/farming/onion/onions7.png"),

    // Potato
    BLOCK_POTATO_GROW_0("/assets/textures/farming/potato/potatoes0.png"),
    BLOCK_POTATO_GROW_2("/assets/textures/farming/potato/potatoes2.png"),
    BLOCK_POTATO_GROW_4("/assets/textures/farming/potato/potatoes4.png"),
    BLOCK_POTATO_GROW_7("/assets/textures/farming/potato/potatoes7.png"),

    // Tomato
    BLOCK_BUDDING_TOMATO_GROW_0("/assets/textures/farming/tomato/budding_tomatoes0.png"),
    BLOCK_BUDDING_TOMATO_GROW_1("/assets/textures/farming/tomato/budding_tomatoes1.png"),
    BLOCK_BUDDING_TOMATO_GROW_2("/assets/textures/farming/tomato/budding_tomatoes2.png"),
    BLOCK_BUDDING_TOMATO_GROW_3("/assets/textures/farming/tomato/budding_tomatoes3.png"),
    BLOCK_BUDDING_TOMATO_GROW_4("/assets/textures/farming/tomato/budding_tomatoes4.png"),
    BLOCK_TOMATO_GROW_0("/assets/textures/farming/tomato/tomatoes0.png"),
    BLOCK_TOMATO_GROW_1("/assets/textures/farming/tomato/tomatoes1.png"),
    BLOCK_TOMATO_GROW_2("/assets/textures/farming/tomato/tomatoes2.png"),
    BLOCK_TOMATO_GROW_3("/assets/textures/farming/tomato/tomatoes3.png"),

    // Wheat
    BLOCK_WHEAT_GROW_0("/assets/textures/farming/wheat/wheat0.png"),
    BLOCK_WHEAT_GROW_1("/assets/textures/farming/wheat/wheat1.png"),
    BLOCK_WHEAT_GROW_2("/assets/textures/farming/wheat/wheat2.png"),
    BLOCK_WHEAT_GROW_3("/assets/textures/farming/wheat/wheat3.png"),
    BLOCK_WHEAT_GROW_4("/assets/textures/farming/wheat/wheat4.png"),
    BLOCK_WHEAT_GROW_5("/assets/textures/farming/wheat/wheat5.png"),
    BLOCK_WHEAT_GROW_6("/assets/textures/farming/wheat/wheat6.png"),
    BLOCK_WHEAT_GROW_7("/assets/textures/farming/wheat/wheat7.png"),

    // Composter
    BLOCK_COMPOSTER_0("/assets/textures/block/composter/composter0.png"),
    BLOCK_COMPOSTER_4("/assets/textures/block/composter/composter4.png"),
    BLOCK_COMPOSTER_5("/assets/textures/block/composter/composter5.png"),
    BLOCK_COMPOSTER_6("/assets/textures/block/composter/composter6.png"),
    BLOCK_COMPOSTER_7("/assets/textures/block/composter/composter7.png"),
    BLOCK_COMPOSTER_8("/assets/textures/block/composter/composter8.png"),
    /* endregion */

    /* region Item */
    ITEM_ANVIL("/assets/textures/item/anvil.png"),
    ITEM_BONE_MEAL("/assets/textures/item/bone_meal.png"),
    ITEM_CABBAGE("/assets/textures/item/cabbage.png"),
    ITEM_CABBAGE_SEED("/assets/textures/item/cabbage_seed.png"),
    ITEM_CARROT("/assets/textures/item/carrot.png"),
    ITEM_CHEST("/assets/textures/item/chest.png"),
    ITEM_COMPOSTER("/assets/textures/item/composter.png"),
    ITEM_CORN("/assets/textures/item/corn.png"),
    ITEM_CORN_SEED("/assets/textures/item/corn_seed.png"),
    ITEM_COTTON("/assets/textures/item/cotton.png"),
    ITEM_COTTON_SEED("/assets/textures/item/cotton_seed.png"),
    ITEM_DIAMOND_HOE("/assets/textures/item/diamond_hoe.png"),
    ITEM_EMERALD("/assets/textures/item/emerald.png"),
    ITEM_EMPTY_SLOT_EMERALD("/assets/textures/item/empty_slot_emerald.png"),
    ITEM_EMPTY_SLOT_HOE("/assets/textures/item/empty_slot_hoe.png"),
    ITEM_GOLDEN_CARROT("/assets/textures/item/golden_carrot.png"),
    ITEM_GOLDEN_HOE("/assets/textures/item/golden_hoe.png"),
    ITEM_IRON_HOE("/assets/textures/item/iron_hoe.png"),
    ITEM_NETHERITE_HOE("/assets/textures/item/netherite_hoe.png"),
    ITEM_ONION("/assets/textures/item/onion.png"),
    ITEM_POTATO("/assets/textures/item/potato.png"),
    ITEM_TOMATO("/assets/textures/item/tomato.png"),
    ITEM_TOMATO_SEED("/assets/textures/item/tomato_seed.png"),
    ITEM_VILLAGER_SPAWN_EGG("/assets/textures/item/villager_spawn_egg.png"),
    ITEM_WHEAT("/assets/textures/item/wheat.png"),
    ITEM_WHEAT_SEED("/assets/textures/item/wheat_seed.png"),
    ITEM_WRITABLE_BOOK("/assets/textures/item/writable_book.png"),
    /* endregion */

    /* region Particle */
    PE_GLINT("/assets/textures/particle/glint.png"),
    /* endregion */

    ;

    val inputStream: InputStream?
        get() = this::class.java.getResourceAsStream(resourcePath)

    /** 延迟加载纹理，避免初始化时占用太多资源 */
    val texture: Texture by lazy { Texture(inputStream!!).apply { load() } }
}
