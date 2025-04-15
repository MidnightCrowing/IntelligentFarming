package utils

import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.net.URI
import javax.swing.*

object BrowserUtils {
    fun openUrl(url: String) {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(URI(url))
                    return
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // 构造弹框内容
        val label = JLabel("<html>系统不支持自动打开浏览器，<br>请手动在浏览器中打开以下网址：</html>")
        val urlField = JTextField(url)
        urlField.isEditable = false

        val copyButton = JButton("复制 URL")
        copyButton.addActionListener {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val selection = StringSelection(url)
            clipboard.setContents(selection, null)
            JOptionPane.showMessageDialog(null, "URL 已复制到剪贴板！")
        }

        val panel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(label)
            add(Box.createVerticalStrut(8))
            add(urlField)
            add(Box.createVerticalStrut(8))
            add(copyButton)
        }

        JOptionPane.showMessageDialog(
            null,
            panel,
            "无法打开浏览器",
            JOptionPane.WARNING_MESSAGE
        )
    }
}
