package org.intelligentfarming.resource

import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

/**
 * 存储图片数据
 */
data class ImageData(val buffer: ByteBuffer, val width: Int, val height: Int) {
    fun cleanup() {
        STBImage.stbi_image_free(buffer)
    }
}