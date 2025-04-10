package com.midnightcrowing.gui.bases.button

import com.midnightcrowing.audio.SoundEffectPlayer
import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.Texture
import com.midnightcrowing.renderer.NineSliceRenderer
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.utils.LayoutScaler.scaleValue
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT

class Slider(parent: Widget) : AbstractButton(parent) {
    private enum class TextureState { DEFAULT, HOVER }

    private val thumbTextures = mapOf(
        TextureState.DEFAULT to TextureResourcesEnum.GUI_SLIDER_THUMB_DEFAULT.texture,
        TextureState.HOVER to TextureResourcesEnum.GUI_SLIDER_THUMB_HOVER.texture,
    )

    private val trackTextures = mapOf(
        TextureState.DEFAULT to TextureResourcesEnum.GUI_SLIDER_TRACK_DEFAULT.texture,
        TextureState.HOVER to TextureResourcesEnum.GUI_SLIDER_TRACK_HOVER.texture,
    )

    private val thumbRenderer = createRenderer(thumbTextures[TextureState.DEFAULT])
    private val trackRenderer = createRenderer(trackTextures[TextureState.DEFAULT])
    private val textRenderer = TextRenderer(window.nvg)

    var isHover = false
    var isLeftMousePress = false

    var value: Double = 0.0
        set(v) {
            field = v.coerceIn(0.0, 1.0)
        }

    private val thumbWidth: Double get() = widgetBounds.width / 25
    var onValueChangedCallback: ((Double) -> Unit)? = null

    fun Double.toPercentage(): String = "${(this * 100).toInt()}%"

    private fun createRenderer(texture: Texture?): NineSliceRenderer? =
        texture?.let { NineSliceRenderer(it, textureBorder = 4f, vertexBorder = 10f) }

    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) = place(ScreenBounds(x1, y1, x2, y2))

    override fun place(bounds: ScreenBounds) {
        super.place(bounds)
        val scaledBorder = scaleValue(parentWidth, 10.0, 15.0).toFloat()
        trackRenderer?.vertexBorder = scaledBorder
        thumbRenderer?.vertexBorder = scaledBorder
    }

    private fun calculateThumbBounds(): ScreenBounds {
        val trackWidth = widgetBounds.width
        val thumbX = widgetBounds.x1 + thumbWidth / 2 + (trackWidth - thumbWidth) * value
        return ScreenBounds(thumbX - thumbWidth / 2, widgetBounds.y1, thumbX + thumbWidth / 2, widgetBounds.y2)
    }

    override fun update() {
        val state = if (isLeftMousePress) TextureState.HOVER else TextureState.DEFAULT
        thumbRenderer?.texture = thumbTextures[state]!!
        // trackRenderer?.texture = trackTextures[state]!!
    }

    override fun onMouseEnter() {
        isHover = true
    }

    override fun onMouseLeave() {
        isHover = false
        isLeftMousePress = false
    }

    override fun onClick(e: MouseClickEvent) {
        updateValueFromMouseX(e.x)
        SoundEffectPlayer.play(soundEffect)
        onValueChangedCallback?.invoke(value)
    }

    override fun onMousePress(e: MousePressedEvent) {
        super.onMousePress(e)
        if (e.button == GLFW_MOUSE_BUTTON_LEFT) {
            isLeftMousePress = true
        }
    }

    override fun onMouseRelease(e: MouseReleasedEvent) {
        super.onMouseRelease(e)
        if (e.button == GLFW_MOUSE_BUTTON_LEFT) {
            isLeftMousePress = false
            SoundEffectPlayer.play(soundEffect)
        }
    }

    override fun onMouseMove(e: MouseMoveEvent) {
        super.onMouseMove(e)
        if (isLeftMousePress) {
            updateValueFromMouseX(e.x)
            onValueChangedCallback?.invoke(value)
        }
    }

    private fun updateValueFromMouseX(mouseX: Double) {
        val trackWidth = widgetBounds.width - thumbWidth
        value = (mouseX - widgetBounds.x1 - thumbWidth / 2) / trackWidth
    }

    override fun doRender() {
        trackRenderer?.render(widgetBounds)
        thumbRenderer?.render(calculateThumbBounds())
        textRenderer.render()
        super.doRender()
    }
}