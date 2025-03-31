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

    fun render(x1: Double, y1: Double, x2: Double, y2: Double) {
        if (texture == null) {
            return
        }

        glEnable(GL_TEXTURE_2D)  // 重新启用纹理
        texture!!.bind()

        glColor4f(1f, 1f, 1f, alpha.toFloat())

        glBegin(GL_QUADS)
        glTexCoord2f(0f, 0f); glVertex2f(x1.toFloat(), y2.toFloat())
        glTexCoord2f(1f, 0f); glVertex2f(x2.toFloat(), y2.toFloat())
        glTexCoord2f(1f, 1f); glVertex2f(x2.toFloat(), y1.toFloat())
        glTexCoord2f(0f, 1f); glVertex2f(x1.toFloat(), y1.toFloat())
        glEnd()
        glDisable(GL_TEXTURE_2D)
    }

    fun render(screenBounds: ScreenBounds) {
        render(screenBounds.x1, screenBounds.y1, screenBounds.x2, screenBounds.y2)
    }

    fun cleanup() {
        texture = null
    }
}
