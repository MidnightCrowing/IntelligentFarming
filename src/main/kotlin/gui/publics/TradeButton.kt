package com.midnightcrowing.gui.publics

import com.midnightcrowing.events.CustomEvent.MouseMoveEvent
import com.midnightcrowing.gui.bases.Button
import com.midnightcrowing.gui.bases.Widget
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.model.item.ItemRegistry
import com.midnightcrowing.model.item.ItemStack
import com.midnightcrowing.model.trade.TradeRecipe
import com.midnightcrowing.renderer.ItemRenderer
import com.midnightcrowing.renderer.TextureRenderer
import com.midnightcrowing.resource.TextureResourcesEnum

class TradeButton(parent: Widget, val tradeRecipe: TradeRecipe) : Button(parent) {
    companion object {
        private const val BASE_WIDTH = 351
        private const val BASE_HEIGHT = 79

        private val BASE_BUY_1_BOUNDS = ScreenBounds(x1 = 27.0, y1 = 13.0, x2 = 78.0, y2 = 64.0)
        private val BASE_BUY_2_BOUNDS = ScreenBounds(x1 = 112.0, y1 = 13.0, x2 = 163.0, y2 = 64.0)
        private val BASE_SELL_BOUNDS = ScreenBounds(x1 = 280.0, y1 = 13.0, x2 = 331.0, y2 = 64.0)
        private val BASE_TRADE_ARROW_BOUNDS = ScreenBounds(x1 = 219.0, y1 = 20.0, x2 = 259.0, y2 = 56.0)
    }

    private val tradeArrowRender = TextureRenderer(TextureResourcesEnum.GUI_TRADE_BUTTON_ABLE_TO_TRADE.texture)

    var isTradeable: Boolean = true
        set(value) {
            field = value
            tradeArrowRender.texture =
                if (value) TextureResourcesEnum.GUI_TRADE_BUTTON_ABLE_TO_TRADE.texture
                else TextureResourcesEnum.GUI_TRADE_BUTTON_UNABLE_TO_TRADE.texture
        }

    // 鼠标位置
    private var mousePosition: Point = Point.EMPTY
    private var mouseHoverItem: ItemRenderer? = null

    private val buy1Item: ItemRenderer? by lazy { setItem(tradeRecipe.buy, BASE_BUY_1_BOUNDS) }
    private val buy2Item: ItemRenderer? by lazy { setItem(tradeRecipe.buyB, BASE_BUY_2_BOUNDS) }
    private val sellItem: ItemRenderer? by lazy { setItem(tradeRecipe.sell, BASE_SELL_BOUNDS) }

    /**
     * 根据父容器大小计算实际位置
     */
    private fun ScreenBounds.calculateRelativeBounds(): ScreenBounds {
        val scaleX = widgetBounds.width / BASE_WIDTH
        val scaleY = widgetBounds.height / BASE_HEIGHT
        return ScreenBounds(x1 * scaleX, y1 * scaleY, x2 * scaleX, y2 * scaleY)
    }

    /**
     * 通用方法，用于设置物品位置
     */
    private fun setItem(stack: ItemStack, bounds: ScreenBounds): ItemRenderer? {
        return ItemRegistry.createItemRender(stack.id, this)?.apply {
            place(bounds.calculateRelativeBounds() + widgetBounds.startPoint)
        }
    }

    fun onParentMouseMove(e: MouseMoveEvent) {
        val pos = Point(e.x, e.y)
        mousePosition = pos
        mouseHoverItem = when {
            (BASE_BUY_1_BOUNDS.calculateRelativeBounds() + widgetBounds.startPoint).contains(pos) -> buy1Item
            (BASE_BUY_2_BOUNDS.calculateRelativeBounds() + widgetBounds.startPoint).contains(pos) -> buy2Item
            (BASE_SELL_BOUNDS.calculateRelativeBounds() + widgetBounds.startPoint).contains(pos) -> sellItem
            else -> null
        }
    }

    override fun onMouseLeave() {
        super.onMouseLeave()
        mouseHoverItem = null
    }

    override fun place(bounds: ScreenBounds) {
        super.place(bounds)
        buy1Item?.place(BASE_BUY_1_BOUNDS.calculateRelativeBounds() + widgetBounds.startPoint)
        buy2Item?.place(BASE_BUY_2_BOUNDS.calculateRelativeBounds() + widgetBounds.startPoint)
        sellItem?.place(BASE_SELL_BOUNDS.calculateRelativeBounds() + widgetBounds.startPoint)
    }

    override fun doRender() {
        super.doRender()
        tradeArrowRender.render(BASE_TRADE_ARROW_BOUNDS.calculateRelativeBounds() + widgetBounds.startPoint)
        buy1Item?.render(tradeRecipe.buy.count)
        buy2Item?.render(tradeRecipe.buyB.count)
        sellItem?.render(tradeRecipe.sell.count)

        mouseHoverItem?.renderTooltip(mousePosition.x, mousePosition.y)
    }
}