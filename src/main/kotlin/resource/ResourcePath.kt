package com.midnightcrowing.resource

import java.io.InputStream

/**
 * 资源路径管理工具
 */
object ResourcePath {
    /**
     * 获取资源路径
     */
    fun getResources(path: String): InputStream? {
        return this::class.java.getResourceAsStream(path)
    }
}