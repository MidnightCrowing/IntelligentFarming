package org.intelligentfarming

import org.intelligentfarming.config.AppConfig
import org.intelligentfarming.gui.Window
import org.lwjgl.opengl.GL11
import views.welcome.Welcome


fun main() {
    val window = Window(
        AppConfig.APP_NAME_CN,
        AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT,
        AppConfig.WINDOW_MIN_WIDTH, AppConfig.WINDOW_MIN_HEIGHT,
    )

    val welcomeFrame = Welcome(window)

    while (!window.shouldClose()) {
        // 清除缓冲区
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        // 渲染内容
        welcomeFrame.render()
        // 交换帧缓冲区
        window.swapBuffers()
        // 处理窗口事件
        window.pollEvents()
    }

    // 释放资源
    welcomeFrame.cleanup()
    window.cleanup()
}
