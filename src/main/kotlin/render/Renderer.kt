package com.midnightcrowing.render

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glTexCoord2f
import org.lwjgl.opengl.GL11.glVertex2f

/**
 * 渲染器，负责渲染场景
 */
class Renderer(private val texture: Texture) {
    init {
        GL11.glEnable(GL11.GL_TEXTURE_2D)
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

    fun cleanup() {
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        texture.cleanup()
    }
}