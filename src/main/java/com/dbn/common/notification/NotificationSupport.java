package com.dbn.common.notification;

import com.dbn.common.util.Titles;
import com.dbn.common.project.ProjectSupplier;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Arrays;

public interface NotificationSupport extends ProjectSupplier {

    default void sendNotification(NotificationType type, NotificationGroup group, String message, Object ... args) {
        sendNotification(getProject(), type, group, message, args);
    }

    default void sendInfoNotification(NotificationGroup group, String message, Object... args) {
        sendInfoNotification(getProject(), group, message, args);
    }

    default void sendWarningNotification(NotificationGroup group, String message, Object... args) {
        sendWarningNotification(getProject(), group, message, args);
    }

    default void sendErrorNotification(NotificationGroup group, String message, Object... args) {
        sendErrorNotification(getProject(), group, message, args);
    }


    static void sendInfoNotification(@Nullable Project project, NotificationGroup group, String message, Object... args) {
        sendNotification(project, NotificationType.INFORMATION, group, message, args);
    }

    static void sendWarningNotification(@Nullable Project project, NotificationGroup group, String message, Object... args) {
        sendNotification(project, NotificationType.WARNING, group, message, args);
    }

    static void sendErrorNotification(@Nullable Project project, NotificationGroup area, String message, Object... args) {
        sendNotification(project, NotificationType.ERROR, area, message, args);
    }

    static void sendNotification(@Nullable Project project, NotificationType type, NotificationGroup group, String message, Object... args) {
        if (project != null && project.isDisposed()) return;

        args = args == null ? null : Arrays.stream(args).map(o -> {
            if (o instanceof Exception) {
                Exception exception = (Exception) o;
                return exception.getMessage();
            } else {
                return o.toString();
            }
        }).toArray();

        message = MessageFormat.format(message, args);
        Notification notification = new Notification(
                "DBNavigator.NotificationGroup",
                Titles.signed(group.toString()),
                message,
                type);
        notification.setImportant(false);
        Notifications.Bus.notify(notification, project);
    }
}
