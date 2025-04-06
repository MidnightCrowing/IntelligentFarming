package com.midnightcrowing.renderer

import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.Texture
import org.lwjgl.opengl.GL46.*


/**
 * 渲染器，负责渲染场景
 */
class TextureRenderer {
    var texture: Texture? = null
        set(value) {
            if (field?.id != value?.id) {
                field = value
                bindTexture()
            }
        }

    var alpha: Double = 1.0  // 默认不透明

    constructor()

    constructor(texture: Texture) {
        this.texture = texture
        bindTexture()
    }

    private fun bindTexture() {
        texture?.let {
            glBindTexture(GL_TEXTURE_2D, it.id)
        }
    }

    fun render(
        u1: Double, v1: Double, u2: Double, v2: Double,
        x1: Double, y1: Double, x2: Double, y2: Double,
    ) {
        if (texture == null) {
            return
        }

        glEnable(GL_TEXTURE_2D)  // 重新启用纹理
        texture!!.bind()

        glColor4f(1f, 1f, 1f, alpha.toFloat())

        glBegin(GL_QUADS)
        glTexCoord2f(u1.toFloat(), v1.toFloat()); glVertex2f(x1.toFloat(), y2.toFloat())
        glTexCoord2f(u2.toFloat(), v1.toFloat()); glVertex2f(x2.toFloat(), y2.toFloat())
        glTexCoord2f(u2.toFloat(), v2.toFloat()); glVertex2f(x2.toFloat(), y1.toFloat())
        glTexCoord2f(u1.toFloat(), v2.toFloat()); glVertex2f(x1.toFloat(), y1.toFloat())
        glEnd()
        glDisable(GL_TEXTURE_2D)
    }

    fun render(x1: Double, y1: Double, x2: Double, y2: Double) {
        render(0.0, 0.0, 1.0, 1.0, x1, y1, x2, y2)
    }

    fun render(screenBounds: ScreenBounds) {
        render(0.0, 0.0, 1.0, 1.0, screenBounds.x1, screenBounds.y1, screenBounds.x2, screenBounds.y2)
    }

    fun cleanup() {
        texture = null
    }
}
