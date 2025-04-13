package com.midnightcrowing.gui.publics.options.menus

import com.midnightcrowing.config.AppConfig
import com.midnightcrowing.gui.bases.button.Button
import com.midnightcrowing.gui.publics.options.OptionMenuBase
import com.midnightcrowing.gui.publics.options.Options

class VideoMenu(parent: Options) : OptionMenuBase(parent, "视频设置") {
    private val swapInterval = Button(buttonLayout).apply {
        textSpacing = 2.0
        updateText(AppConfig.SWAP_INTERVAL)

        onClickCallback = {
            AppConfig.SWAP_INTERVAL = !AppConfig.SWAP_INTERVAL
            updateText(AppConfig.SWAP_INTERVAL)
            window.setSwapInterval(AppConfig.SWAP_INTERVAL)
        }
    }

    private fun Button.updateText(isEnabled: Boolean) {
        text = "垂直同步: ${if (isEnabled) "开启" else "关闭"}"
    }

    init {
        buttonLayout.addButton(1, swapInterval)
    }
}