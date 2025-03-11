package com.midnightcrowing.resource

import com.midnightcrowing.resource.ResourcePath.getResources


enum class ResourcesEnum(val path: String) {
    // GUI
    COMPONENTS_HOT_BAR(getResources("gui\\hot_bar.png")),
    INVENTORY(getResources("gui\\inventory.png")),
    CHECK_BOX(getResources("gui\\check_box.png")),

    // Views
    WELCOME_BACKGROUND(getResources("welcome_background.jpg")),

    // Farming
    CABBAGE(getResources("farming\\cabbage.png")),
    CARROT(getResources("farming\\carrot.png")),
    POTATO(getResources("farming\\potato.png")),
    TOMATO(getResources("farming\\tomato.png")),
    WHEAT(getResources("farming\\wheat.png")),
}