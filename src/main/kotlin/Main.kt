package com.midnightcrowing

import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.gui.scenes.MainMenuScreen


fun main() {
    val window = Window.createWindow()

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
