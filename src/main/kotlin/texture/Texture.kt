package com.midnightcrowing.texture

import org.lwjgl.opengl.ARBFramebufferObject
import org.lwjgl.opengl.GL46.*
import java.lang.ref.Cleaner
import java.util.concurrent.atomic.AtomicBoolean


class Texture(image: Image, val name: String) {
    var id: Int = 0
        private set

    val width: Int = image.width
    val height: Int = image.height

    private var cleanable: Cleaner.Cleanable? = null
    private val isCleaned = AtomicBoolean(false) // 标记是否被主动清理

    init {
        load(image)

        // 注册清理器
        cleanable = TextureCleaner.cleaner.register(this, CleanupAction(id, name, isCleaned))
//        println("Texture $name (id: $id) loaded.")
    }

    fun load(image: Image) {
        id = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, id)

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        val format = if (image.hasAlphaChannel()) GL_RGBA else GL_RGB
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.width, image.height, 0, format, GL_UNSIGNED_BYTE, image.buffer)

        ARBFramebufferObject.glGenerateMipmap(GL_TEXTURE_2D)

        image.cleanup()
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, id)
    }

    fun cleanup() {
        if (isCleaned.compareAndSet(false, true)) {
            glDeleteTextures(id)
//            println("Texture $name (id: $id) deleted.")
            cleanable?.clean()
            cleanable = null
        }
    }

    private class CleanupAction(
        private val textureId: Int,
        private val textureName: String,
        private val isCleaned: AtomicBoolean,
    ) : Runnable {
        override fun run() {
            if (!isCleaned.get()) {
                TextureErrorTracker.report(textureName, textureId)
            }
        }
    }
}