package com.midnightcrowing.gui.publics.options.menus

import com.midnightcrowing.audio.BackgroundMusicPlayer
import com.midnightcrowing.config.AppConfig.MAIN_VOLUME
import com.midnightcrowing.config.AppConfig.MUSIC_VOLUME
import com.midnightcrowing.config.AppConfig.SOUND_VOLUME
import com.midnightcrowing.config.AppConfig.UI_VOLUME
import com.midnightcrowing.gui.bases.button.Slider
import com.midnightcrowing.gui.publics.options.OptionMenuBase
import com.midnightcrowing.gui.publics.options.Options
import kotlin.reflect.KMutableProperty0

class AudioMenu(parent: Options) : OptionMenuBase(parent, "音乐和声音选项") {
    private data class VolumeConfig(
        val name: String,
        val prop: KMutableProperty0<Double>,
        val onChange: (Double) -> Unit,
    )

    init {
        val configs = listOf(
            VolumeConfig("主音量", ::MAIN_VOLUME) { volume ->
                MAIN_VOLUME = volume
            },
            VolumeConfig("音乐", ::MUSIC_VOLUME) { volume ->
                MUSIC_VOLUME = volume
                BackgroundMusicPlayer.setVolume(MAIN_VOLUME * MUSIC_VOLUME)
            },
            VolumeConfig("音效", ::SOUND_VOLUME) { volume ->
                SOUND_VOLUME = volume
            },
            VolumeConfig("UI", ::UI_VOLUME) { volume ->
                UI_VOLUME = volume
            }
        )

        configs.forEachIndexed { index, config ->
            val slider = Slider(buttonLayout).apply {
                text = "${config.name}: ${config.prop.get().toPercentage()}"
                value = config.prop.get()
                textSpacing = 2.0
                onValueChangedCallback = { volume ->
                    text = "${config.name}: ${volume.toPercentage()}"
                    config.onChange(volume)
                }
            }
            buttonLayout.addButton(index + 1, slider)
        }
    }
}
