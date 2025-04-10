package com.midnightcrowing.gui.publics.options.menus

import com.midnightcrowing.gui.bases.button.Button
import com.midnightcrowing.gui.publics.options.OptionMenuBase
import com.midnightcrowing.gui.publics.options.OptionMenuEnum
import com.midnightcrowing.gui.publics.options.Options

class MainMenu(parent: Options) : OptionMenuBase(parent, "选项") {
    private val buttons = listOf(
        Button(buttonLayout).apply {
            text = "视频设置…"; textSpacing = 2.0
            onClickCallback = { controller.switchToMenu(OptionMenuEnum.VIDEO_OPTIONS) }
        },
        Button(buttonLayout).apply {
            text = "音乐和声音…"; textSpacing = 2.0
            onClickCallback = { controller.switchToMenu(OptionMenuEnum.AUDIO_OPTIONS) }
        },
        Button(buttonLayout).apply {
            text = "关于…"; textSpacing = 2.0
            onClickCallback = { controller.switchToMenu(OptionMenuEnum.ABOUT_OPTIONS) }
        },
    )

    init {
        buttons.forEachIndexed { index, slider -> buttonLayout.addButton(index + 1, slider) }
    }
}