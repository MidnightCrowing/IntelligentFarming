package com.midnightcrowing.resource

enum class ResourceType(val folder: String) {
    ROOT("assets"),

    /** FONT */
    FONT("font"),

    /** Sound */
    SOUND("sounds"),
    SO_BACKGROUND("${SOUND.folder}/background"),
    SO_EFFECTS("${SOUND.folder}/effects"),

    /** Texture */
    TEXTURE("textures"),
    TE_BACKGROUND("${TEXTURE.folder}/background"),
    TE_BLOCK("${TEXTURE.folder}/block"),
    TE_GUI("${TEXTURE.folder}/gui"),
    TE_ITEM("${TEXTURE.folder}/item"),
    TE_PARTICLE("${TEXTURE.folder}/particle"),
}