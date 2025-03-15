package com.midnightcrowing

import com.midnightcrowing.config.AppConfig
import com.midnightcrowing.gui.MainMenuScreen
import com.midnightcrowing.gui.Window
import com.midnightcrowing.resource.FontEnum
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.resource.ResourcesLoader.loadFont


fun main() {
    val window = Window(
        AppConfig.APP_NAME_CN,
        AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT,
        AppConfig.WINDOW_MIN_WIDTH, AppConfig.WINDOW_MIN_HEIGHT,
    )

    val fontPath = ResourcesEnum.FONT_DEFAULT.inputStream
    if (fontPath == null) {
        println("字体资源加载失败")
        return
    }
    loadFont(
        FontEnum.DEFAULT.value,
        "E:/Projects/IntelligentFarming/build/resources/main/assets/font/unifont-16.0.02.otf"
//        fontPath
    )

    window.setScreen(MainMenuScreen(window))

    window.loop()

    // 释放资源
    window.cleanup()
}
