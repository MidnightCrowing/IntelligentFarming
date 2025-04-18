package com.midnightcrowing.texture

import java.util.concurrent.atomic.AtomicInteger

object TextureErrorTracker {
    const val MAX_ERRORS: Int = 10 // 最大错误次数

    val errorCount = AtomicInteger(0)

    fun report(textureName: String, textureId: Int) {
        val count = errorCount.incrementAndGet()
        System.err.println("ERROR #$count: Texture $textureName (id: $textureId) was garbage collected without cleanup()!")
    }

    fun checkMaxErrors(): Boolean {
        return errorCount.get() >= MAX_ERRORS
    }
}
