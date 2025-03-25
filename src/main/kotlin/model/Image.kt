package com.midnightcrowing.model

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.awt.Color
import java.io.InputStream
import java.nio.ByteBuffer
import kotlin.random.Random

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

    /**
     * 检查是否带有 Alpha 透明通道
     */
    fun hasAlphaChannel(): Boolean {
        val pixelSize = buffer.capacity() / (width * height)
        return pixelSize == 4 // 如果每个像素有 4 个字节（RGBA），说明有透明度
    }

    fun extractSquareRegion(size: Int): Image {
        val pixelSize = 4 // Assuming RGBA
        val maxAttempts = 999
        var attempts = 0
        while (attempts < maxAttempts) {
            val startX = Random.nextInt(0, width - size - 100)
            val startY = Random.nextInt(0, height - size - 100)
            val regionBuffer = MemoryUtil.memAlloc(size * size * pixelSize)
            var transparentPixelCount = 0
            for (y in startY until startY + size) {
                for (x in startX until startX + size) {
                    val index = (y * width + x) * pixelSize
                    val r = buffer.get(index)
                    val g = buffer.get(index + 1)
                    val b = buffer.get(index + 2)
                    val a = buffer.get(index + 3)

                    regionBuffer.put(r).put(g).put(b).put(a)

                    if (a.toInt() == 0) {
                        transparentPixelCount++
                    }
                }
            }
            regionBuffer.flip()

            val totalPixels = size * size
            val transparentRatio = transparentPixelCount.toDouble() / totalPixels

            // 如果透明像素比例小于 20%，则返回该区域
            if (transparentRatio > 0.2) {
                return Image(regionBuffer, size, size)
            } else {
                regionBuffer.clear()
                MemoryUtil.memFree(regionBuffer)
            }
            attempts++
        }

        throw RuntimeException("Unable to find a suitable region with less than 20% transparency after $maxAttempts attempts")
    }

    fun getRandomColors(count: Int = 5): List<Color> {
        val colors = mutableListOf<Color>()
        val maxIndex = buffer.capacity() - 4 // 避免访问超界
        buffer.rewind()

        while (colors.size < count) {
            val index = Random.nextInt(0, width * height) * 4

            if (index < 0 || index + 3 >= maxIndex) {
                println("Index out of bounds: $index")
                continue // 跳过当前循环，避免访问越界
            }

            val r = buffer.get(index).toInt() and 0xFF
            val g = buffer.get(index + 1).toInt() and 0xFF
            val b = buffer.get(index + 2).toInt() and 0xFF
            val a = buffer.get(index + 3).toInt() and 0xFF

            if (a > 50) { // 过滤透明像素
                colors.add(Color(r, g, b))
            }
        }
        return colors
    }

    /**
     * 释放显存
     */
    fun cleanup() {
        STBImage.stbi_image_free(buffer)
    }
}