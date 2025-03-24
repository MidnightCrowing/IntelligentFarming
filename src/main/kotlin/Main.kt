package com.midnightcrowing

import com.midnightcrowing.gui.base.Window
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.scenes.MainMenuScreen
import org.lwjgl.nanovg.NanoVG.nvgCreateFont


fun main() {
    val window = Window.createWindow()

    // TODO
    val fontPath = ResourcesEnum.FONT_DEFAULT.inputStream
    if (fontPath == null) {
        println("字体资源加载失败")
        return
    }
    nvgCreateFont(
        window.nvg,
        "default",
        "E:/Projects/IntelligentFarming/build/resources/main/assets/font/unifont-16.0.02.otf"
    )

    window.screen = MainMenuScreen(window)

    while (!window.shouldClose()) {
        window.update()
        window.renderBegin()
        window.render()
        window.renderEnd()
        window.swapBuffers() // 交换帧缓冲区
        window.pollEvents() // 处理窗口事件
    }

    // 释放资源
    window.cleanup()
}
