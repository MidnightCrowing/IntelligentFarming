package com.midnightcrowing.render

import com.midnightcrowing.model.NdcBounds
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glTexCoord2f
import org.lwjgl.opengl.GL11.glVertex2f
import java.io.InputStream

/**
 * 从图片资源中创建一个渲染器
 * @param inputStream 图片资源的输入流
 */
fun createImageRenderer(inputStream: InputStream?): ImageRenderer {
    if (inputStream == null) {
        throw IllegalArgumentException("inputStream: 不能为空")
    }
    return ImageRenderer(Texture(inputStream).apply { load() })
}

/**
 * 渲染器，负责渲染场景
 */
class ImageRenderer {
    private var texture: Texture? = null
    private var alpha: Float = 1.0f  // 默认不透明

    constructor()

    constructor(texture: Texture) {
        this.texture = texture
        bindTexture()
    }

    fun setTexture(newTexture: Texture) {
        if (texture?.id != newTexture.id) {
            texture = newTexture
            bindTexture()
        }
    }

    fun setAlpha(alpha: Float) {
        this.alpha = alpha
    }

    private fun bindTexture() {
        texture?.let {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, it.id)
        }
    }

    fun render(x1: Float, y1: Float, x2: Float, y2: Float) {
        if (texture == null) {
            return
        }

        texture!!.bind()

        GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha)

        GL11.glBegin(GL11.GL_QUADS)
        glTexCoord2f(0f, 0f); glVertex2f(x1, y2)
        glTexCoord2f(1f, 0f); glVertex2f(x2, y2)
        glTexCoord2f(1f, 1f); glVertex2f(x2, y1)
        glTexCoord2f(0f, 1f); glVertex2f(x1, y1)
        GL11.glEnd()
    }

    fun render(ndcBounds: NdcBounds) {
        render(ndcBounds.left, ndcBounds.top, ndcBounds.right, ndcBounds.bottom)
    }

    fun cleanup() {
        texture?.cleanup()
    }
}
