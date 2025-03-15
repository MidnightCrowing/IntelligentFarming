package com.midnightcrowing.farmings

data class FarmBounds(
    val isAvailable: Boolean,
    val screenBottom: Float,
    val screenTop: Float,
    val screenLeft: Float,
    val screenRight: Float,
    val screenBlockHeight: Float,
)