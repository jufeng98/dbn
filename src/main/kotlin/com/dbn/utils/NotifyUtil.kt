package com.dbn.utils

import com.dbn.browser.DatabaseBrowserManager
import com.dbn.execution.ExecutionManager
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.wm.ToolWindowManager

/**
 * 通知
 */
object NotifyUtil {
    private val STICKY_BALLOON_GROUP: NotificationGroup =
        NotificationGroupManager.getInstance().getNotificationGroup("DataBaseManager.STICKY_BALLOON")
    private val NONE: NotificationGroup =
        NotificationGroupManager.getInstance().getNotificationGroup("DataBaseManager.NONE")

    fun notifySuccess(project: Project, message: String) {
        STICKY_BALLOON_GROUP.createNotification("Tip", message, NotificationType.INFORMATION).notify(project)
    }

    fun notifyWarn(project: Project, title: String, message: String) {
        STICKY_BALLOON_GROUP.createNotification(title, message, NotificationType.WARNING).notify(project)
    }

    fun notifyInfo(project: Project, msg: String) {
        NONE.createNotification("Tip", msg, NotificationType.INFORMATION).notify(project)
    }

    fun notifyDbToolWindowInfo(project: Project, msg: String) {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val htmlBody = "<div style='font-size:18pt'>${msg}</div>"
        toolWindowManager.notifyByBalloon(DatabaseBrowserManager.TOOL_WINDOW_ID, MessageType.INFO, htmlBody)
    }

    fun notifyConsoleToolWindowInfo(project: Project, msg: String) {
        val toolWindowManager = ToolWindowManager.getInstance(project)
        val htmlBody = "<div style='font-size:18pt'>${msg}</div>"
        toolWindowManager.notifyByBalloon(ExecutionManager.TOOL_WINDOW_ID, MessageType.INFO, htmlBody)
    }


}
