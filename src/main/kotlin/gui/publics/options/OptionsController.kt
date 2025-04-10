package com.midnightcrowing.gui.publics.options

import java.util.*

class OptionsController(private val options: Options) {
    private val menuStack: Stack<OptionMenuEnum> = Stack()

    fun switchToMenu(id: OptionMenuEnum) {
        options.switchToMenu(id)
        // 将当前菜单压入堆栈
        menuStack.push(id)
    }

    fun backToMenu() {
        if (menuStack.size > 1) {
            // 弹出当前菜单
            menuStack.pop()
            // 获取上一个菜单
            val previousMenu = menuStack.peek()
            options.switchToMenu(previousMenu)
        } else {
            // 没有上一级菜单
            finishOptions()
        }
    }

    private fun finishOptions() = options.setHidden(true)
}