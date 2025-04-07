package com.midnightcrowing.controllers

import com.midnightcrowing.gui.publics.Inventory
import com.midnightcrowing.model.item.ItemList
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.item.Items


class InventoryController(farmController: FarmController) {
    private lateinit var inventory: Inventory
    val hotBarController: HotBarController by lazy { farmController.hotBar }

    val items: ItemList = ItemList(36) // 36格：27背包+9快捷栏
    val hotBarItems: List<ItemStack> get() = items.slice(0 until 9) // 快捷栏在索引0到8
    val mainInvItems: List<ItemStack> get() = items.slice(9 until 36) // 背包在索引9到35

    fun init(inventory: Inventory) {
        this.inventory = inventory

        // 示例物品
        setItem(0, ItemStack(Items.WHEAT_SEED.id, 64))
        setItem(1, ItemStack(Items.CARROT.id, 64))
        setItem(2, ItemStack(Items.POTATO.id, 64))
        setItem(3, ItemStack(Items.CABBAGE_SEED.id, 64))
        setItem(4, ItemStack(Items.CORN_SEED.id, 64))
        setItem(5, ItemStack(Items.COTTON_SEED.id, 64))
        setItem(6, ItemStack(Items.TOMATO_SEED.id, 64))
        setItem(7, ItemStack(Items.ONION.id, 64))
        setItem(8, ItemStack(Items.BONE_MEAL.id, 64))
        setItem(9, ItemStack(Items.EMERALD.id, 64))
        setItem(10, ItemStack(Items.EMERALD.id, 64))
        setItem(11, ItemStack(Items.EMERALD.id, 64))
        setItem(12, ItemStack(Items.EMERALD.id, 64))
        setItem(13, ItemStack(Items.EMERALD.id, 64))
        setItem(14, ItemStack(Items.EMERALD.id, 64))
        setItem(15, ItemStack(Items.EMERALD.id, 64))
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
            val maxCount: Int = ItemRegistry.getItemMaxCount(currentItem.id)
            if (currentItem.id == item.id && currentItem.count < maxCount) {
                val total = currentItem.count + item.count
                if (total > maxCount) {
                    currentItem.count = maxCount
                    item.count = total - maxCount
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

    /**
     * 处理物品交换和叠加
     * @param invItem 背包中的物品
     * @param dragItem 拖动中的物品
     * @return 处理后的物品对, 第一个是背包物品，第二个是拖动物品
     */
    fun exchangeAndMergeItems(invItem: ItemStack, dragItem: ItemStack): Pair<ItemStack, ItemStack> {
        return when {
            // 两者都为空，直接返回空
            invItem.isEmpty() && dragItem.isEmpty() -> ItemStack.EMPTY to ItemStack.EMPTY

            // 背包有物品，拖动为空，取出物品
            !invItem.isEmpty() && dragItem.isEmpty() -> ItemStack.EMPTY to invItem

            // 背包为空，拖动有物品，放入物品
            invItem.isEmpty() && !dragItem.isEmpty() -> dragItem to ItemStack.EMPTY

            // 都有物品，且 id 相同，叠加物品
            invItem.id == dragItem.id -> {
                // 叠加物品
                val totalCount = invItem.count + dragItem.count
                val maxCount: Int = ItemRegistry.getItemMaxCount(dragItem.id)
                invItem.count = totalCount.coerceAtMost(maxCount)
                dragItem.count = (totalCount - maxCount).coerceAtLeast(0)
                invItem to if (dragItem.count == 0) ItemStack.EMPTY else dragItem
            }

            // 都有物品，且 id 不同，交换物品
            else -> dragItem to invItem
        }
    }

    /**
     * 右键交换物品
     * @param invItem 背包中的物品
     * @param dragItem 拖动中的物品
     * @return 处理后的物品对, 第一个是背包物品，第二个是拖动物品
     */
    fun rightClickExchangeItems(invItem: ItemStack, dragItem: ItemStack): Pair<ItemStack, ItemStack> {
        return when {
            // 拖拽空，背包不空 => 拿出一半或全部
            dragItem.isEmpty() && !invItem.isEmpty() -> {
                val halfCount = invItem.count / 2
                if (halfCount > 0) {
                    val newDragItem = invItem.copy(count = halfCount)
                    invItem.count -= halfCount
                    newDragItem to invItem
                } else {
                    invItem to ItemStack.EMPTY
                }
            }

            // 拖拽有，背包空 => 放入一个
            !dragItem.isEmpty() && invItem.isEmpty() -> {
                val newInvItem = dragItem.copy(count = 1)
                dragItem.count -= 1
                if (dragItem.count <= 0) {
                    newInvItem to ItemStack.EMPTY
                } else {
                    newInvItem to dragItem
                }
            }

            // 两者相同 => 合并 1 个
            invItem.id == dragItem.id -> {
                invItem.count += 1
                dragItem.count -= 1
                val newDrag = if (dragItem.count <= 0) ItemStack.EMPTY else dragItem
                val newInv = if (invItem.count <= 0) ItemStack.EMPTY else invItem
                newInv to newDrag
            }

            // 其他情况 => 不交换，保持原样
            else -> invItem to dragItem
        }
    }


    fun displayInventory() {
        println("玩家物品栏:")
        for (i in 0 until 36) {
            val item = items[i]
            println("槽位 $i: ${if (item.isEmpty()) "空" else "${item.id} x${item.count}"}")
        }
    }
}