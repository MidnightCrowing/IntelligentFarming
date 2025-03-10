package org.intelligentfarming.resource

import java.io.File

/**
 * 资源路径管理工具
 */
object ResourcePath {
    private const val BUILD_PATH = "build/resources/main"
    private const val SRC_PATH = "resources"

    private val isBuild: Boolean = File(BUILD_PATH).exists()

    /**
     * 获取资源路径
     */
    fun getResources(path: String): String {
        return if (isBuild) "$BUILD_PATH/$path" else "$SRC_PATH/$path"
    }
}