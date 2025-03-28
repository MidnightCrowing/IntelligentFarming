package com.midnightcrowing.render

import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.Texture
import org.lwjgl.opengl.GL46.*

class NineSliceRenderer(
    var texture: Texture,
    var textureBorder: Float, // 纹理边框宽度（像素单位）
    var vertexBorder: Float,  // 屏幕渲染边框宽度（屏幕单位）
) {
    private val texWidth = texture.image.width
    private val texHeight = texture.image.height

    var alpha: Double = 1.0  // 默认不透明

    fun render(x: Float, y: Float, width: Float, height: Float) {
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

        // 计算九宫格的纹理坐标（转换为 0-1 范围）
        fun toTexCoord(texPixel: Float, isU: Boolean): Float {
            return if (isU) texPixel / texWidth else 1f - (texPixel / texHeight)
        }

        val u0 = toTexCoord(0f, true)
        val u1 = toTexCoord(textureBorder, true)
        val u2 = toTexCoord(texWidth - textureBorder, true)
        val u3 = toTexCoord(texWidth.toFloat(), true)

        val v0 = toTexCoord(0f, false)
        val v1 = toTexCoord(textureBorder, false)
        val v2 = toTexCoord(texHeight - textureBorder, false)
        val v3 = toTexCoord(texHeight.toFloat(), false)

        // 九宫格顶点和对应纹理坐标
        val slices = listOf(
            Quad(x0, y2, x1, y3, u0, v2, u1, v3), // 左上角
            Quad(x2, y2, x3, y3, u2, v2, u3, v3), // 右上角
            Quad(x0, y0, x1, y1, u0, v0, u1, v1), // 左下角
            Quad(x2, y0, x3, y1, u2, v0, u3, v1), // 右下角

            Quad(x1, y2, x2, y3, u1, v2, u2, v3), // 上边
            Quad(x1, y0, x2, y1, u1, v0, u2, v1), // 下边
            Quad(x0, y1, x1, y2, u0, v1, u1, v2), // 左边
            Quad(x2, y1, x3, y2, u2, v1, u3, v2), // 右边

            Quad(x1, y1, x2, y2, u1, v1, u2, v2)  // 中心区域
        )

        // 绘制所有九宫格部分
        slices.forEach { drawSlice(it) }

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
    )

    /**
     * 绘制九宫格中的一个部分
     */
    private fun drawSlice(q: Quad) {
        glTexCoord2f(q.u1, q.v1); glVertex2f(q.x1, q.y1) // 左上角
        glTexCoord2f(q.u2, q.v1); glVertex2f(q.x2, q.y1) // 右上角
        glTexCoord2f(q.u2, q.v2); glVertex2f(q.x2, q.y2) // 右下角
        glTexCoord2f(q.u1, q.v2); glVertex2f(q.x1, q.y2) // 左下角
    }
}
