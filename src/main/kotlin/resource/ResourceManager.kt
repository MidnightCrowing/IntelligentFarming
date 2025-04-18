package com.midnightcrowing.resource

import java.io.InputStream

object ResourceManager {
    fun getInputStream(location: ResourceLocation): InputStream? {
        return javaClass.getResourceAsStream(location.toAssetPath())
    }
}