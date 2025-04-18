package com.midnightcrowing.texture

import com.midnightcrowing.resource.ResourceLocation
import com.midnightcrowing.resource.ResourceManager
import java.nio.ByteBuffer

object TextureManager {
    private val textureCache = HashMap<ResourceLocation, Texture>()

    // https://zh.minecraft.wiki/w/%E6%97%A0%E6%95%88%E7%BA%B9%E7%90%86%E4%B8%8E%E6%A8%A1%E5%9E%8B
    private val missingTexture: Texture by lazy {
        Texture(generateMissingTextureImage(), "missing texture")
    }

    /**
     * 绑定纹理
     * @param location 纹理位置
     */
    fun bindTexture(location: ResourceLocation) {
        getTexture(location)?.bind()
    }

    /**
     * 获取纹理，如果缓存中没有，则尝试加载
     */
    fun getTexture(location: ResourceLocation): Texture? {
        return textureCache[location] ?: loadTexture(location)?.also {
            textureCache[location] = it
        }
    }

    /**
     * 加载纹理，如果失败则使用全局 fallback
     */
    private fun loadTexture(location: ResourceLocation): Texture? {
        val inputStream = try {
            ResourceManager.getInputStream(location)
                ?: run {
                    System.err.println("Texture not found: $location, using fallback.")
                    return missingTexture
                }
        } catch (e: Exception) {
            System.err.println("Error loading texture: ${e.message}, using fallback.")
            return missingTexture
        }

        return Texture(Image.loadImage(inputStream), location.toString())
    }

    /**
     * 生成一个紫黑交错的 missing texture 图像
     */
    private fun generateMissingTextureImage(size: Int = 4): Image {
        val buffer = ByteBuffer.allocateDirect(size * size * 4)
        val purple = byteArrayOf(0xF8.toByte(), 0x00, 0xF8.toByte(), 0xFF.toByte())
        val black = byteArrayOf(0x00, 0x00, 0x00, 0xFF.toByte())

        for (y in 0 until size) {
            for (x in 0 until size) {
                val isPurple = (x < size / 2) xor (y < size / 2)
                buffer.put(if (isPurple) purple else black)
            }
        }

        buffer.flip()
        return Image(buffer, size, size)
    }

    /**
     * 清理指定纹理
     */
    fun cleanup(location: ResourceLocation) {
        textureCache[location]?.cleanup()
        textureCache.remove(location)
    }

    /**
     * 清理所有纹理（不包括全局 missing）
     */
    fun cleanupAll() {
        textureCache.values
            .filterNot { it === missingTexture } // 避免清除 fallback
            .forEach { it.cleanup() }

        textureCache.clear()
    }
}
