package com.midnightcrowing.resource

import java.io.InputStream
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

sealed class AudioResource(val path: String) {
    // 单文件资源
    class Single(path: String) : AudioResource(path)

    // 多文件资源组
    class Group(private val paths: List<String>) : AudioResource("") {
        fun getRandomAudioStream(): AudioInputStream {
            val randomPath = paths.random()
            return AudioSystem.getAudioInputStream(
                javaClass.getResourceAsStream(randomPath)!!.buffered()
            )
        }
    }

    val inputStream: InputStream?
        get() = javaClass.getResourceAsStream(path)

    fun getAudioStream(): AudioInputStream =
        AudioSystem.getAudioInputStream(inputStream!!.buffered())

    // 资源定义
    companion object {
        /** `background.music` 背景音乐 */
        val BACKGROUND_MUSIC = Single("/assets/sounds/background/C418-Sweden.wav")

        /** `ui.button.click` UI点击按钮 */
        val UI_BUTTON_CLICK = Single("/assets/sounds/effects/ui.button.click.wav")

        /** `block.composter.empty` 清空堆肥桶 */
        val BLOCK_COMPOSTER_EMPTY = Group(
            listOf(
                "/assets/sounds/effects/block.composter.empty1.wav",
                "/assets/sounds/effects/block.composter.empty2.wav",
                "/assets/sounds/effects/block.composter.empty3.wav"
            )
        )

        /** `block.composter.fill_success` 往堆肥桶里加入物品并增加堆肥层 */
        val BLOCK_COMPOSTER_FILL_SUCCESS = Group(
            listOf(
                "/assets/sounds/effects/block.composter.fill_success1.wav",
                "/assets/sounds/effects/block.composter.fill_success2.wav",
                "/assets/sounds/effects/block.composter.fill_success3.wav",
                "/assets/sounds/effects/block.composter.fill_success4.wav"
            )
        )

        /** `block.composter.ready` 堆肥完成 */
        val BLOCK_COMPOSTER_READY = Group(
            listOf(
                "/assets/sounds/effects/block.composter.ready1.wav",
                "/assets/sounds/effects/block.composter.ready2.wav",
                "/assets/sounds/effects/block.composter.ready3.wav",
                "/assets/sounds/effects/block.composter.ready4.wav"
            )
        )

        /** `block.crop.break` 收获作物 */
        val BLOCK_CROP_BREAK = Group(
            listOf(
                "/assets/sounds/effects/block.crop.break1.wav",
                "/assets/sounds/effects/block.crop.break2.wav",
                "/assets/sounds/effects/block.crop.break3.wav",
                "/assets/sounds/effects/block.crop.break4.wav",
                "/assets/sounds/effects/block.crop.break5.wav",
                "/assets/sounds/effects/block.crop.break6.wav"
            )
        )

        /** `entity.villager.no` 交易失败 */
        val ENTITY_VILLAGER_NO = Group(
            listOf(
                "/assets/sounds/effects/entity.villager.no1.wav",
                "/assets/sounds/effects/entity.villager.no2.wav",
                "/assets/sounds/effects/entity.villager.no3.wav"
            )
        )

        /** `entity.villager.trade` 打开交易GUI */
        val ENTITY_VILLAGER_TRADE = Group(
            listOf(
                "/assets/sounds/effects/entity.villager.trade1.wav",
                "/assets/sounds/effects/entity.villager.trade2.wav",
                "/assets/sounds/effects/entity.villager.trade3.wav"
            )
        )

        /** `entity.villager.yes` 交易成功 */
        val ENTITY_VILLAGER_YES = Group(
            listOf(
                "/assets/sounds/effects/entity.villager.yes1.wav",
                "/assets/sounds/effects/entity.villager.yes2.wav",
                "/assets/sounds/effects/entity.villager.yes3.wav"
            )
        )

        /** `item.bone_meal.use` 使用骨粉 */
        val ITEM_BONE_MEAL_USE = Group(
            listOf(
                "/assets/sounds/effects/item.bone_meal.use1.wav",
                "/assets/sounds/effects/item.bone_meal.use2.wav",
                "/assets/sounds/effects/item.bone_meal.use3.wav",
                "/assets/sounds/effects/item.bone_meal.use4.wav",
                "/assets/sounds/effects/item.bone_meal.use5.wav"
            )
        )

        /** `item.crop.plant` 种植作物 */
        val ITEM_CROP_PLANT = Group(
            listOf(
                "/assets/sounds/effects/item.crop.plant1.wav",
                "/assets/sounds/effects/item.crop.plant2.wav",
                "/assets/sounds/effects/item.crop.plant3.wav",
                "/assets/sounds/effects/item.crop.plant4.wav",
                "/assets/sounds/effects/item.crop.plant5.wav",
                "/assets/sounds/effects/item.crop.plant6.wav"
            )
        )

        // 通过ID获取资源（需要时添加更多映射）
        fun getById(id: String): AudioResource {
            return when (id) {
                "background.music" -> BACKGROUND_MUSIC
                "ui.button.click" -> UI_BUTTON_CLICK
                "block.composter.empty" -> BLOCK_COMPOSTER_EMPTY
                "block.composter.fill_success" -> BLOCK_COMPOSTER_FILL_SUCCESS
                "block.composter.ready" -> BLOCK_COMPOSTER_READY
                "block.crop.break" -> BLOCK_CROP_BREAK
                "entity.villager.no" -> ENTITY_VILLAGER_NO
                "entity.villager.trade" -> ENTITY_VILLAGER_TRADE
                "entity.villager.yes" -> ENTITY_VILLAGER_YES
                "item.bone_meal.use" -> ITEM_BONE_MEAL_USE
                "item.crop.plant" -> ITEM_CROP_PLANT
                else -> throw IllegalArgumentException("Unknown audio resource: $id")
            }
        }
    }
}