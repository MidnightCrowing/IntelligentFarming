package com.midnightcrowing.model

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.io.InputStream
import java.nio.ByteBuffer

/**
 * 存储图片数据
 */
data class Image(val buffer: ByteBuffer, val width: Int, val height: Int) {
    companion object {
        fun loadImage(inputStream: InputStream): Image {
            // 读取 InputStream 到 ByteBuffer
            val byteArray = inputStream.readBytes()
            val buffer = MemoryUtil.memAlloc(byteArray.size).put(byteArray)
            buffer.flip()  // 准备 ByteBuffer 进行读取

            // 使用堆内存分配
            val width = MemoryUtil.memAllocInt(1)
            val height = MemoryUtil.memAllocInt(1)
            val channels = MemoryUtil.memAllocInt(1)

            STBImage.stbi_set_flip_vertically_on_load(true)
            val imageBuffer = STBImage.stbi_load_from_memory(buffer, width, height, channels, 4)
                ?: throw RuntimeException("无法加载图片: ${STBImage.stbi_failure_reason()} $inputStream")

            return Image(imageBuffer, width.get(), height.get())
        }
    }

    fun hasAlphaChannel(): Boolean {
        val pixelSize = buffer.capacity() / (width * height)
        return pixelSize == 4 // 如果每个像素有 4 个字节（RGBA），说明有透明度
    }

    fun cleanup() {
        STBImage.stbi_image_free(buffer)
    }
}