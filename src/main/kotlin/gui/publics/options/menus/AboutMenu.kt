package com.midnightcrowing.gui.publics.options.menus

import com.midnightcrowing.config.AppConfig.VERSION
import com.midnightcrowing.gui.bases.button.Button
import com.midnightcrowing.gui.publics.options.OptionMenuBase
import com.midnightcrowing.gui.publics.options.Options
import com.midnightcrowing.renderer.TextRenderer
import com.midnightcrowing.utils.LayoutScaler
import utils.BrowserUtils.openUrl

class AboutMenu(parent: Options) : OptionMenuBase(parent, "关于") {
    private val descriptionLines: List<String> = listOf(
        "本游戏的灵感来源于 Minecraft，借鉴了其中与种田相关的内容。",
        "这是一个免费游戏，主要作为大学生 Java 课程设计的项目而开发。",
        "本项目完全开源，欢迎在 GitHub 上查阅源码、参与开发或提交反馈建议。",
        "感谢您体验本游戏，祝您游戏愉快！",
        "",
        "游戏中所使用的所有资源均来自 Minecraft 原版、相关模组或公开网络资源，",
        "具体信息请参见源码仓库中的 README 文件。",
        "",
        "作者: MidnightCrowing",
        "贡献者: StevenArai",
        "版本: $VERSION"
    )

    // 每一行一个渲染器，保持样式一致
    private val descRenderers = descriptionLines.map { line ->
        TextRenderer(window.nvg).apply {
            text = line
        }
    }

    private val githubButton = Button(buttonLayout).apply {
        text = "项目 GitHub"
        onClickCallback = { openUrl("https://github.com/MidnightCrowing/IntelligentFarming") }
    }

    private val readmeButton = Button(buttonLayout).apply {
        text = "查看 README"
        onClickCallback = { openUrl("https://github.com/MidnightCrowing/IntelligentFarming?tab=readme-ov-file#readme") }
    }

    private val feedbackButton = Button(buttonLayout).apply {
        text = "提供反馈"
        onClickCallback = { openUrl("https://github.com/MidnightCrowing/IntelligentFarming/issues") }
    }

    init {
        buttonLayout.addButton(7, githubButton)
        buttonLayout.addButton(7, readmeButton)
        buttonLayout.addButton(7, feedbackButton)
    }

    override fun place(width: Int, height: Int) {
        super.place(width, height)
        val startY = height * 0.25
        val lineHeight = LayoutScaler.scaleValue(parentWidth, 30.0, 43.0)

        descRenderers.forEachIndexed { index, renderer ->
            renderer.x = width / 2.0
            renderer.y = startY + index * lineHeight
            renderer.fontSize = LayoutScaler.scaleValue(parentWidth)
        }
    }

    override fun doRender() {
        super.doRender()
        descRenderers.forEach { it.render() }
    }
}
