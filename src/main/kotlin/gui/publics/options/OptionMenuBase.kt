package com.midnightcrowing.gui.publics.options

import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.gui.bases.button.Button
import com.midnightcrowing.gui.layouts.ButtonLayout
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.utils.LayoutScaler

open class OptionMenuBase(parent: Options, title: String) : Widget(parent) {
    protected val controller: OptionsController = parent.controller

    private val titleRenderer: TextRenderer = TextRenderer(window.nvg).apply { text = title }

    protected val buttonLayout = ButtonLayout(this, btnGapX = 0.01, btnGapY = 0.02, offsetY = -0.5)

    protected val finishButton = Button(buttonLayout).apply {
        text = "完成"; textSpacing = 2.0
        onClickCallback = { controller.backToMenu() }
    }

    init {
        buttonLayout.addButton(8, finishButton)
    }

    override fun update() = buttonLayout.update()

    open fun place(width: Int, height: Int) {
        super.place(0.0, 0.0, width.toDouble(), height.toDouble())

        titleRenderer.fontSize = LayoutScaler.scaleValue(parentWidth)
        titleRenderer.x = width / 2.0
        titleRenderer.y = height * 0.15

        buttonLayout.place(width, height)
    }

    override fun place(bounds: ScreenBounds) = this.place(bounds.x2.toInt(), bounds.y2.toInt())

    override fun doRender() {
        titleRenderer.render()
        buttonLayout.render()
    }

    override fun doCleanup() = buttonLayout.cleanup()
}