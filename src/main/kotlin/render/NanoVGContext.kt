package com.midnightcrowing.render

import com.midnightcrowing.gui.base.Window
import org.lwjgl.nanovg.NanoVG.nvgBeginFrame
import org.lwjgl.nanovg.NanoVG.nvgEndFrame
import org.lwjgl.nanovg.NanoVGGL2.*
import org.lwjgl.opengl.GL46

object NanoVGContext {
    val vg: Long = nvgCreate(NVG_ANTIALIAS or NVG_STENCIL_STROKES)

    init {
        if (vg == 0L) {
            throw RuntimeException("Failed to create NanoVG context")
        }
    }

    fun beginFrame(window: Window) {
        GL46.glEnable(GL46.GL_BLEND)  // 确保透明度正常
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA)

        nvgBeginFrame(vg, window.width.toFloat(), window.height.toFloat(), 1f)
    }

    fun endFrame() {
        nvgEndFrame(vg)
    }

    fun cleanup() {
        nvgDelete(vg)
    }
}
