package com.midnightcrowing.renderer

import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.Texture
import org.lwjgl.opengl.GL46.*
import kotlin.math.ceil
import kotlin.math.min

class NineSliceRenderer(
    var texture: Texture,
    var textureBorder: Float, // 纹理边框宽度（像素单位）
    var vertexBorder: Float,  // 屏幕渲染边框宽度（屏幕单位）
) {
    private val texWidth = texture.image.width
    private val texHeight = texture.image.height

    var alpha: Double = 1.0  // 默认不透明

    fun render(x: Float, y: Float, width: Float, height: Float) {
        val vertexBorder = min(vertexBorder, width - 1)

        glEnable(GL_TEXTURE_2D)
        texture.bind()
        glBegin(GL_QUADS)
        glColor4f(1f, 1f, 1f, alpha.toFloat())

        // 计算九宫格的屏幕坐标
        val x0 = x
        val x1 = x + vertexBorder
        val x2 = x + width - vertexBorder
        val x3 = x + width

        val y0 = y
        val y1 = y + vertexBorder
        val y2 = y + height - vertexBorder
        val y3 = y + height

        val u0 = 0f
        val u1 = textureBorder
        val u2 = texWidth - textureBorder
        val u3 = texWidth.toFloat()

        val v0 = 0f
        val v1 = textureBorder
        val v2 = texHeight - textureBorder
        val v3 = texHeight.toFloat()

        // 九宫格顶点和对应纹理坐标
        val slices = listOf(
            Quad(x0, y0, x1, y1, u0, v0, u1, v1), // 左上角
            Quad(x2, y0, x3, y1, u2, v0, u3, v1), // 右上角
            Quad(x0, y2, x1, y3, u0, v2, u1, v3), // 左下角
            Quad(x2, y2, x3, y3, u2, v2, u3, v3), // 右下角

            Quad(x1, y0, x2, y1, u1, v0, u2, v1), // 上边
            Quad(x1, y2, x2, y3, u1, v2, u2, v3), // 下边
            Quad(x0, y1, x1, y2, u0, v1, u1, v2), // 左边
            Quad(x2, y1, x3, y2, u2, v1, u3, v2), // 右边

            Quad(x1, y1, x2, y2, u1, v1, u2, v2)  // 中心区域
        )

        // 绘制所有九宫格部分
        slices.forEach { drawSlice(it, vertexBorder) }

        glEnd()
        glDisable(GL_TEXTURE_2D)
    }

    fun render(bounds: ScreenBounds) {
        render(
            x = bounds.x1.toFloat(),
            y = bounds.y1.toFloat(),
            width = bounds.width.toFloat(),
            height = bounds.height.toFloat()
        )
    }

    /**
     * 表示一个矩形区域（四个顶点），用于简化绘制
     */
    private data class Quad(
        val x1: Float, val y1: Float, val x2: Float, val y2: Float,
        val u1: Float, val v1: Float, val u2: Float, val v2: Float,
    ) {
        val vw get() = x2 - x1  // 渲染宽度
        val tw get() = u2 - u1  // 纹理宽度
        val vh get() = y2 - y1  // 渲染高度
        val th get() = v2 - v1  // 纹理高度
    }

    // 计算九宫格的纹理坐标（转换为 0-1 范围）
    fun toTexCoord(texPixel: Float, isU: Boolean): Float {
        return if (isU) texPixel / texWidth else 1f - (texPixel / texHeight)
    }

    /**
     * 绘制九宫格中的一个部分，采用复制纹理的方式进行渲染。
     */
    private fun drawSlice(q: Quad, vertexBorder: Float) {
        val uw = q.tw / textureBorder * vertexBorder  // 纹理缩放后的渲染宽度
        val uh = q.th / textureBorder * vertexBorder  // 纹理缩放后的渲染高度
        val rw = q.vw / uw                            // 计算渲染宽度与纹理宽度的比率
        val rh = q.vh / uh                            // 计算渲染高度与纹理高度的比率
        val nx = ceil(rw).toInt()                     // 计算需要绘制的水平切片数量
        val ny = ceil(rh).toInt()                     // 计算需要绘制的垂直切片数量

        for (i in 0 until nx) {
            for (j in 0 until ny) {
                val u1 = q.u1
                val u2 = if (i == nx - 1 && rw % 1 != 0f) q.u1 + rw % 1 * q.tw else q.u2
                val v1 = q.v1
                val v2 = if (j == ny - 1 && rh % 1 != 0f) q.v1 + rh % 1 * q.th else q.v2

                val x1 = q.x1 + i * uw
                val x2 = if (i == nx - 1) q.x2 else q.x1 + (i + 1) * uw
                val y1 = q.y1 + j * uh
                val y2 = if (j == ny - 1) q.y2 else q.y1 + (j + 1) * uh

                glTexCoord2f(toTexCoord(u1, true), toTexCoord(v1, false)); glVertex2f(x1, y1) // 左上角
                glTexCoord2f(toTexCoord(u2, true), toTexCoord(v1, false)); glVertex2f(x2, y1) // 右上角
                glTexCoord2f(toTexCoord(u2, true), toTexCoord(v2, false)); glVertex2f(x2, y2) // 右下角
                glTexCoord2f(toTexCoord(u1, true), toTexCoord(v2, false)); glVertex2f(x1, y2) // 左下角
            }
        }
    }
}
