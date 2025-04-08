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
}