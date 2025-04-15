package com.midnightcrowing.utils

object FPSCounter {
    private var lastTime = System.nanoTime()
    private var frames = 0
    var fps = 0
        private set

    fun update() {
        val currentTime = System.nanoTime()
        frames++
        if (currentTime - lastTime >= 1_000_000_000) {
            fps = frames
            frames = 0
            lastTime = currentTime
        }
    }
}