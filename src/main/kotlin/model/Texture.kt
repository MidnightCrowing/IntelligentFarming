package com.midnightcrowing.model

import org.lwjgl.opengl.ARBFramebufferObject
import org.lwjgl.opengl.GL46.*
import java.io.InputStream

/**
 * 纹理类，负责加载和绑定纹理，支持透明度。
 */
class Texture {
    val image: Image

    constructor(inputStream: InputStream) {
        image = Image.loadImage(inputStream)
    }

    constructor(image: Image) {
        this.image = image
    }

    var id: Int = 0
        private set

    fun load() {
        id = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, id)

        // 纹理参数设置
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST) // 纹理缩小
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST) // 纹理放大

        // 传输纹理数据到 OpenGL，确保支持透明度
        val format = if (image.hasAlphaChannel()) GL_RGBA else GL_RGB
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.width, image.height, 0, format, GL_UNSIGNED_BYTE, image.buffer)

        ARBFramebufferObject.glGenerateMipmap(GL_TEXTURE_2D) // 生成 Mipmap 以提高缩放质量
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    fun cleanup() {
        glDeleteTextures(id)
        image.cleanup()
        println("纹理 $id 已被删除")
    }
}