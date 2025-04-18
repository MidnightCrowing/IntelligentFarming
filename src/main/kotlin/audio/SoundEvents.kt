package com.midnightcrowing.audio

enum class SoundEvents(val id: String) {
    /** `background.music` 背景音乐 */
    BACKGROUND_MUSIC("background.music"),

    /** `ui.button.click` UI点击按钮 */
    UI_BUTTON_CLICK("ui.button.click"),

    /** `block.composter.empty` 清空堆肥桶 */
    BLOCK_COMPOSTER_EMPTY("block.composter.empty"),

    /** `block.composter.fill_success` 往堆肥桶里加入物品并增加堆肥层 */
    BLOCK_COMPOSTER_FILL_SUCCESS("block.composter.fill_success"),

    /** `block.composter.ready` 堆肥完成 */
    BLOCK_COMPOSTER_READY("block.composter.ready"),

    /** `block.crop.break` 收获作物 */
    BLOCK_CROP_BREAK("block.crop.break"),

    /** `entity.villager.no` 交易失败 */
    ENTITY_VILLAGER_NO("entity.villager.no"),

    /** `entity.villager.trade` 打开交易GUI */
    ENTITY_VILLAGER_TRADE("entity.villager.trade"),

    /** `entity.villager.yes` 交易成功 */
    ENTITY_VILLAGER_YES("entity.villager.yes"),

    /** `item.bone_meal.use` 使用骨粉 */
    ITEM_BONE_MEAL_USE("item.bone_meal.use"),

    /** `item.crop.plant` 种植作物 */
    ITEM_CROP_PLANT("item.crop.plant"),
}