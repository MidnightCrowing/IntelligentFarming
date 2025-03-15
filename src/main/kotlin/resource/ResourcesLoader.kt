package com.midnightcrowing.resource

import com.midnightcrowing.model.Image
import com.midnightcrowing.render.NanoVGContext.vg
import org.lwjgl.nanovg.NanoVG.nvgCreateFont
import org.lwjgl.nanovg.NanoVG.nvgCreateFontMem
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryUtil
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * 资源加载辅助类
 */
object ResourcesLoader {
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

    // 添加字体缓存防止重复加载
    private val fontCache = mutableMapOf<String, ByteBuffer>()

    fun loadFont(fontName: String, inputStream: InputStream) {
        // 检查缓存
        if (fontCache.containsKey(fontName)) {
            return
        }

        val fontData = readStreamToByteBuffer(inputStream)

        // 使用堆内存分配（LWJGL的memAlloc默认使用堆外内存）
        val fontMem = MemoryUtil.memAlloc(fontData.remaining()).apply {
            put(fontData)
            flip()
        }

        // 加入缓存
        fontCache[fontName] = fontMem

        // 创建字体（NanoVG会复制数据，可以立即释放）
        try {
            if (nvgCreateFontMem(vg, fontName, fontMem, true) == -1) {
                throw RuntimeException("Failed to load font from memory")
            }
        } finally {
            // 注意：根据NanoVG文档，当stash设置为1时会复制数据，因此可以安全释放
            MemoryUtil.memFree(fontMem)
            fontCache.remove(fontName)
        }
    }


    /**
     * 载入字体
     * @param fontName 字体名称
     * @param fontPath 字体文件路径（如 TTF）
     */
    fun loadFont(fontName: String, fontPath: String) {
        if (nvgCreateFont(vg, fontName, fontPath) == -1) {
            throw RuntimeException("Failed to load font: $fontPath")
        }
    }
}

/**
 * 将 InputStream 读取到 ByteBuffer
 */
private fun readStreamToByteBuffer(stream: InputStream): ByteBuffer {
    return stream.use { input ->
        val output = ByteArrayOutputStream()
        val buffer = ByteArray(81920) // 使用固定缓冲区

        // 传统读取方式
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }

        // 转换为堆内存缓冲
        ByteBuffer.wrap(output.toByteArray()).apply {
            order(ByteOrder.nativeOrder())
        }
    }
}
