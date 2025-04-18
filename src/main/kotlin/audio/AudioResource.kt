package com.midnightcrowing.audio

import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.resource.ResourceManager
import com.midnightcrowing.resource.ResourceType
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem

object AudioResource {
    private val resourceMap: Map<SoundEvents, List<ResourceLocation>> = mapOf(
        SoundEvents.BACKGROUND_MUSIC to listOf(
            ResourceLocation(ResourceType.SO_BACKGROUND, "minecraft", "C418-Sweden.wav")
        ),
        SoundEvents.UI_BUTTON_CLICK to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "ui.button.click.wav")
        ),
        SoundEvents.BLOCK_COMPOSTER_EMPTY to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.empty1.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.empty2.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.empty3.wav")
        ),
        SoundEvents.BLOCK_COMPOSTER_FILL_SUCCESS to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.fill_success1.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.fill_success2.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.fill_success3.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.fill_success4.wav")
        ),
        SoundEvents.BLOCK_COMPOSTER_READY to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.ready1.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.ready2.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.ready3.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.composter.ready4.wav")
        ),
        SoundEvents.BLOCK_CROP_BREAK to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.crop.break1.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.crop.break2.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.crop.break3.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.crop.break4.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.crop.break5.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "block.crop.break6.wav")
        ),
        SoundEvents.ENTITY_VILLAGER_NO to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "entity.villager.no1.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "entity.villager.no2.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "entity.villager.no3.wav")
        ),
        SoundEvents.ENTITY_VILLAGER_TRADE to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "entity.villager.trade1.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "entity.villager.trade2.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "entity.villager.trade3.wav")
        ),
        SoundEvents.ENTITY_VILLAGER_YES to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "entity.villager.yes1.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "entity.villager.yes2.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "entity.villager.yes3.wav")
        ),
        SoundEvents.ITEM_BONE_MEAL_USE to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.bone_meal.use1.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.bone_meal.use2.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.bone_meal.use3.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.bone_meal.use4.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.bone_meal.use5.wav")
        ),
        SoundEvents.ITEM_CROP_PLANT to listOf(
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.crop.plant1.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.crop.plant2.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.crop.plant3.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.crop.plant4.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.crop.plant5.wav"),
            ResourceLocation(ResourceType.SO_EFFECTS, "minecraft", "item.crop.plant6.wav")
        )
    )

    fun List<ResourceLocation>.getRandomAudioStream(): AudioInputStream {
        return AudioSystem.getAudioInputStream(
            ResourceManager.getInputStream(this.random())?.buffered()
        )
    }

    fun getByEvent(sound: SoundEvents): List<ResourceLocation>? {
        return resourceMap[sound]
    }

    fun getAudioStream(location: ResourceLocation): AudioInputStream? {
        val stream = ResourceManager.getInputStream(location)?.buffered()
        return AudioSystem.getAudioInputStream(stream)
    }
}