package com.midnightcrowing.controllers

import com.midnightcrowing.gui.scenes.farmScene.Inventory
import com.midnightcrowing.model.item.ItemList
import com.midnightcrowing.model.item.ItemStack


class InventoryController(farmController: FarmController) {
    private lateinit var inventory: Inventory
    val hotBarController: HotBarController by lazy { farmController.hotBar }

    val items: ItemList = ItemList(36) // 36格：27背包+9快捷栏
    val hotBarItems: List<ItemStack> get() = items.slice(0 until 9) // 快捷栏在索引0到8
    val mainInvItems: List<ItemStack> get() = items.slice(9 until 36) // 背包在索引9到35

    fun init(inventory: Inventory) {
        this.inventory = inventory

        // 示例物品
        setItem(0, ItemStack("minecraft:cabbage_seed", 2))
        setItem(1, ItemStack("minecraft:carrot", 2))
        setItem(2, ItemStack("minecraft:corn_seed", 2))
        setItem(3, ItemStack("minecraft:cotton_seed", 2))
        setItem(4, ItemStack("minecraft:onion", 63))
        setItem(12, ItemStack("minecraft:onion", 2))
        setItem(16, ItemStack("minecraft:onion", 2))
        setItem(5, ItemStack("minecraft:potato", 2))
        setItem(6, ItemStack("minecraft:tomato_seed", 2))
        setItem(7, ItemStack("minecraft:wheat_seed", 2))
    }

    fun getItem(slot: Int): ItemStack = items[slot]

    fun setItem(slot: Int, stack: ItemStack) {
        items[slot] = stack
    }

    /**
     * 向背包中添加物品
     * @param item 要添加的物品
     * @param target 目标物品栏（如 `"all"`、`"hotbar"`、`"main"`）
     * @return 是否成功添加
     */
    fun addItem(item: ItemStack, target: String = "all"): Boolean {
        val (minIndex, maxIndex) = when (target) {
            "hotbar" -> 0 to 9
            "main" -> 9 to 36
            else -> 0 to 36
        }

        // 先尝试叠加
        for (i in minIndex until maxIndex) {
            val currentItem = items[i]
            if (currentItem.id == item.id && currentItem.count < 64) {
                val total = currentItem.count + item.count
                if (total > 64) {
                    currentItem.count = 64
                    item.count = total - 64
                } else {
                    currentItem.count = total
                    return true
                }
            }
        }

        // 再寻找空槽位
        for (i in minIndex until maxIndex) {
            if (isSlotEmpty(i)) {
                items[i] = item
                return true
            }
        }
        return false // 背包已满
    }

    fun removeItem(slot: Int): ItemStack {
        val item = items[slot]
        items[slot] = ItemStack.EMPTY
        return item
    }

    fun popItem(slot: Int): ItemStack {
        val item = items[slot]
        items[slot] = ItemStack.EMPTY
        return item
    }

    fun isSlotEmpty(slot: Int): Boolean = items.isEmpty(slot)

    fun swapSlots(slot1: Int, slot2: Int) {
        val temp = items[slot1]
        items[slot1] = items[slot2]
        items[slot2] = temp
    }

    fun displayInventory() {
        println("玩家物品栏:")
        for (i in 0 until 36) {
            val item = items[i]
            println("槽位 $i: ${if (item.isEmpty()) "空" else "${item.id} x${item.count}"}")
        }
    }
}