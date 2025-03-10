package org.intelligentfarming.resource

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack

/**
 * 纹理加载辅助类
 */
object TextureLoader {
    fun loadImage(filePath: String): ImageData {
        val stack = MemoryStack.stackPush()
        val width = stack.mallocInt(1)
        val height = stack.mallocInt(1)
        val channels = stack.mallocInt(1)

        STBImage.stbi_set_flip_vertically_on_load(true)
        val buffer = STBImage.stbi_load(filePath, width, height, channels, 4)
            ?: throw RuntimeException("无法加载图片: ${STBImage.stbi_failure_reason()} $filePath")

        return ImageData(buffer, width.get(), height.get())
    }
}