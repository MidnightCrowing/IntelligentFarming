//package com.midnightcrowing.resource
//
///**
// * 资源加载辅助类
// */
//object ResourcesLoader {
//
//    // 添加字体缓存防止重复加载
////    private val fontCache = mutableMapOf<String, ByteBuffer>()
//
////    fun loadFont(fontName: String, inputStream: InputStream) {
////        // 检查缓存
////        if (fontCache.containsKey(fontName)) {
////            return
////        }
////
////        val fontData = readStreamToByteBuffer(inputStream)
////
////        // 使用堆内存分配（LWJGL的memAlloc默认使用堆外内存）
////        val fontMem = MemoryUtil.memAlloc(fontData.remaining()).apply {
////            put(fontData)
////            flip()
////        }
////
////        // 加入缓存
////        fontCache[fontName] = fontMem
////
////        // 创建字体（NanoVG会复制数据，可以立即释放）
////        try {
////            if (nvgCreateFontMem(wi, fontName, fontMem, true) == -1) {
////                throw RuntimeException("Failed to load font from memory")
////            }
////        } finally {
////            // 注意：根据NanoVG文档，当stash设置为1时会复制数据，因此可以安全释放
////            MemoryUtil.memFree(fontMem)
////            fontCache.remove(fontName)
////        }
////    }
//
//
////    /**
////     * 载入字体
////     * @param fontName 字体名称
////     * @param fontPath 字体文件路径（如 TTF）
////     */
////    fun loadFont(fontName: String, fontPath: String): Boolean {
////        return nvgCreateFont(nvg, fontName, fontPath) != -1
////    }
//}
//
///**
// * 将 InputStream 读取到 ByteBuffer
// */
////private fun readStreamToByteBuffer(stream: InputStream): ByteBuffer {
////    return stream.use { input ->
////        val output = ByteArrayOutputStream()
////        val buffer = ByteArray(81920) // 使用固定缓冲区
////
////        // 传统读取方式
////        var bytesRead: Int
////        while (input.read(buffer).also { bytesRead = it } != -1) {
////            output.write(buffer, 0, bytesRead)
////        }
////
////        // 转换为堆内存缓冲
////        ByteBuffer.wrap(output.toByteArray()).apply {
////            order(ByteOrder.nativeOrder())
////        }
////    }
////}
