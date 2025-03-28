package com.midnightcrowing.controllers

import com.midnightcrowing.gui.Inventory
import com.midnightcrowing.model.item.ItemList
import com.midnightcrowing.model.item.ItemStack


class InventoryController(gameController: GameController) {
    private lateinit var inventory: Inventory
    internal val hotBarController: HotBarController by lazy { gameController.hotBar }

    internal val items = ItemList(36) // 36格：27背包+9快捷栏
    val hotBarItems get() = items.slice(0 until 9) // 快捷栏在索引0到8
    val mainInvItems get() = items.slice(9 until 36) // 背包在索引9到35

    fun init(inventory: Inventory) {
        this.inventory = inventory

        // 示例物品
        setItem(0, ItemStack("minecraft:cabbage_seed", 2))
        setItem(1, ItemStack("minecraft:carrot", 2))
        setItem(2, ItemStack("minecraft:corn_seed", 2))
        setItem(3, ItemStack("minecraft:cotton_seed", 2))
        setItem(4, ItemStack("minecraft:onion", 2))
        setItem(5, ItemStack("minecraft:potato", 2))
        setItem(6, ItemStack("minecraft:tomato_seed", 2))
        setItem(7, ItemStack("minecraft:wheat_seed", 2))
    }

    fun getItem(slot: Int): ItemStack = items[slot]

    fun setItem(slot: Int, stack: ItemStack) {
        items[slot] = stack
    }

    fun addItem(stack: ItemStack): Boolean {
        // TODO
        for (i in 0 until 36) {
            if (items[i].id == stack.id && items[i].count < 64) {
                if (items[i].count + stack.count > 64) {
                    val excess = items[i].count + stack.count - 64
                    items[i].count = 64
                    stack.count = excess
                } else {
                    items[i].count += stack.count
                    return true
                }
                return true
            }
            if (stack.count <= 0) {
                return true // 添加完成
            }
        }
        for (i in 0 until 36) {
            if (items[i].isEmpty()) {
                items[i] = stack
                return true // 添加完成
            }
        }
        return false // 没有空槽位
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