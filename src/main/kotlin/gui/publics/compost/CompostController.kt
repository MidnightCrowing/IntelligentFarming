package com.midnightcrowing.gui.publics.compost

import com.midnightcrowing.audio.SoundEffectPlayer
import com.midnightcrowing.gui.publics.inventory.InventoryController
import com.midnightcrowing.gui.scenes.farmScene.FarmController
import com.midnightcrowing.model.item.ItemList
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items
import com.midnightcrowing.particles.composter.ComposterParticleSystem
import com.midnightcrowing.utils.Timer

class CompostController(farmController: FarmController) {
    private lateinit var compost: Compost
    val invController: InventoryController by lazy { farmController.inventory }

    // 原料槽物品
    val materialSlotItems: ItemList = ItemList(6)     // 6格：2 × 3

    // 产物槽物品
    var productSlotItem: ItemStack = ItemStack.EMPTY  // 1格

    private val timer = Timer(500) // 每 500 毫秒执行一次

    // 定义可堆肥的农作物和种子 ID 集合
    private val farmItemIds: Set<String> = setOf(
        Items.CABBAGE.id,
        Items.CARROT.id,
        Items.CORN.id,
        Items.COTTON.id,
        Items.ONION.id,
        Items.POTATO.id,
        Items.TOMATO.id,
        Items.WHEAT.id
    )
    private val farmSeedItemIds: Set<String> = setOf(
        Items.CABBAGE_SEED.id, Items.CORN_SEED.id, Items.COTTON_SEED.id, Items.TOMATO_SEED.id, Items.WHEAT_SEED.id
    )

    // 粒子系统，用于生成和管理粒子效果
    val particleSystem: ComposterParticleSystem = ComposterParticleSystem()

    // 判断是否为农作物
    private val ItemStack.isFarm: Boolean get() = this.id in farmItemIds

    // 判断是否为种子
    private val ItemStack.isFarmSeed: Boolean get() = this.id in farmSeedItemIds

    fun init(compost: Compost) {
        this.compost = compost
    }

    fun update() {
        particleSystem.update(0.016f) // Assuming 60 FPS, so deltaTime is approximately 1/60

        if (timer.shouldRun()) {
            when (compost.composterBlock.compostLevel) {
                7 -> simulateCompostingProgress()        // 模拟堆肥桶工作中
                8 -> finishComposting()                  // 堆肥完成，生成骨粉
                else -> processMaterialInput()           // 处理原料槽中的物品
            }
        }
    }

    /**
     * 模拟堆肥桶正在工作，将等级从 7 增加到 8
     */
    private fun simulateCompostingProgress() {
        SoundEffectPlayer.play("block.composter.ready")
        compost.composterBlock.compostLevel = 8
    }

    /**
     * 堆肥完成时生成产物（骨粉），并清空堆肥等级
     */
    private fun finishComposting() {
        if (productSlotItem.isEmpty()) {
            productSlotItem = ItemStack(Items.BONE_MEAL.id, 1)
        } else {
            productSlotItem.count += 1
        }
        SoundEffectPlayer.play("block.composter.empty")
        compost.composterBlock.compostLevel = 0
    }

    /**
     * 遍历原料槽，将种子或作物放入堆肥桶
     */
    private fun processMaterialInput() {
        if (productSlotItem.count >= Items.BONE_MEAL.maxCount) return // 产物骨粉已满

        for (i in 0 until materialSlotItems.size()) {
            val item = materialSlotItems[i]
            if (item.isEmpty()) continue

            val level = compost.composterBlock.compostLevel
            if (level >= 7) break // 等级已满

            when {
                item.isFarmSeed -> {
                    compost.composterBlock.compostLevel += 1
                    removeItemFromSlot(i)
                    generateParticles()
                    SoundEffectPlayer.play("block.composter.fill_success")
                    break // 一次只处理一个物品
                }

                item.isFarm -> {
                    compost.composterBlock.compostLevel = minOf(level + 3, 7)
                    removeItemFromSlot(i)
                    generateParticles()
                    SoundEffectPlayer.play("block.composter.fill_success")
                    break // 一次只处理一个物品
                }

                else -> {
                    // 不可堆肥的物品，不处理
                }
            }
        }
    }

    /**
     * 从指定槽位中移除一个物品，若数量归零则清空槽位
     */
    private fun removeItemFromSlot(index: Int) {
        val item = materialSlotItems[index]
        item.count -= 1
        materialSlotItems[index] = if (item.count <= 0) ItemStack.EMPTY else item
    }

    /**
     * 生成粒子效果
     */
    private fun generateParticles() {
        if (!compost.isVisible) return // 如果界面隐藏，则不生成粒子，避免性能浪费
        particleSystem.generateParticles(compost.composterBlock.bounds.between + Compost.COMPOSTER_ORIGIN_OFFSET)
    }

    fun getItem(slot: Int): ItemStack = materialSlotItems[slot]

    fun setItem(slot: Int, stack: ItemStack) {
        // 检查ItemsList是否全为空
        if (materialSlotItems.isListEmpty()) {
            timer.skip()
        }
        materialSlotItems[slot] = stack
    }

    fun popItem(slot: Int): ItemStack {
        val item = materialSlotItems[slot]
        materialSlotItems[slot] = ItemStack.EMPTY
        return item
    }
}