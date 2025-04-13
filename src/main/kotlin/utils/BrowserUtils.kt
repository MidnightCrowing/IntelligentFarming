package utils

import java.awt.Desktop
import java.net.URI
import javax.swing.JOptionPane

object BrowserUtils {
    fun openUrl(url: String) {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(URI(url))
            }
        } else {
            JOptionPane.showMessageDialog(
                null,
                "系统不支持 Desktop，请手动在浏览器中打开该网址：$url",
                "错误",
                JOptionPane.WARNING_MESSAGE
            )
        }
    }
}