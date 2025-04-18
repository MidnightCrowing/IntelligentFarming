package com.midnightcrowing.gui.scenes.farmScene

import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.button.Button
import com.midnightcrowing.gui.layouts.ButtonLayout
import com.midnightcrowing.renderer.RectangleRenderer
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.utils.LayoutScaler
import kotlin.reflect.KClass

class EscMenus(parent: Widget, val controller: FarmController, z: Int? = null) : Widget(parent, z) {
    private val buttonLayout = ButtonLayout(this, offsetY = -0.25, btnHeight = 0.065, btnGapX = 0.013, btnGapY = 0.015)
    private val menuButtons = listOf(
        Button(buttonLayout).apply {
            text = "回到游戏"; textSpacing = 2.0; onClickCallback = { controller.backToGame() }
        },
        Button(buttonLayout).apply {
            text = "进度"; textSpacing = 2.0; onClickCallback = { controller.openAdvancements() }
        },
        Button(buttonLayout).apply {
            text = "统计信息"; textSpacing = 2.0; onClickCallback = { controller.openStatistics() }
        },
        Button(buttonLayout).apply {
            text = "选项…"; textSpacing = 2.0; onClickCallback = { controller.openOptions() }
        },
        Button(buttonLayout).apply {
            text = "提供反馈"; textSpacing = 2.0; onClickCallback = { controller.provideFeedback() }
        },
        Button(buttonLayout).apply {
            text = "不保存并退回到标题屏幕"; textSpacing = 2.0; onClickCallback = { controller.backToTitle() }
        }
    )
    private val titleRenderer: TextRenderer = TextRenderer(window.nvg).apply { text = "游戏菜单" }
    private val backgroundRender: RectangleRenderer = RectangleRenderer(
        color = floatArrayOf(0f, 0f, 0f, 0.73f),
    )

    init {
        buttonLayout.addButton(1, menuButtons[0])
        buttonLayout.addButton(2, menuButtons[1])
        buttonLayout.addButton(2, menuButtons[2])
        buttonLayout.addButton(3, menuButtons[3])
        buttonLayout.addButton(3, menuButtons[4])
        buttonLayout.addButton(4, menuButtons[5])
    }

    override fun containsPoint(x: Double, y: Double, event: KClass<out Event>?): Boolean = true

    override fun onClick(e: MouseClickEvent) {}

    override fun onRightClick(e: MouseRightClickEvent) {}

    override fun onMousePress(e: MousePressedEvent) {}

    override fun onMouseRelease(e: MouseReleasedEvent) {}

    override fun onMouseMove(e: MouseMoveEvent) {}

    override fun onMouseScroll(e: MouseScrollEvent) {}

    fun place(width: Int, height: Int) {
        super.place(0.0, 0.0, width.toDouble(), height.toDouble())

        backgroundRender.x2 = width.toDouble()
        backgroundRender.y2 = height.toDouble()

        titleRenderer.fontSize = LayoutScaler.scaleValue(parentWidth)
        titleRenderer.x = width / 2.0
        titleRenderer.y = height * 0.25

        buttonLayout.place(width, height)
    }

    override fun update() = buttonLayout.update()

    override fun doRender() {
        backgroundRender.render()
        titleRenderer.render()
        buttonLayout.render()
    }
}