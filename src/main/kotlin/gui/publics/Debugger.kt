package com.midnightcrowing.gui.publics

import com.midnightcrowing.gui.bases.Window
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.utils.FPSCounter
import com.midnightcrowing.utils.GameTick
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT
import org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE

class Debugger(private val window: Window) {
    private var visible: Boolean = true

    private val fpsCounter = FPSCounter()
    private val fpsTextRenderer: TextRenderer = TextRenderer(window.nvg).apply {
        x = 5.0; y = 15.0; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }
    private val tickTextRenderer: TextRenderer = TextRenderer(window.nvg).apply {
        x = 5.0; y = 40.0; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }
    private val mousePosRenderer: TextRenderer = TextRenderer(window.nvg).apply {
        x = 5.0; y = 65.0; textAlign = NVG_ALIGN_LEFT or NVG_ALIGN_MIDDLE
    }

    fun update() {
        // 更新 FPS 计数器
        fpsCounter.update()
    }

    fun render() {
        if (!visible) return

        // 渲染 FPS 和 Tick 信息
        fpsTextRenderer.render("FPS: ${fpsCounter.fps}")
        tickTextRenderer.render("Tick: ${GameTick.tick}")
        val (mousePosX, mousePosY) = window.getCursorPos()
        mousePosRenderer.render("Mouse: X: $mousePosX, Y: $mousePosY")
    }

    fun toggleVisible() {
        visible = !visible
    }
}