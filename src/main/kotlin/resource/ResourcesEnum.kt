package com.midnightcrowing.resource

import com.midnightcrowing.resource.ResourcePath.getResources
import java.io.InputStream


enum class ResourcesEnum(val path: InputStream?) {
    // GUI
    COMPONENTS_HOT_BAR(getResources("/assets/gui/hot_bar.png")),
    INVENTORY(getResources("/assets/gui/inventory.png")),
    CHECK_BOX(getResources("/assets/gui/check_box.png")),
    BUTTON_DEFAULT(getResources("/assets/gui/button_default.png")),
    BUTTON_HOVER(getResources("/assets/gui/button_hover.png")),
    BUTTON_PRESSED(getResources("/assets/gui/button_pressed.png")),

    // Views
    WELCOME_BACKGROUND(getResources("/assets/welcome_background.jpg")),

    // Farming
    CABBAGE(getResources("/assets/farming/cabbage.png")),
    CARROT(getResources("/assets/farming/carrot.png")),
    POTATO(getResources("/assets/farming/potato.png")),
    TOMATO(getResources("/assets/farming/tomato.png")),
    WHEAT(getResources("/assets/farming/wheat.png")),
}