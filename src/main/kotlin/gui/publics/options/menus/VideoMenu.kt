package com.midnightcrowing.gui.publics.options.menus

import com.midnightcrowing.gui.bases.button.Button
import com.midnightcrowing.gui.publics.options.OptionMenuBase
import com.midnightcrowing.gui.publics.options.Options

class VideoMenu(parent: Options) : OptionMenuBase(parent, "视频设置") {
    private val verticalSync = Button(buttonLayout).apply {
        var isVerticalSyncEnabled = false // 默认关闭垂直同步
        text = "垂直同步: 关闭"; textSpacing = 2.0

        onClickCallback = {
            isVerticalSyncEnabled = !isVerticalSyncEnabled // 切换状态
            text = "垂直同步: ${if (isVerticalSyncEnabled) "开启" else "关闭"}" // 更新按钮文字
        }
    }

    init {
        buttonLayout.addButton(1, verticalSync)
    }
}