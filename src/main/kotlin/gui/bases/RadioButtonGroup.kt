package com.midnightcrowing.gui.bases

class RadioButtonGroup {
    private val buttons = mutableListOf<Button>()
    var select: Button? = null
        private set

    fun addButton(button: Button) {
        buttons.add(button)
        button.onClickCallback = {
            setSelectedButton(button)
        }
    }

    fun clear() {
        buttons.forEach { it.isSelect = false }
        select = null
    }

    var updateSelectCallback: (() -> Unit)? = null

    private fun setSelectedButton(button: Button) {
        select?.isSelect = false
        select = button
        button.isSelect = true

        updateSelectCallback?.invoke()
    }
}