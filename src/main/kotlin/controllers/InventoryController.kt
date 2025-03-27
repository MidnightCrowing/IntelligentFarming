package com.midnightcrowing.controllers

import com.midnightcrowing.gui.Inventory
import com.midnightcrowing.model.item.ItemList
import com.midnightcrowing.model.item.ItemStack


class InventoryController(gameController: GameController) {
    private lateinit var inventory: Inventory
    private val items = ItemList(36) // 36格：27背包+9快捷栏
    val hotBarItems get() = items.slice(0 until 9) // 快捷栏在索引0到8
    val mainInvItems get() = items.slice(9 until 36) // 背包在索引9到35

    fun init(inventory: Inventory) {
        this.inventory = inventory
    }

    fun getItem(slot: Int): ItemStack = items[slot]

    fun setItem(slot: Int, stack: ItemStack) {
        items[slot] = stack
    }

    fun addItem(stack: ItemStack): Boolean {
        for (i in 0 until 36) {
            if (items[i].isEmpty()) {
                items[i] = stack
                return true
            }
        }
        return false // 没有空槽位
    }

    fun removeItem(slot: Int): ItemStack {
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