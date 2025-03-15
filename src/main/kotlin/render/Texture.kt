package com.midnightcrowing.render

import com.midnightcrowing.resource.ResourcesLoader
import org.lwjgl.opengl.ARBFramebufferObject.glGenerateMipmap
import org.lwjgl.opengl.GL11.*
import java.io.InputStream


/**
 * 纹理管理类，负责加载和绑定纹理，支持透明度。
 */
class Texture(private val inputStream: InputStream) {
    var id: Int = 0
        private set

    fun load() {
        val image = ResourcesLoader.loadImage(inputStream)

        id = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, id)

        // 纹理参数设置
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST) // 缩小过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST) // 放大过滤

        // 开启 OpenGL 透明度混合
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        // 传输纹理数据到 OpenGL，支持透明度
        val format = if (image.hasAlphaChannel()) GL_RGBA else GL_RGB
        glTexImage2D(GL_TEXTURE_2D, 0, format, image.width, image.height, 0, format, GL_UNSIGNED_BYTE, image.buffer)

        glGenerateMipmap(GL_TEXTURE_2D) // 生成 Mipmap 以提高缩放质量

        image.cleanup()
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    fun cleanup() {
        glDeleteTextures(id)
    }
}
