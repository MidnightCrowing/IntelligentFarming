package com.midnightcrowing.renderer

import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.texture.TextureManager
import org.lwjgl.opengl.GL46.*


/**
 * 渲染器，负责渲染场景
 */
class TextureRenderer(
    var location: ResourceLocation? = null,
    var alpha: Double = 1.0,
) {
    fun render(
        u1: Double, v1: Double, u2: Double, v2: Double,
        x1: Double, y1: Double, x2: Double, y2: Double,
    ) {
        if (location == null) {
            return
        }

        TextureManager.bindTexture(location!!)

        glEnable(GL_TEXTURE_2D)  // 重新启用纹理

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
}
