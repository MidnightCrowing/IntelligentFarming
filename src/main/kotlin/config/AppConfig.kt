package com.midnightcrowing.config

import java.util.*

object AppConfig {
    const val APP_NAME = "IntelligentFarming"
    const val APP_NAME_CN = "智慧农耕"
    const val WINDOW_WIDTH = 1216
    const val WINDOW_HEIGHT = 684
    const val WINDOW_MIN_WIDTH = 1216
    const val WINDOW_MIN_HEIGHT = 684

    val VERSION: String
        get() {
            val props = Properties()
            props.load(AppConfig::class.java.getResourceAsStream("/version.properties"))
            return props.getProperty("version")
        }

    /** 视频设置 */
    // 垂直同步
    var SWAP_INTERVAL: Boolean = true

    /** 音乐和声音选项 */
    // 主音量
    var MAIN_VOLUME: Double = 1.0
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }

    // 音乐音量
    var MUSIC_VOLUME: Double = 1.0
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }

    // 音效音量
    var SOUND_VOLUME: Double = 1.0
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }

    // UI音量
    var UI_VOLUME: Double = 1.0
        set(value) {
            field = value.coerceIn(0.0, 1.0)
        }
}