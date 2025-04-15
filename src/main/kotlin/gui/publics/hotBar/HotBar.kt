package com.midnightcrowing.gui.publics.hotBar

import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemRenderCache
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum
import com.midnightcrowing.utils.GameTick
import org.lwjgl.glfw.GLFW
import kotlin.reflect.KClass

class HotBar(parent: Widget, private val controller: HotBarController) : Widget(parent) {
    companion object {
        // 基础尺寸常量
        private const val BASE_WIDTH = 364
        private const val BASE_HEIGHT = 44
        private const val BASE_GRID_LEFT_BORDER = 8
        private const val BASE_GRID_TOP_BORDER = 8
        private const val BASE_GRID_BOTTOM_BORDER = 37
        private const val BASE_GRID_WIDTH = 29
        private const val BASE_GRID_GAP = 11
        private const val BASE_CHECKBOX_SIZE = 7
        private const val SCALE_BASE = 700.0

        // 计算缩放比例
        private val SCALED: Double by lazy { SCALE_BASE / BASE_WIDTH }
        val SCALED_WIDTH by lazy { BASE_WIDTH * SCALED }
        val SCALED_HEIGHT by lazy { BASE_HEIGHT * SCALED }
        val GRID_LEFT_BORDER by lazy { BASE_GRID_LEFT_BORDER * SCALED }
        val GRID_TOP_BORDER by lazy { BASE_GRID_TOP_BORDER * SCALED }
        val GRID_BOTTOM_BORDER by lazy { BASE_GRID_BOTTOM_BORDER * SCALED }
        val GRID_WIDTH by lazy { BASE_GRID_WIDTH * SCALED }
        val GRID_GAP by lazy { BASE_GRID_GAP * SCALED }
        val CHECKBOX_SIZE by lazy { BASE_CHECKBOX_SIZE * SCALED }
    }

    // 渲染器
    override val renderer: TextureRenderer = TextureRenderer(TextureResourcesEnum.GUI_HOT_BAR.texture)
    private val itemLabelRenderer: TextRenderer = TextRenderer(window.nvg).apply { fontSize = 20.0 }

    // 物品选中框
    val itemCheckBox: ItemCheckBox = ItemCheckBox(this)

    // 物品缓存，避免每次渲染时重复创建
    private val itemRenderCache: ItemRenderCache = ItemRenderCache(this)

    // 上次呈现文本的时间
    private var textRenderTime: Long = GameTick.tick

    // 网格起始坐标
    private var gridStartX: Double = 0.0
    private var gridStartY: Double = 0.0
    private var gridEndY: Double = 0.0

    init {
        controller.init(this)
    }

    fun setItemLabelText(text: String?) {
        if (text == null) {
            itemLabelRenderer.text = ""
            return
        }
        itemLabelRenderer.text = text
        textRenderTime = GameTick.tick
        itemLabelRenderer.textOpacity = 1.0
    }

    override fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        super.place(x1, y1, x2, y2)

        gridStartX = widgetBounds.x1 + GRID_LEFT_BORDER
        gridStartY = widgetBounds.y1 + GRID_TOP_BORDER
        gridEndY = widgetBounds.y1 + GRID_BOTTOM_BORDER

        itemLabelRenderer.x = (x1 + x2) / 2
        itemLabelRenderer.y = y1 - 30
        itemCheckBox.place(getGridBoundsWithCheckbox(controller.selectedGridId))
    }

    // 计算网格边界
    private fun calculateGridBounds(id: Int): ScreenBounds {
        require(id in 0..8) { "id must be between 0 and 8" }

        val startX: Double = gridStartX + (GRID_WIDTH + GRID_GAP) * id
        val endX: Double = startX + GRID_WIDTH
        val startY: Double = gridStartY
        val endY: Double = gridEndY

        return ScreenBounds(startX, startY, endX, endY)
    }

    // 获取普通网格位置
    private fun getGridBounds(id: Int): ScreenBounds = calculateGridBounds(id)

    // 获取带选中框的网格位置
    fun getGridBoundsWithCheckbox(id: Int): ScreenBounds {
        val itemBounds = calculateGridBounds(id)
        return ScreenBounds(
            x1 = itemBounds.x1 - CHECKBOX_SIZE,
            y1 = itemBounds.y1 - CHECKBOX_SIZE - 3,
            x2 = itemBounds.x2 + CHECKBOX_SIZE,
            y2 = itemBounds.y2 + CHECKBOX_SIZE
        )
    }

    // 通过坐标获取网格ID
    private fun findGridCheckboxIdAt(x: Double): Int? =
        (0..8).firstOrNull { id ->
            val bounds = getGridBoundsWithCheckbox(id)
            x in bounds.x1..bounds.x2
        }

    override fun containsPoint(x: Double, y: Double, event: KClass<out Event>?): Boolean {
        return if (event == MouseScrollEvent::class) {
            true
        } else {
            super.containsPoint(x, y, event)
        }
    }

    override fun onClick(e: MouseClickEvent) {
        findGridCheckboxIdAt(e.x)?.let {
            controller.selectedGridId = it
        }
    }

    override fun onMouseScroll(e: MouseScrollEvent) {
        if (e.offsetY < 0) {
            controller.selectedGridId = (controller.selectedGridId + 1) % 9
        } else if (e.offsetY > 0) {
            controller.selectedGridId = (controller.selectedGridId + 8) % 9
        }
    }

    override fun onKeyPress(e: KeyPressedEvent): Boolean {
        var selectedGridId = controller.selectedGridId
        when (e.key) {
            in GLFW.GLFW_KEY_1..GLFW.GLFW_KEY_9 -> {
                val keyIndex = e.key - GLFW.GLFW_KEY_1
                selectedGridId = keyIndex
            }

            GLFW.GLFW_KEY_LEFT -> selectedGridId = (selectedGridId + 8) % 9
            GLFW.GLFW_KEY_RIGHT -> selectedGridId = (selectedGridId + 1) % 9
            else -> return true
        }
        controller.selectedGridId = selectedGridId
        return true
    }

    override fun doRender() {
        val timeDiff = GameTick.tick - textRenderTime
        if (timeDiff < 3200) {
            if (timeDiff >= 3000) {
                itemLabelRenderer.textOpacity = (1 - (timeDiff - 3000).toDouble() / 200).coerceIn(0.0, 1.0)
            }
            itemLabelRenderer.render()
        }

        renderItems()

        itemCheckBox.render()
    }

    private fun renderItems() {
        controller.itemsList.forEachIndexed { index, item -> renderItem(item, getGridBounds(index)) }

        // 清理不在stack.id里的物品
        itemRenderCache.retainAll(controller.itemsList.map { it.id })
    }

    private fun renderItem(stack: ItemStack, position: ScreenBounds) {
        if (!stack.isEmpty()) {
            val item = itemRenderCache.getItemCache(stack.id)
            item?.place(position)
            item?.render(stack.count)
        }
    }

    override fun doCleanup() = itemCheckBox.cleanup()
}