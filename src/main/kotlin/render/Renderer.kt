package com.midnightcrowing.render

import com.midnightcrowing.utils.NdcBounds
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glTexCoord2f
import org.lwjgl.opengl.GL11.glVertex2f
import java.io.InputStream

/**
 * 从图片资源中创建一个渲染器
 * @param inputStream 图片资源的输入流
 */
fun createRenderer(inputStream: InputStream?): Renderer {
    if (inputStream == null) {
        throw IllegalArgumentException("inputStream: 不能为空")
    }
    return Renderer(Texture(inputStream).apply { load() })
}

/**
 * 渲染器，负责渲染场景
 */
class Renderer(private var texture: Texture) {  // 允许动态修改 Texture
    init {
        GL11.glEnable(GL11.GL_TEXTURE_2D)
        bindTexture()  // 绑定当前纹理
    }

    fun setTexture(newTexture: Texture) {
        if (texture.id != newTexture.id) {  // 避免重复绑定相同纹理
            texture = newTexture
            bindTexture()
        }
    }

    private fun bindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id)
    }

    fun render(x1: Float, y1: Float, x2: Float, y2: Float) {
        texture.bind()

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
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        texture.cleanup()
    }
}
