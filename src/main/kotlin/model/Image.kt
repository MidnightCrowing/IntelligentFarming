package com.midnightcrowing.model

import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

/**
 * 存储图片数据
 */
data class Image(val buffer: ByteBuffer, val width: Int, val height: Int) {
    fun hasAlphaChannel(): Boolean {
        val pixelSize = buffer.capacity() / (width * height)
        return pixelSize == 4 // 如果每个像素有 4 个字节（RGBA），说明有透明度
    }

    fun cleanup() {
        STBImage.stbi_image_free(buffer)
    }
}