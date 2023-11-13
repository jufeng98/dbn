package com.dbn.common.thread;

import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Command {

    public static void run(Project project, String commandName, Runnable command) {
        Write.run(() -> {
            CommandProcessor commandProcessor = CommandProcessor.getInstance();
            commandProcessor.executeCommand(project, command, commandName, null);
        });

    }
}
