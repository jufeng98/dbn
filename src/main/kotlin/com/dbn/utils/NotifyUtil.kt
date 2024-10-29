package com.dbn.utils

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

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

    fun notifyInfo(project: Project, msg: String) {
        NONE.createNotification("Tip", msg, NotificationType.INFORMATION).notify(project)
    }


}
