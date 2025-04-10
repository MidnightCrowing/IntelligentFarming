package com.midnightcrowing.gui.publics.options.menus

import com.midnightcrowing.gui.bases.button.Slider
import com.midnightcrowing.gui.publics.options.OptionMenuBase
import com.midnightcrowing.gui.publics.options.Options

class AudioMenu(parent: Options) : OptionMenuBase(parent, "音乐和声音选项") {
    private val sliders = listOf(
        Slider(buttonLayout).apply {
            text = "主音量: 100%"; value = 1.0; textSpacing = 2.0
            onValueChangedCallback = { volume -> text = "主音量: ${volume.toPercentage()}" }
        },
        Slider(buttonLayout).apply {
            text = "音乐: 100%"; value = 1.0; textSpacing = 2.0
            onValueChangedCallback = { volume -> text = "音乐: ${volume.toPercentage()}" }
        },
        Slider(buttonLayout).apply {
            text = "音效: 100%"; value = 1.0; textSpacing = 2.0
            onValueChangedCallback = { volume -> text = "音效: ${volume.toPercentage()}" }
        },
        Slider(buttonLayout).apply {
            text = "UI: 100%"; value = 1.0; textSpacing = 2.0
            onValueChangedCallback = { volume -> text = "UI: ${volume.toPercentage()}" }
        }
    )

    init {
        sliders.forEachIndexed { index, slider -> buttonLayout.addButton(index + 1, slider) }
    }
}