package com.midnightcrowing.texture

import java.lang.ref.Cleaner

object TextureCleaner {
    val cleaner: Cleaner = Cleaner.create()
}