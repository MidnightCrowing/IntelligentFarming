package com.midnightcrowing.resource

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.io.InputStream

/**
 * 纹理加载辅助类
 */
object TextureLoader {
    fun loadImage(inputStream: InputStream): ImageData {
        // 读取 InputStream 到 ByteBuffer
        val byteArray = inputStream.readBytes()
        val buffer = MemoryUtil.memAlloc(byteArray.size).put(byteArray)
        buffer.flip()  // 准备 ByteBuffer 进行读取

        val stack = MemoryStack.stackPush()
        val width = stack.mallocInt(1)
        val height = stack.mallocInt(1)
        val channels = stack.mallocInt(1)

        STBImage.stbi_set_flip_vertically_on_load(true)
        val imageBuffer = STBImage.stbi_load_from_memory(buffer, width, height, channels, 4)
            ?: throw RuntimeException("无法加载图片: ${STBImage.stbi_failure_reason()} $inputStream")

        return ImageData(imageBuffer, width.get(), height.get())
    }
}
