package org.intelligentfarming.utils

import org.intelligentfarming.resource.ImageData

/**
 * 图片相关的工具方法
 */
object ImageUtils {
    /**
     * 判断图片是否包含透明通道
     */
    fun hasAlphaChannel(image: ImageData): Boolean {
        val pixelSize = image.buffer.capacity() / (image.width * image.height)
        return pixelSize == 4 // 如果每个像素有 4 个字节（RGBA），说明有透明度
    }
}