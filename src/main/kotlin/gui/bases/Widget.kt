package com.midnightcrowing.gui.bases

import com.midnightcrowing.events.CustomEvent.*
import com.midnightcrowing.events.Event
import com.midnightcrowing.events.Event.WindowResizeEvent
import com.midnightcrowing.events.annotations.*
import com.midnightcrowing.model.Point
import com.midnightcrowing.model.ScreenBounds
import com.midnightcrowing.renderer.TextureRenderer
import kotlin.reflect.KClass

open class Widget {
    val window: Window
    val parent: Widget?

    protected open val renderer: TextureRenderer = TextureRenderer()
    val z: Int
    var widgetBounds: ScreenBounds = ScreenBounds.EMPTY
        private set
    private var visible: Boolean = true
    private val parentVisible: Boolean get() = parent?.visible != false && parent?.parentVisible != false
    open val isVisible: Boolean get() = visible && parentVisible

    constructor(window: Window, z: Int? = null) {
        this.window = window
        this.parent = null
        this.z = z ?: 0

        registerListeners()  // 注册监听器
    }

    constructor(parent: Widget, z: Int? = null) {
        this.window = parent.window
        this.parent = parent
        this.z = z ?: (parent.z + 1)

        registerListeners()  // 注册监听器
    }

    /**
     * 获取父组件的宽度
     */
    val parentWidth: Double get() = parent?.widgetBounds?.width ?: window.width.toDouble()

    /**
     * 判断给定坐标是否在组件范围内
     * @param x X 坐标
     * @param y Y 坐标
     * @param event 事件类型，可用于需要对特定事件进行处理的情况
     */
    open fun containsPoint(x: Double, y: Double, event: KClass<out Event>? = null): Boolean {
        return widgetBounds.contains(Point(x, y))
    }

    open fun setHidden(value: Boolean) {
        visible = !value
    }

    open fun toggleVisible() {
        visible = !visible
    }

    /**
     * 获取鼠标在窗口中的坐标
     *
     * @return 一个包含鼠标 x 和 y 坐标的 Pair
     */
    fun getCursorPos(): Pair<Double, Double> = window.getCursorPos()

    open fun place(x1: Double, y1: Double, x2: Double, y2: Double) {
        widgetBounds = ScreenBounds(x1, y1, x2, y2)
    }

    open fun place(bounds: ScreenBounds) {
        widgetBounds = bounds
    }

    open fun update() {}

    /**
     * 渲染组件
     */
    open fun render() {
        if (isVisible) {
            renderer.render(widgetBounds)
            doRender() // 子类自定义渲染
        }
    }

    /**
     * 提供钩子方法，供子类添加渲染逻辑，避免重复判断 `isVisible`
     */
    protected open fun doRender() {}

    /**
     * 清理资源
     */
    fun cleanup() {
        renderer.cleanup()
        unregisterListener()
        doCleanup() // 子类自定义清理
    }

    /**
     * 提供钩子方法，供子类添加清理逻辑
     */
    protected open fun doCleanup() {}

    // region 事件处理

    /**
     * 注册事件监听器
     */
    protected fun registerListeners() {
        window.eventManager.registerWidget(WindowResizeEvent::class, this)
        window.eventManager.registerWidget(MouseClickEvent::class, this)
        window.eventManager.registerWidget(MouseRightClickEvent::class, this)
        window.eventManager.registerWidget(MouseEnterEvent::class, this)
        window.eventManager.registerWidget(MouseLeaveEvent::class, this)
        window.eventManager.registerWidget(MouseMoveEvent::class, this)
        window.eventManager.registerWidget(MousePressedEvent::class, this)
        window.eventManager.registerWidget(MouseReleasedEvent::class, this)
        window.eventManager.registerWidget(MouseScrollEvent::class, this)
        window.eventManager.registerWidget(KeyPressedEvent::class, this)
        window.eventManager.registerWidget(KeyReleasedEvent::class, this)
    }

    /**
     * 取消注册事件监听器
     */
    protected fun unregisterListener() {
        window.eventManager.unregisterWidget(WindowResizeEvent::class, this)
        window.eventManager.unregisterWidget(MouseClickEvent::class, this)
        window.eventManager.unregisterWidget(MouseRightClickEvent::class, this)
        window.eventManager.unregisterWidget(MouseEnterEvent::class, this)
        window.eventManager.unregisterWidget(MouseLeaveEvent::class, this)
        window.eventManager.unregisterWidget(MouseMoveEvent::class, this)
        window.eventManager.unregisterWidget(MousePressedEvent::class, this)
        window.eventManager.unregisterWidget(MouseReleasedEvent::class, this)
        window.eventManager.unregisterWidget(MouseScrollEvent::class, this)
        window.eventManager.unregisterWidget(KeyPressedEvent::class, this)
        window.eventManager.unregisterWidget(KeyReleasedEvent::class, this)
    }

    /**
     * 窗口大小改变事件
     */
    @WindowResizeEventHandler
    open fun onWindowResize(e: WindowResizeEvent) {
    }

    /**
     * 鼠标按下事件
     */
    @MousePressEventHandler
    open fun onMousePress(e: MousePressedEvent) {
    }

    /**
     * 鼠标释放事件
     */
    @MouseReleaseEventHandler
    open fun onMouseRelease(e: MouseReleasedEvent) {
    }

    /**
     * 鼠标点击事件
     */
    @MouseClickEventHandler
    open fun onClick(e: MouseClickEvent) {
    }

    /**
     * 鼠标右键点击事件
     */
    @MouseRightClickEventHandler
    open fun onRightClick(e: MouseRightClickEvent) {
    }

    /**
     * 鼠标移入事件
     */
    @MouseEnterEventHandler
    open fun onMouseEnter() {
    }

    /**
     * 鼠标移出事件
     */
    @MouseLeaveEventHandler
    open fun onMouseLeave() {
    }

    /**
     * 鼠标移动事件
     */
    @MouseMoveEventHandler
    open fun onMouseMove(e: MouseMoveEvent) {
    }

    /**
     * 鼠标滚轮事件
     */
    @MouseScrollEventHandler
    open fun onMouseScroll(e: MouseScrollEvent) {
    }

    /**
     * 按键按下事件
     */
    @KeyPressEventHandler
    open fun onKeyPress(e: KeyPressedEvent): Boolean = true

    /**
     * 按键释放事件
     */
    @KeyReleaseEventHandler
    open fun onKeyReleased(e: KeyReleasedEvent): Boolean = true

    // endregion
}
