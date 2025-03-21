package com.midnightcrowing.gui

import com.midnightcrowing.gui.base.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.render.ImageRenderer
import com.midnightcrowing.render.createImageRenderer
import com.midnightcrowing.resource.ResourcesEnum
import com.midnightcrowing.scenes.FarmScene

class ItemCheckBox(scene: FarmScene) : Widget(scene.window) {
    override val renderer: ImageRenderer = createImageRenderer(ResourcesEnum.CHECK_BOX.inputStream)

    private var boundsNow = ScreenBounds(0f, 0f, 0f, 0f)
    private var boundsTarget = ScreenBounds(0f, 0f, 0f, 0f)

    private var lastUpdateTime: Long = System.currentTimeMillis()

    fun interpolate(from: Float, to: Float, deltaTime: Float): Float {
        return (from - to) * deltaTime / 50
    }

    override fun place(bounds: ScreenBounds) {
        super.place(bounds)
        boundsNow = bounds
        boundsTarget = bounds
    }

    // 通过动画移动到指定位置
    fun moveTo(bounds: ScreenBounds) {
        boundsTarget = bounds
        boundsNow.x1 -= -50
        boundsNow.y1 -= -50
        boundsNow.x2 += -50
        boundsNow.y2 += -50
    }

    override fun render() {
        val currentTime = System.currentTimeMillis()
        val deltaTime = (currentTime - lastUpdateTime).toFloat()
        lastUpdateTime = currentTime

        boundsNow.x1 -= interpolate(boundsNow.x1, boundsTarget.x1, deltaTime)
        boundsNow.y1 -= interpolate(boundsNow.y1, boundsTarget.y1, deltaTime)
        boundsNow.x2 -= interpolate(boundsNow.x2, boundsTarget.x2, deltaTime)
        boundsNow.y2 -= interpolate(boundsNow.y2, boundsTarget.y2, deltaTime)

        super.place(boundsNow)

        super.render()
    }
}