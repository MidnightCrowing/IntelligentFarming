package com.midnightcrowing.utils

object GameTick {
    val lastTime = System.currentTimeMillis()
    var tick: Long = 0
        private set

    fun update() {
        tick = System.currentTimeMillis() - lastTime
    }
}