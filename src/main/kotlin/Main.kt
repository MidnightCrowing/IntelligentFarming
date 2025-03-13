package com.midnightcrowing

import com.midnightcrowing.config.AppConfig
import com.midnightcrowing.gui.MainMenuScreen
import com.midnightcrowing.gui.Window
import com.midnightcrowing.render.NanoVGContext
import com.midnightcrowing.resource.FontEnum
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.resource.ResourcesLoader.loadFont
import org.lwjgl.opengl.GL11


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

    val mainMenuScreenFrame = MainMenuScreen(window)

    while (!window.shouldClose()) {
        // 清除缓冲区
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        // 开始 NanoVG 绘制
        NanoVGContext.beginFrame(window)
        // 渲染内容
        mainMenuScreenFrame.render()
        // 结束 NanoVG 绘制
        NanoVGContext.endFrame()
        // 交换帧缓冲区
        window.swapBuffers()
        // 处理窗口事件
        window.pollEvents()
    }

    // 释放资源
    mainMenuScreenFrame.cleanup()
    NanoVGContext.cleanup()
    window.cleanup()
}
